package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.entity.ai.HealSelfCastGoal;
import alexthw.ars_elemental.common.entity.ai.MageProjCastingGoal;
import com.alexthw.sauce.api.item.ISchoolFocus;
import com.alexthw.sauce.api.item.ISchoolProvider;
import com.hollingsworth.arsnouveau.api.registry.SpellCasterRegistry;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.block.tile.IAnimationListener;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHarm;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectKnockback;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static alexthw.ars_elemental.common.items.armor.ElementalArmor.getArmorSetFromElement;
import static com.alexthw.sauce.util.ParticleUtil.schoolToColor;

public class EntityMageBase extends Monster implements RangedAttackMob, ISchoolProvider, IAnimationListener {

    public static final Spell SWORD_SPELL = new Spell(MethodTouch.INSTANCE, EffectKnockback.INSTANCE);
    public final List<Spell> pSpells = new ArrayList<>();

    public SpellSchool school;
    public String type = "medium";

    public int castCooldown = 0;
    public int selfCastCooldown = 0;
    public int animationTimer = 0;
    public int currentAnim = -1;

    public EntityMageBase(EntityType<? extends Monster> type, Level level, @Nullable SpellSchool school) {
        this(type, level);
        this.school = school;
    }

    static final String[] types = {"light", "medium", "heavy"};

    @Override
    public void tick() {
        super.tick();
        if (castCooldown > 0) castCooldown--;
        if (selfCastCooldown > 0) selfCastCooldown--;
        if (animationTimer > 0) animationTimer--;
        if (currentAnim > 0 && animationTimer == 0) {
            currentAnim = -1;
        }
    }

    /**
     * Default Proj -> simple harm
     * Default Self -> simple heal
     * Sword spell -> knockback
     */
    protected EntityMageBase(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        if (pSpells.isEmpty()) {
            pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectHarm.INSTANCE, AugmentAmplify.INSTANCE));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
        this.populateDefaultEquipmentSlots(pLevel.getRandom(), pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    public static ItemStack getArmorForSlot(EquipmentSlot slot, SpellSchool school, String type) {
        Item item = switch (slot) {
            case HEAD -> getArmorSetFromElement(school, type).getHat();
            case CHEST -> getArmorSetFromElement(school, type).getChest();
            case LEGS -> getArmorSetFromElement(school, type).getLegs();
            case FEET -> getArmorSetFromElement(school, type).getBoots();
            default -> null;
        };

        if (item == null) return ItemStack.EMPTY;

        return item.getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 48D);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, EntityMageBase.class, true, e -> e instanceof EntityMageBase mage && school != mage.school));
        if (ConfigHandler.Common.MAGES_AGGRO.get()) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, e -> e instanceof Player player && !ISchoolFocus.getFociSchools(player).contains(school)));
        }
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(10, new NearestAttackableTargetGoal<>(this, Monster.class, true, e -> !(e instanceof EntityMageBase)));
        // Only activate melee if the target is truly in melee range (within ~3 blocks) to knock them back
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true) {
            private final double maxActivateDistanceSqr = 16.0D; // 3 blocks

            @Override
            public boolean canUse() {
                LivingEntity target = mob.getTarget();
                if (target == null) return false;
                if (mob.distanceToSqr(target) > maxActivateDistanceSqr) return false;
                return super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                LivingEntity target = mob.getTarget();
                if (target == null) return false;
                if (mob.distanceToSqr(target) > maxActivateDistanceSqr) return false;
                return super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(3, new MageProjCastingGoal<>(this, 1.1d, 48f, () -> castCooldown <= 0, 2, 10));
        this.goalSelector.addGoal(2, new HealSelfCastGoal<>(this, 0.33, 1, 10));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new FloatGoal(this));

        super.registerGoals();
    }

    @Override
    public void startAnimation(int arg) {
        currentAnim = arg;
        animationTimer = arg * 20;
    }

    public static class MageResolver extends EntitySpellResolver {

        public SpellSchool getSchool() {
            return school;
        }

        private final SpellSchool school;

        public MageResolver(SpellContext context, SpellSchool school) {
            super(context);
            this.school = school;
        }

        @Override
        public boolean hasFocus(ItemStack stack) {
            return hasFocus(stack.getItem());
        }

        @Override
        public boolean hasFocus(Item item) {
            if (item instanceof ISchoolFocus focus) {
                return school == focus.getSchool();
            } else if (item == ItemsRegistry.SHAPERS_FOCUS.get()) {
                return true;
            }
            return super.hasFocus(item);
        }

        @Override
        public SpellResolver getNewResolver(SpellContext context) {
            return new MageResolver(context, school);
        }
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("cast", castCooldown);
        tag.putInt("selfCast", selfCastCooldown);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.castCooldown = tag.getInt("cast");
        this.selfCastCooldown = tag.getInt("selfCast");
    }

    //Monster overrides

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity pEntity) {
        return super.isAlliedTo(pEntity) || pEntity instanceof LivingEntity living && ISchoolFocus.getFociSchools(living).contains(this.school);
    }

    public int getMaxSpawnClusterSize() {
        return 3;
    }

    @Override
    public int getBaseExperienceReward() {
        return 15;
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pDistanceFactor) {
        Spell spell = this.pSpells.get(random.nextInt(pSpells.size()));
        ParticleColor color = schoolToColor(this.school.getId());
        EntitySpellResolver resolver = new MageResolver(new SpellContext(level(), spell, this, new LivingCaster(this)).withColors(color), school);
        resolver.onCast(ItemStack.EMPTY, level());
        this.castCooldown = 20;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        String random = "medium";// types[randomSource.nextInt(types.length)];
        if (school != null) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                setItemSlot(slot, getArmorForSlot(slot, this.school, random));
            }
        } else {
            setItemSlot(EquipmentSlot.HEAD, ItemsRegistry.BATTLEMAGE_HOOD.get().getDefaultInstance());
            setItemSlot(EquipmentSlot.CHEST, ItemsRegistry.BATTLEMAGE_ROBES.get().getDefaultInstance());
            setItemSlot(EquipmentSlot.LEGS, ItemsRegistry.BATTLEMAGE_LEGGINGS.get().getDefaultInstance());
            setItemSlot(EquipmentSlot.FEET, ItemsRegistry.BATTLEMAGE_BOOTS.get().getDefaultInstance());
        }
        setItemInHand(InteractionHand.OFF_HAND, ItemsRegistry.APPRENTICE_SPELLBOOK.get().getDefaultInstance());
        var sword = ItemsRegistry.ENCHANTERS_SWORD.get().getDefaultInstance();
        try {
            Objects.requireNonNull(SpellCasterRegistry.from(sword)).setSpell(SWORD_SPELL).saveToStack(sword);
        } catch (NullPointerException ignored) {
            System.out.println("Failed to create spell caster for enchanter sword. How did we end up here?");
        }
        setItemInHand(InteractionHand.MAIN_HAND, sword);
    }

    @Override
    public SpellSchool getSchool() {
        return school;
    }
}
