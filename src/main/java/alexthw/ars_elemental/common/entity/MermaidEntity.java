package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.common.entity.ai.DolphinJumpGoal;
import alexthw.ars_elemental.common.entity.ai.FollowBoatGoalM;
import alexthw.ars_elemental.common.entity.ai.HybridStrollGoal;
import alexthw.ars_elemental.common.entity.ai.MermaidAi;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import com.hollingsworth.arsnouveau.common.block.tile.IAnimationListener;
import com.hollingsworth.arsnouveau.common.compat.PatchouliHandler;
import com.hollingsworth.arsnouveau.common.entity.goal.GoBackHomeGoal;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.PlayState;
import software.bernie.ars_nouveau.geckolib3.core.builder.AnimationBuilder;
import software.bernie.ars_nouveau.geckolib3.core.controller.AnimationController;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationData;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationFactory;
import software.bernie.ars_nouveau.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MermaidEntity extends PathfinderMob implements IAnimatable, IAnimationListener, IVariantColorProvider<MermaidEntity>, IDispellable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(MermaidEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(MermaidEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> COLOR = SynchedEntityData.defineId(MermaidEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Optional<BlockPos>> HOME = SynchedEntityData.defineId(MermaidEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public MermaidEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.1F, 0.7F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.maxUpStep = 1.5F;
    }

    public MermaidEntity(Level level, boolean tamed) {
        this(ModEntities.SIREN_ENTITY.get(), level);
        setTamed(tamed);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> p_149138_) {
        return MermaidAi.makeBrain((Brain<MermaidEntity>) this.brainProvider().makeBrain(p_149138_));
    }

    @Override
    public Brain<MermaidEntity> getBrain() {
        return (Brain<MermaidEntity>) super.getBrain();
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new HybridStrollGoal(this, 1.0f, 40));
        goalSelector.addGoal(3, new BreathAirGoal(this));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6F));
        goalSelector.addGoal(4, new DolphinJumpGoal(this, 10));
        goalSelector.addGoal(5, new GoBackHomeGoal(this, this::getHome, 20, () -> this.getHome() != null));
        goalSelector.addGoal(8, new FollowBoatGoalM(this, () -> this.getHome() == null));
    }

    @Override
    public void die(DamageSource source) {
        if (!level.isClientSide && isTamed()) {
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), ModItems.SIREN_CHARM.get().getDefaultInstance()));
        }
        super.die(source);
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float p_70097_2_) {
        if (source == DamageSource.DROWN || source == DamageSource.IN_WALL || source == DamageSource.SWEET_BERRY_BUSH || source == DamageSource.CACTUS)
            return false;
        return super.hurt(source, p_70097_2_);
    }

    //Dolphin inheritance
    public boolean checkSpawnObstruction(LevelReader pLevel) {
        return pLevel.isUnobstructed(this);
    }

    public int getMaxAirSupply() {
        return 6000;
    }

    public boolean removeWhenFarAway(double dist) {
        return !isTamed();
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int getExperienceReward() {
        return 0;
    }

    //IDispellable
    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if (this.isRemoved())
            return false;

        if (!level.isClientSide && isTamed()) {
            ItemStack stack = new ItemStack(ModItems.SIREN_CHARM.get());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
            ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
            this.remove(RemovalReason.DISCARDED);
        }
        return this.isTamed();
    }

    //gecko stuff
    AnimationController<MermaidEntity> actions;

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::idle));
        actions = new AnimationController<>(this, "actions", 10, this::actions);
        data.addAnimationController(actions);
    }

    private PlayState idle(AnimationEvent<?> event) {
        if (isJumping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("jump"));
            return PlayState.CONTINUE;
        }
        if (getDeltaMovement().y > 0.3) {
            setJump(true);
            event.getController().setAnimation(new AnimationBuilder().addAnimation("jump"));
        } else if (isOnGround() && !isInWater() || (level.isClientSide && PatchouliHandler.isPatchouliWorld())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("ground"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("floating"));
        }
        return PlayState.CONTINUE;
    }

    private PlayState actions(AnimationEvent<?> event) {

        if (getDeltaMovement().length() > 0 || (level.isClientSide && PatchouliHandler.isPatchouliWorld())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    //data stuff

    public static AttributeSupplier createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.4F)
                .build();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOME, Optional.empty());
        this.entityData.define(TAMED, false);
        this.entityData.define(JUMPING, false);
        this.entityData.define(COLOR, Variants.random(this.random).toString());
    }

    public boolean isJumping() {
        return this.entityData.get(JUMPING);
    }

    public void setJump(boolean is) {
        this.entityData.set(JUMPING, is);
    }

    public boolean isTamed() {
        return this.entityData.get(TAMED);
    }

    public void setTamed(boolean tamed) {
        this.entityData.set(TAMED, tamed);
    }

    public void setHome(BlockPos home) {
        this.entityData.set(HOME, Optional.of(home));
    }

    public @Nullable
    BlockPos getHome() {
        return this.entityData.get(HOME).orElse(null);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.storeBlockPos(tag, "home", getHome());
        tag.putBoolean("tamed", this.entityData.get(TAMED));
        tag.putString("color", this.entityData.get(COLOR));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (NBTUtil.hasBlockPos(tag, "home"))
            this.entityData.set(HOME, Optional.of(NBTUtil.getBlockPos(tag, "home")));
        setTamed(tag.getBoolean("tamed"));
        this.entityData.set(COLOR, tag.getString("color"));
    }

    public int getMaxSpawnClusterSize() {
        return 3;
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(LevelAccessor levelAccessor, BlockPos pos) {
        int i = levelAccessor.getSeaLevel() + 10;
        int j = i - 30;
        boolean f1 = pos.getY() >= j && pos.getY() <= i;
        return f1 && levelAccessor.getFluidState(pos.below()).is(FluidTags.WATER) && levelAccessor.getBlockState(pos.above()).is(Blocks.WATER);
    }

    //Pathfinder
    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MermaidPathNavigation<>(this, pLevel);
    }

    @Override
    public void startAnimation(int arg) {

    }

    public static class MermaidPathNavigation<E extends PathfinderMob> extends WaterBoundPathNavigation {
        public MermaidPathNavigation(E p_149218_, Level p_149219_) {
            super(p_149218_, p_149219_);
        }

        /**
         * If on ground or swimming and can swim
         */
        protected boolean canUpdatePath() {
            return true;
        }

        protected PathFinder createPathFinder(int p_149222_) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
            return new PathFinder(this.nodeEvaluator, p_149222_);
        }

        public boolean isStableDestination(BlockPos p_149224_) {
            return !this.level.getBlockState(p_149224_.below()).isAir();
        }
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }
    }

    int animTicks;
    boolean taming;

    @Override
    protected void customServerAiStep() {
        this.getBrain().tick((ServerLevel) this.level, this);
        MermaidAi.updateActivity(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (isJumping() && animTicks <= 30) {
            animTicks++;
        }
        if (animTicks > 30) {
            setJump(false);
            animTicks = 0;
            if (!isTamed() && taming) {
                taming = false;
                ItemStack stack = new ItemStack(ModItems.SIREN_SHARDS.get(), 1 + level.random.nextInt(2));
                level.addFreshEntity(new ItemEntity(level, getX(), getY() + 0.5, getZ(), stack));
                ANCriteriaTriggers.rewardNearbyPlayers(ANCriteriaTriggers.POOF_MOB, (ServerLevel) this.level, this.getOnPos(), 10);
                this.remove(RemovalReason.DISCARDED);
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.NEUTRAL, 1f, 1f);
            }
        }
    }

    //Variants

    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND || pPlayer.getCommandSenderWorld().isClientSide)
            return InteractionResult.PASS;

        ItemStack stack = pPlayer.getItemInHand(hand);

        if (isTamed()) {
            String color = Variants.getColorFromStack(stack);
            if (color != null && !getColor(this).equals(color)) {
                this.setColor(color, this);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        } else if (stack.getItem() == Items.SEA_PICKLE && !taming) {
            this.setDeltaMovement(getDeltaMovement().add(0, 0.8, 0));
            this.setJump(true);
            this.markHurt();
            taming = true;
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(pPlayer, pVec, hand);
    }

    public enum Variants {
        KELP,
        BUBBLE,
        FIRE,
        TUBE,
        HORN,
        BRAIN;

        public static String getColorFromStack(ItemStack stack) {

            if (stack.getItem() == Items.KELP) return KELP.toString();
            if (stack.getItem() == Items.BUBBLE_CORAL) return BUBBLE.toString();
            if (stack.getItem() == Items.HORN_CORAL) return HORN.toString();
            if (stack.getItem() == Items.TUBE_CORAL) return TUBE.toString();
            if (stack.getItem() == Items.FIRE_CORAL) return FIRE.toString();
            if (stack.getItem() == Items.BRAIN_CORAL) return BRAIN.toString();

            return null;
        }

        public static Variants random(RandomSource random) {
            Map<Integer, Variants> ordinalMap = Arrays.stream(Variants.values()).collect(Collectors.toMap(Enum::ordinal, var -> var, (a, b) -> b));

            return ordinalMap.get(random.nextInt(ordinalMap.size()));
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public String getColor(MermaidEntity mermaidEntity) {
        return this.entityData.get(COLOR);
    }

    public void setColor(String color, MermaidEntity mermaidEntity) {
        this.entityData.set(COLOR, color);
    }

    @Override
    public ResourceLocation getTexture(MermaidEntity entity) {
        return prefix("textures/entity/mermaid_" + (getColor(entity).isEmpty() ? Variants.KELP.toString() : getColor(entity)) + ".png");
    }

}
