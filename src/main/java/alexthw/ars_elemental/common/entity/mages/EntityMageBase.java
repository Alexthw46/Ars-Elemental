package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.entity.ai.ProjCastingGoal;
import alexthw.ars_elemental.common.entity.ai.SelfCastGoal;
import alexthw.ars_elemental.common.items.ISchoolItem;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.util.ElementalArsenal;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHarm;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static alexthw.ars_elemental.util.ParticleUtil.schoolToColor;

public class EntityMageBase extends Monster implements RangedAttackMob {

    public final List<Spell> pSpells = new ArrayList<>();
    public final List<Spell> sSpells = new ArrayList<>();

    public SpellSchool school;

    public int castCooldown = 0;

    /**
     * Default Proj -> simple harm
     * Default Self -> simple heal
     */
    protected EntityMageBase(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectHarm.INSTANCE, AugmentAmplify.INSTANCE));
        sSpells.add(new Spell(MethodSelf.INSTANCE, EffectHeal.INSTANCE, AugmentAmplify.INSTANCE));
    }

    @Override
    public void tick() {
        super.tick();
        if (castCooldown > 0) castCooldown--;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, EntityMageBase.class, true, (e) -> e instanceof EntityMageBase mage && school != mage.school));
        if (ConfigHandler.Common.MAGES_AGGRO.get()) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, (e) -> (e instanceof Player player && ISchoolItem.hasFocus(player.level, player) != school)));
        }
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(10, new NearestAttackableTargetGoal<>(this, Monster.class, true, (e) -> !(e instanceof EntityMageBase)));
        this.goalSelector.addGoal(3, new ProjCastingGoal<>(this, 1.0d, 30, 36f, () -> castCooldown <= 0, 0, 10));
        this.goalSelector.addGoal(6, new SelfCastGoal<>(this, 20, 0, () -> (castCooldown <= 0 && (getHealth() < getMaxHealth() / 4)), 0, 10));

        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        super.registerGoals();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        if (school != null && CompatUtils.isArsenalLoaded()) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                setItemSlot(slot, ElementalArsenal.getArmorForSlot(slot, this.school));
            }
        } else {
            setItemSlot(EquipmentSlot.HEAD, ItemsRegistry.ARCHMAGE_HOOD.getDefaultInstance());
            setItemSlot(EquipmentSlot.CHEST, ItemsRegistry.ARCHMAGE_ROBES.getDefaultInstance());
            setItemSlot(EquipmentSlot.LEGS, ItemsRegistry.ARCHMAGE_LEGGINGS.getDefaultInstance());
            setItemSlot(EquipmentSlot.FEET, ItemsRegistry.ARCHMAGE_BOOTS.getDefaultInstance());
        }
        setItemInHand(InteractionHand.MAIN_HAND, ItemsRegistry.APPRENTICE_SPELLBOOK.getDefaultInstance());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 16D);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pDistanceFactor) {
        Spell spell = this.pSpells.get(random.nextInt(pSpells.size()));
        ParticleColor color = schoolToColor(this.school.getId());
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(spell, this).withColors(color.toWrapper()).withType(SpellContext.CasterType.ENTITY));
        resolver.onCast(ItemStack.EMPTY, this, level);
        this.castCooldown = 40;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("cast", castCooldown);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.castCooldown = tag.getInt("cast");
    }

    //Monster overrides
    @Override
    public float getWalkTargetValue(@NotNull BlockPos pPos, @NotNull LevelReader pLevel) {
        return super.getWalkTargetValue(pPos,pLevel);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

}
