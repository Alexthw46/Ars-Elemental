package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.common.entity.ai.HijackTurretGoal;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.alexthw.sauce.api.item.ISchoolProvider;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.util.SummonUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.items.data.ICharmSerializable;
import com.hollingsworth.arsnouveau.common.items.data.PersistentFamiliarData;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FlashjackEntity extends Parrot implements GeoEntity, ICharmSerializable, IDispellable, ISchoolProvider {
    public static final EntityDataAccessor<String> COLOR = SynchedEntityData.defineId(FlashjackEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Optional<BlockPos>> HOME = SynchedEntityData.defineId(FlashjackEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation idle = RawAnimation.begin().thenLoop("idle.air");
    public static final RawAnimation inactive = RawAnimation.begin().thenPlayAndHold("idle.ground");
    public static final RawAnimation flapping = RawAnimation.begin().thenLoop("idle.flapping");
    public static final RawAnimation attack = RawAnimation.begin().thenPlayAndHold("attack");

    public FlashjackEntity(EntityType<? extends Parrot> entityType, Level level) {
        super(entityType, level);
    }

    public FlashjackEntity(Level world) {
        super(ModEntities.FLASHJACK_ENTITY.get(), world);
    }

    @Override
    public SpellSchool getSchool() {
        return SpellSchools.ELEMENTAL_AIR;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new HijackTurretGoal(this, 40));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        return SummonUtil.canSummonTakeDamage(pSource) && super.hurt(pSource, pAmount);
    }

    @Override
    public void tick() {
        super.tick();
        SummonUtil.healOverTime(this);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        if (!level.isClientSide && isTamed()) {
            ItemStack stack = new ItemStack(ModItems.FLASHJACK_CHARM);
            stack.set(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, createCharmData());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (!player.level().isClientSide && this.isOwnedBy(player)) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.is(Tags.Items.DYES_YELLOW) && !this.getColor().equals("flashjack")) {
                this.setColor("flashjack");
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }

            if (stack.is(Tags.Items.DYES_RED) && !this.getColor().equals("flapjack")) {
                this.setColor("flapjack");
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            if (stack.is(Tags.Items.DYES_BLUE) && !this.getColor().equals("bluejay")) {
                this.setColor("bluejay");
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public boolean isTamed() {
        return this.getOwner() != null;
    }

    public static boolean checkSpawnRules(EntityType<? extends Parrot> parrot, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    public boolean isActive() {
        return this.getTarget() != null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "idle_controller", 5, event -> onGround() ? event.setAndContinue(inactive) : event.setAndContinue(idle)));
        data.add(new AnimationController<>(this, "action_controller", 5, event -> {
            // Play the attack animation if the entity has a target, try to not loop it
            if (isActive() && !event.isCurrentAnimation(attack)) {
                return event.setAndContinue(attack);
            }
            // If the entity is on the ground, stop the animation
            if (event.getAnimatable().onGround())
                return PlayState.STOP;
            // If the entity is in the attack animation, continue it until the end
            if (event.isCurrentAnimation(attack) && event.animationTick < 100) {
                return PlayState.CONTINUE;
            }
            // Defaults to flapping animation if in the air and not attacking
            return event.setAndContinue(flapping);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    public @Nullable BlockPos getHome() {
        return this.entityData.get(HOME).orElse(null);
    }

    public void setHome(BlockPos home) {
        this.entityData.set(HOME, Optional.of(home));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HOME, Optional.empty());
        builder.define(COLOR, "flashjack");
    }

    @Override
    public boolean onDispel(@javax.annotation.Nullable LivingEntity caster) {
        if (this.isRemoved())
            return false;

        if (!level.isClientSide && isTamed()) {
            ItemStack stack = new ItemStack(ModItems.FLASHJACK_CHARM);
            stack.set(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, createCharmData());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
            ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
            this.remove(RemovalReason.DISCARDED);
        }
        return this.isTamed();
    }

    @Override
    public void fromCharmData(PersistentFamiliarData data) {
        setColor(data.color());
        setCustomName(data.name());
    }

    public String getColor() {
        return this.entityData.get(COLOR);
    }

    public void setColor(String color) {
        this.entityData.set(COLOR, color);
    }

    public ResourceLocation getTexture() {
        return prefix("textures/entity/" + (getColor().isEmpty() ? "flashjack" : getColor()) + ".png");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("color", this.entityData.get(COLOR));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(COLOR, compound.getString("color"));
    }
    
}
