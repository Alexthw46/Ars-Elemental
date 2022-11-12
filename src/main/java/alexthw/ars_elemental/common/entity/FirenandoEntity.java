package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ConfigHandler.Common;
import alexthw.ars_elemental.common.entity.ai.FireCannonGoal;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.client.ITooltipProvider;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.IAnimationListener;
import com.hollingsworth.arsnouveau.common.entity.goal.GoBackHomeGoal;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.PlayState;
import software.bernie.ars_nouveau.geckolib3.core.builder.AnimationBuilder;
import software.bernie.ars_nouveau.geckolib3.core.controller.AnimationController;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationData;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FirenandoEntity extends PathfinderMob implements RangedAttackMob, IAnimatable, ITooltipProvider, IAnimationListener, IWandable, IDispellable, IVariantColorProvider<FirenandoEntity> {
    public FirenandoEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    public FirenandoEntity(Level world) {
        super(ModEntities.FIRENANDO_ENTITY.get(), world);
    }

    private int castCooldown = 0;
    private final ParticleColor color = new ParticleColor(250, 50, 15);
    private final ParticleColor colorAlt = new ParticleColor(15, 100, 200);
    public final Spell spell = new Spell(MethodHomingProjectile.INSTANCE, EffectIgnite.INSTANCE, AugmentSensitive.INSTANCE, EffectFlare.INSTANCE);
    public UUID owner;

    @Override
    public void tick() {
        super.tick();
        if (castCooldown > 0) {
            this.castCooldown--;
        }
        if (!level.isClientSide() && level.getGameTime() % 20 == 0 && this.isActive() && this.getHealth() < this.getMaxHealth()) {
            this.heal(1.0f);
        }
    }


    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source == DamageSource.CACTUS || source == DamageSource.SWEET_BERRY_BUSH || source.isFire())
            return false;
        if (!isActive()) return false;
        return super.hurt(source, amount);
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || !isActive();
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (!level.isClientSide && !source.isBypassInvul()) {
            this.entityData.set(ACTIVE, false);
            if (source.getEntity() != null) {
                if (Common.FIRENANDO_KILL.get() && source.getEntity().getUUID().equals(getOwner())) {
                    level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(ModItems.FIRENANDO_CHARM.get())));
                    super.die(source);
                    return;
                }

                if (source.getEntity() instanceof Mob mob) {
                    mob.setTarget(null);
                }
            }
            this.setHealth(0.1F);
            return;
        }
        super.die(source);
    }

    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if (this.isRemoved() || level.isClientSide) return false;

        level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(ModItems.FIRENANDO_CHARM.get())));
        ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
        this.remove(RemovalReason.DISCARDED);

        return true;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float p_82196_2_) {
        ParticleColor spellColor = getColor(this).equals(Variants.MAGMA.toString()) ? color : colorAlt;
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(level, spell, this, new LivingCaster(this)).withColors(spellColor));
        EntityHomingProjectile projectileSpell = new EntityHomingProjectile(level, resolver);
        List<Predicate<LivingEntity>> ignore = MethodHomingProjectile.basicIgnores(this, false, resolver.spell);
        ignore.add(entity -> !(entity instanceof Enemy));
        ignore.add(entity -> entity instanceof FirenandoEntity firenando && getOwner().equals(firenando.getOwner()));
        projectileSpell.setColor(spellColor);
        projectileSpell.shoot(this, this.getXRot(), this.getYRot(), 0.0F, 0.8f, 0.8f);
        projectileSpell.setIgnored(ignore);
        level.addFreshEntity(projectileSpell);
        this.castCooldown = 20;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new GoBackHomeGoal(this, this::getHome, 10, () -> this.getTarget() == null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, false, (entity) -> entity instanceof Enemy));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new FireCannonGoal(this, 0.8d, 20, 55f, () -> castCooldown <= 0 && isActive(), Animations.SHOOT.ordinal(), 20));
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

    @Override
    public int getExperienceReward() {
        return 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16D);
    }

    public static final EntityDataAccessor<Optional<BlockPos>> HOME = SynchedEntityData.defineId(FirenandoEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    public static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(FirenandoEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(FirenandoEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> COLOR = SynchedEntityData.defineId(FirenandoEntity.class, EntityDataSerializers.STRING);

    @Override
    public void onFinishedConnectionFirst(@Nullable BlockPos storedPos, @Nullable LivingEntity storedEntity, Player playerEntity) {
        if (storedPos != null) {
            setHome(storedPos);
            PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.home_set"));
        }
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }

    public void setHome(BlockPos home) {
        this.entityData.set(HOME, Optional.of(home));
    }

    public @Nullable
    BlockPos getHome() {
        return this.entityData.get(HOME).orElse(null);
    }

    public UUID getOwner(){
        return owner;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOTING, false);
        this.entityData.define(ACTIVE, true);
        this.entityData.define(HOME, Optional.empty());
        this.entityData.define(COLOR, Variants.MAGMA.toString());
    }

    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.storeBlockPos(tag, "home", getHome());
        tag.putInt("cast", castCooldown);
        tag.putBoolean("active", this.entityData.get(ACTIVE));
        tag.putString("color", this.entityData.get(COLOR));
        if (owner != null) tag.putUUID("owner", owner);

    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (NBTUtil.hasBlockPos(tag, "home")) {
            setHome(NBTUtil.getNullablePos(tag, "home"));
        }
        this.castCooldown = tag.getInt("cast");
        this.entityData.set(ACTIVE, tag.getBoolean("active"));
        this.entityData.set(COLOR, tag.getString("color"));
        this.owner = tag.hasUUID("owner") ? tag.getUUID("owner") : null;
    }

    final AnimationFactory factory = new AnimationFactory(this);

    AnimationController<FirenandoEntity> attackController;

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle_controller", 5f, this::idlePredicate));
        attackController = new AnimationController<>(this, "attack_controller", 1f, this::attackPredicate);
        data.addAnimationController(attackController);
    }

    <T extends IAnimatable> PlayState attackPredicate(AnimationEvent<T> event) {
        if (!isActive()) return PlayState.STOP;
        if (attackController.getCurrentAnimation() == null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        }
        return PlayState.CONTINUE;
    }

    <T extends IAnimatable> PlayState idlePredicate(AnimationEvent<T> event) {
        if (isActive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle.body"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("inactive"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void startAnimation(int arg) {
        if (arg == Animations.SHOOT.ordinal()) {
            if (attackController.getCurrentAnimation() != null && (attackController.getCurrentAnimation().animationName.equals("shoot"))) {
                return;
            }
            attackController.markNeedsReload();
            attackController.setAnimation(new AnimationBuilder().addAnimation("shoot", false).addAnimation("idle", false));
        }
    }

    public void setOwner(Player player) {
        this.owner = player.getUUID();
    }

    @Override
    public void getTooltip(List<Component> tooltip) {
        if (getHome() != null) {
            String home = getHome().getX() + ", " + getHome().getY() + ", " + getHome().getZ();
            tooltip.add(Component.translatable("ars_nouveau.weald_walker.home", home));
        } else {
            tooltip.add(Component.translatable("ars_nouveau.weald_walker.home", Component.translatable("ars_nouveau.nothing").getString()));
        }
    }

    public enum Animations {
        SHOOT
    }

    public enum Variants {
        MAGMA,
        SOUL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public String getColor(FirenandoEntity firenando) {
        return this.entityData.get(COLOR);
    }

    @Override
    public void setColor(String color) {
        setColor(color, null);
    }

    public void setColor(String color, FirenandoEntity firenando) {
        this.entityData.set(COLOR, color);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.level.isClientSide && player.getUUID().equals(owner)) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() == Blocks.MAGMA_BLOCK.asItem() && !getColor(this).equals(Variants.MAGMA.toString())) {
                this.setColor(Variants.MAGMA.toString(), this);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            if (stack.getItem() == Blocks.SOUL_SAND.asItem() && !getColor(this).equals(Variants.SOUL.toString())) {
                this.setColor(Variants.SOUL.toString(), this);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        if (!player.level.isClientSide() && !isActive()) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.getItem() == Items.MAGMA_CREAM || stack.getItem() == Items.BLAZE_POWDER) {
                this.heal(100);
                this.entityData.set(ACTIVE, true);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public ResourceLocation getTexture(FirenandoEntity entity) {
        if (!isActive()) return prefix("textures/entity/firenando_inactive.png");
        return prefix("textures/entity/firenando_" + (getColor(entity).isEmpty() ? Variants.MAGMA.toString() : getColor(entity)) + ".png");
    }

}
