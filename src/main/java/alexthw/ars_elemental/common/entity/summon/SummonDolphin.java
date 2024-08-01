package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SummonDolphin extends Dolphin implements PlayerRideableJumping, ISummon {

    public SummonDolphin(EntityType<? extends Dolphin> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonDolphin(Level world) {
        this(ModEntities.DOLPHIN_SUMMON.get(), world);
    }

    public SummonDolphin(SummonHorse oldHorse, Player summoner) {
        this(summoner.level());
        BlockPos position = oldHorse.blockPosition();
        setPos(position.getX(), position.getY(), position.getZ());
        ticksLeft = oldHorse.getTicksLeft();
        setOwnerID(summoner.getUUID());
        oldHorse.getActiveEffects().stream().filter(e -> e.getEffect().value().isBeneficial()).forEach(this::addEffect);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.DOLPHIN_SUMMON.get();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new DolphinJumpGoal(this, 10));
        this.goalSelector.addGoal(8, new FollowBoatGoal(this));
    }
    protected void positionRider(@NotNull Entity passenger, Entity.MoveFunction callback) {
        Vec3 vec3 = this.getPassengerRidingPosition(passenger);
        Vec3 vec31 = passenger.getVehicleAttachmentPoint(this);
        callback.accept(passenger, vec3.x - vec31.x, vec3.y - vec31.y, vec3.z - vec31.z);
            if (passenger instanceof Mob mob && this.getControllingPassenger() == passenger) {
                this.yBodyRot = mob.yBodyRot;
            }

    }

//    @Override
//    public Vec3 getPassengerRidingPosition(Entity entity) {
//        return new Vec3(0,0.45D,0);
//    }
//
//
//    @Override
//    public boolean dismountsUnderwater() {
//        return false;
//    }

    public LivingEntity getControllingPassenger() {

        Entity entity = this.getFirstPassenger();
        if (entity instanceof LivingEntity) {
            return (LivingEntity) entity;
        }

        return null;
    }



    @Override
    protected boolean canRide(@NotNull Entity pEntity) {
        return pEntity instanceof Player;
    }


    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {

        if (player.level().isClientSide()) return InteractionResult.PASS;

        if (player.getMainHandItem().isEmpty() && !player.isShiftKeyDown()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.isAlive()) {
            LivingEntity livingentity = this.getControllingPassenger();
            if (this.isVehicle() && livingentity != null) {
                this.setYRot(livingentity.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float strafe = livingentity.xxa * 0.5F;
                float forward = livingentity.zza * 2;
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }

                if (this.isControlledByLocalInstance()) {
                    if (getMoistnessLevel() > 2350) {
                        //this.flyingSpeed = 0.15F;
                    } else {
                        //this.flyingSpeed = 0.02F;
                        this.setSpeed((float) Math.min(this.getSpeed(), this.getAttributeValue(Attributes.MOVEMENT_SPEED) + 0.5F));
                    }
                    if (this.isInWater()) {
                        this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) + 2.5F);
                        float vert = (float) (pTravelVector.y - 0.4 * (0.2 + livingentity.getXRot()));
                        super.travel(new Vec3(strafe, vert + 0.5, forward));
                    } else {
                        super.travel(new Vec3(strafe, pTravelVector.y, forward));
                    }
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

            } else {
                //this.flyingSpeed = 0.02F;
                super.travel(pTravelVector);
            }
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.975F;
    }

    @Override
    protected void customServerAiStep() {
        if (isVehicle() && getControllingPassenger() instanceof Player player) {
            player.setAirSupply(player.getMaxAirSupply());
        }
    }

    //ISummon

    public int ticksLeft;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SummonDolphin.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    public int getBaseExperienceReward() {
        return 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            ticksLeft--;
            if (ticksLeft <= 0) {
                ParticleUtil.spawnPoof((ServerLevel) level(), blockPosition());
                this.remove(RemovalReason.DISCARDED);
                onSummonDeath(level(), null, true);
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        onSummonDeath(level(), cause, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.ticksLeft = compound.getInt("left");
        UUID uuid = null;
        if (compound.hasUUID("owner")) {
            uuid = compound.getUUID("owner");
        }
        if (uuid != null) {
            this.setOwnerID(uuid);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("left", ticksLeft);
        writeOwner(compound);
    }

    @Override
    public int getTicksLeft() {
        return ticksLeft;
    }

    @Override
    public void setTicksLeft(int ticks) {
        this.ticksLeft = ticks;
    }


    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.getEntityData().get(OWNER_UUID).isEmpty() ? this.getUUID() : this.getEntityData().get(OWNER_UUID).get();
    }

    @Override
    public void setOwnerID(UUID uuid) {
        this.getEntityData().set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public void onPlayerJump(int pJumpPower) {
    }

    @Override
    public boolean canJump() {
        return this.isInWater() || getMoistnessLevel() > 2300;
    }

    @Override
    public void handleStartJump(int pJumpPower) {
        if (pJumpPower < 50) return;
        Direction direction = this.getMotionDirection();
        if (getControllingPassenger() != null && getControllingPassenger().getXRot() > 20) {
            this.setDeltaMovement(this.getDeltaMovement().add(direction.getStepX(), -1.5, direction.getStepZ()).scale(0.7F * pJumpPower / 10F));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().add(direction.getStepX(), 1.5, direction.getStepZ()).scale(0.7F * pJumpPower / 10F));
        }
    }

    @Override
    public void handleStopJump() {

    }

}
