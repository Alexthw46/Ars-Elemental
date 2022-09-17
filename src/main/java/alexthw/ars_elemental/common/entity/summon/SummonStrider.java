package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class SummonStrider extends Strider implements PlayerRideable, ISummon {
    public SummonStrider(EntityType<? extends Strider> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SummonStrider(Level pLevel) {
        super(ModEntities.STRIDER_SUMMON.get(), pLevel);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.STRIDER_SUMMON.get();
    }

    public SummonStrider(SummonHorse oldHorse, Player summoner) {
        this(summoner.level);
        BlockPos position = oldHorse.blockPosition();
        setPos(position.getX(), position.getY(), position.getZ());
        ticksLeft = oldHorse.getTicksLeft();
        this.equipSaddle(SoundSource.NEUTRAL);
        setOwnerID(summoner.getUUID());
        oldHorse.getActiveEffects().stream().filter(e -> e.getEffect().isBeneficial()).forEach(this::addEffect);
    }

    public LivingEntity getControllingPassenger() {

        Entity entity = this.getFirstPassenger();
        if (entity instanceof LivingEntity) {
            return (LivingEntity) entity;
        }

        return null;
    }

    @Override
    protected boolean canRide(Entity pEntity) {
        return pEntity instanceof Player;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
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
                float steer = livingentity.zza;
                if (steer <= 0.0F) {
                    steer *= 0.25F;
                }

                if (this.isControlledByLocalInstance()) {
                    this.setSpeed(getMoveSpeed());
                    super.travelWithInput(new Vec3(strafe, pTravelVector.y, steer));
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.tryCheckInsideBlocks();
            } else {
                super.travel(pTravelVector);
            }
        }

    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.level.isClientSide() && player.getMainHandItem().isEmpty() && !player.isShiftKeyDown()) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;

    }

    //ISummon

    public int ticksLeft;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SummonStrider.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    public int getExperienceReward() {
        return 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            ticksLeft--;
            if (ticksLeft <= 0) {
                ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
                this.remove(RemovalReason.DISCARDED);
                onSummonDeath(level, null, true);
            }
        }
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        onSummonDeath(level, cause, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
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
    public void addAdditionalSaveData(CompoundTag compound) {
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
    public UUID getOwnerID() {
        return this.getEntityData().get(OWNER_UUID).isEmpty() ? this.getUUID() : this.getEntityData().get(OWNER_UUID).get();
    }

    @Override
    public void setOwnerID(UUID uuid) {
        this.getEntityData().set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    //override
    public Strider getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    public boolean isFood(ItemStack pStack) {
        return false;
    }

    protected void dropEquipment() {
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        return false;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }
}
