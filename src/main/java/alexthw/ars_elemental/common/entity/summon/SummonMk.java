package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

public class SummonMk extends Slime implements IFollowingSummon, ISummon {

    public LivingEntity owner;

    public SummonMk(EntityType<? extends Slime> p_33588_, Level p_33589_) {
        super(p_33588_, p_33589_);
    }

    public SummonMk(Level world) {
        this(ModEntities.SLIME_SUMMON.get(), world);
    }

    public SummonMk(SummonMk oldSummon, @Nullable Player summoner) {
        this(oldSummon.level);
        owner = oldSummon.owner;
        BlockPos position = oldSummon.blockPosition();
        setPos(position.getX(), position.getY(), position.getZ());
        ticksLeft = oldSummon.getTicksLeft();
        if (summoner != null) setOwnerID(summoner.getUUID());
        oldSummon.getActiveEffects().stream().filter(e -> e.getEffect().isBeneficial()).forEach(this::addEffect);
        tryResetGoals();
    }

    // A workaround for goals not registering correctly for a dynamic variable on reload as read() is called after constructor.
    public void tryResetGoals() {
        this.goalSelector.availableGoals = new LinkedHashSet<>();
        this.addGoalsAfterConstructor();
    }

    private void addGoalsAfterConstructor() {
        super.registerGoals();
        targetSelector.removeAllGoals();

        this.goalSelector.addGoal(2, new FollowSummonerGoal(this, owner, 1.0, 6.0f, 3.0f));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 10, false, true,
                entity -> (entity instanceof Mob mob && mob.getTarget() != null &&
                           mob.getTarget().equals(this.owner)) || entity != null && entity.getKillCredit() != null && entity.getKillCredit().equals(this.owner)
                                                                  && entity != this.owner
        ));
    }

    @Override
    public void registerGoals() {
    }

    @Override
    protected boolean isDealsDamage() {
        return true;
    }

    @Override
    public void push(Entity pEntity) {
        super.push(pEntity);
        if (pEntity instanceof LivingEntity entity && !pEntity.getUUID().equals(getOwnerID()))
            this.dealDamage(entity);
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!pEntity.getUUID().equals(getOwnerID())) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource instanceof EntityDamageSource eSource && eSource.getEntity() instanceof ISummon summon) {
            if (summon.getOwnerID() != null && summon.getOwnerID().equals(this.getOwnerID())) return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected int getExperienceReward(Player pPlayer) {
        return 0;
    }

    @Override
    public void setSize(int pSize, boolean pResetHealth) {
        super.setSize(pSize, pResetHealth);
        int i = Mth.clamp(pSize, 1, 127);

        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4F + 0.05F * (float) i);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2 * i);
    }

    protected int getJumpDelay() {
        return this.random.nextInt(10) + 10;
    }

    @Override
    public EntityType<? extends Slime> getType() {
        return ModEntities.SLIME_SUMMON.get();
    }

    @Override
    public void remove(RemovalReason pReason) {
        int i = this.getSize();
        if (this.level instanceof ServerLevel server && i > 1 && this.isDeadOrDying()) {
            Component component = this.getCustomName();
            boolean flag = this.isNoAi();
            float f = (float) i / 4.0F;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);

            for (int l = 0; l < k; ++l) {
                float f1 = ((float) (l % 2) - 0.5F) * f;
                float f2 = ((float) (l / 2) - 0.5F) * f;
                SummonMk slime = new SummonMk(this, (Player) this.getOwner(server));
                if (this.isPersistenceRequired()) {
                    slime.setPersistenceRequired();
                }

                slime.setCustomName(component);
                slime.setNoAi(flag);
                slime.setInvulnerable(this.isInvulnerable());
                slime.setSize(j, true);
                slime.moveTo(this.getX() + (double) f1, this.getY() + 0.5D, this.getZ() + (double) f2, this.random.nextFloat() * 360.0F, 0.0F);
                this.level.addFreshEntity(slime);
            }
        }


        this.setRemoved(pReason);
        if (pReason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_KILLED);
        }
        this.invalidateCaps();
    }

    public int ticksLeft;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SummonMk.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.of(Util.NIL_UUID));
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
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
            owner = level.getPlayerByUUID(uuid);
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

    @Override
    public UUID getOwnerID() {
        return this.getEntityData().get(OWNER_UUID).isEmpty() ? this.getUUID() : this.getEntityData().get(OWNER_UUID).get();
    }

    @Override
    public void setOwnerID(UUID uuid) {
        this.getEntityData().set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public Level getWorld() {
        return this.level;
    }

    @Override
    public PathNavigation getPathNav() {
        return this.navigation;
    }

    @Override
    public Mob getSelfEntity() {
        return this;
    }

    public LivingEntity getSummoner() {
        return this.level.getPlayerByUUID(this.getOwnerID());
    }

}
