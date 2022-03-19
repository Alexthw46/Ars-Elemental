package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.compat.PatchouliHandler;
import com.hollingsworth.arsnouveau.common.entity.goal.GoBackHomeGoal;
import com.hollingsworth.arsnouveau.common.entity.goal.whirlisprig.FollowPlayerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MermaidEntity extends PathfinderMob implements IAnimatable, IDispellable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public static final EntityDataAccessor<Boolean> TAMED = SynchedEntityData.defineId(MermaidEntity.class, EntityDataSerializers.BOOLEAN);
    public BlockPos homePos;
    private boolean setBehaviors;


    public MermaidEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.moveControl =  new FlyingMoveControl(this, 10, true);
        addGoalsAfterConstructor();
    }

    public MermaidEntity(Level level, boolean tamed){
        this(ModRegistry.SIREN_ENTITY.get(), level);
        setTamed(tamed);
        addGoalsAfterConstructor();
    }

    protected void addGoalsAfterConstructor(){
        if (this.level.isClientSide())
            return;

        for(WrappedGoal goal : getGoals()){
            this.goalSelector.addGoal(goal.getPriority(), goal.getGoal());
        }
    }

    public List<WrappedGoal> getGoals(){
        return this.entityData.get(TAMED) ? getTamedGoals() : getWildGoals();
    }

    @Override
    protected void registerGoals() { }

    public List<WrappedGoal> getTamedGoals(){
        List<WrappedGoal> list = new ArrayList<>();
        list.add(new WrappedGoal(3, new RandomLookAroundGoal(this)));
        list.add(new WrappedGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F)));
        list.add(new WrappedGoal(2, new GoBackHomeGoal(this, this::getHome, 5, () -> this.getTarget() == null)));

        return list;
    }

    public BlockPos getHome() {
        return this.homePos;
    }

    public List<WrappedGoal> getWildGoals() {
        List<WrappedGoal> list = new ArrayList<>();
        list.add(new WrappedGoal(6, new FollowPlayerGoal(this, 1.0D, 5.0F, 5.0F)));
        list.add(new WrappedGoal(0, new RandomSwimmingGoal(this,1.2f,30)));
        list.add(new WrappedGoal(2, new RandomLookAroundGoal(this)));
        list.add(new WrappedGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F)));
        return list;
    }

    @Override
    public void die(DamageSource source) {
        if(!level.isClientSide && isTamed()){
            ItemStack stack = new ItemStack(ModRegistry.SIREN_CHARM.get());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
        }
        super.die(source);
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float p_70097_2_) {
        if(source == DamageSource.DROWN || source == DamageSource.IN_WALL || source == DamageSource.SWEET_BERRY_BUSH || source == DamageSource.CACTUS)
            return false;
        return super.hurt(source, p_70097_2_);
    }

    public boolean removeWhenFarAway(double dist) {
        return false;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if (this.isRemoved())
            return false;

        if (!level.isClientSide && isTamed()) {
            ItemStack stack = new ItemStack(ModRegistry.SIREN_CHARM.get());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
            ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
            this.remove(RemovalReason.DISCARDED);
        }
        return this.isTamed();
    }

    //start gecko stuff

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::idle));
        data.addAnimationController(new AnimationController<>(this,"actions",20,this::actions));
    }

    private <T extends IAnimatable> PlayState idle(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("floating"));
        return PlayState.CONTINUE;
    }

    private <T extends IAnimatable> PlayState actions(AnimationEvent<T> event){
        if (getDeltaMovement().length() > 0 || isInWater() || (level.isClientSide && PatchouliHandler.isPatchouliWorld())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim"));
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    //end gecko stuff

    public static AttributeSupplier createAttributes(){
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FLYING_SPEED, Attributes.FLYING_SPEED.getDefaultValue())
                .add(Attributes.MAX_HEALTH,10)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .build();
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TAMED, false);
    }

    public boolean isTamed(){
        return this.entityData.get(TAMED);
    }

    public void setTamed(boolean tamed){
        this.entityData.set(TAMED, tamed);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.storeBlockPos(tag, "home", homePos);
        tag.putBoolean("tamed", this.entityData.get(TAMED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(NBTUtil.hasBlockPos(tag, "home"))
            this.homePos = NBTUtil.getBlockPos(tag, "home");
        setTamed(tag.getBoolean("tamed"));
        if(!setBehaviors){
            tryResetGoals();
            setBehaviors = true;
        }
    }

    public void tryResetGoals(){
        this.goalSelector.removeAllGoals();
        this.addGoalsAfterConstructor();
    }

    //this is the hard part TODO

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, world);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanFloat(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }


    public void travel(Vec3 pTravelVector) {
        if (this.isInLava()) {
            this.moveRelative(0.02F, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        } else {
            BlockPos ground = new BlockPos(this.getX(), this.getY(), this.getZ());
            float f = 0.91F;

            if (this.onGround) {
                f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
            }

            float f1 = 0.16277137F / (f * f * f);
            this.moveRelative(0.1F * f1, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(f));
        }

        this.calculateEntityAnimation(this, false);
    }

}
