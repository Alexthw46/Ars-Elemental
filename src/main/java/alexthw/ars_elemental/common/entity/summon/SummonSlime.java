package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.IFollowingSummon;
import com.hollingsworth.arsnouveau.common.entity.goal.FollowSummonerGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class SummonSlime extends Slime implements IFollowingSummon, ISummon {

    public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(SummonSlime.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SummonSlime.class, EntityDataSerializers.OPTIONAL_UUID);
    public LivingEntity owner;
    public int ticksLeft;

    public SummonSlime(EntityType<? extends Slime> p_33588_, Level p_33589_) {
        super(p_33588_, p_33589_);
    }

    public SummonSlime(Level world) {
        this(ModEntities.SLIME_SUMMON.get(), world);
    }

    public SummonSlime(SummonSlime oldSummon, @Nullable Player summoner) {
        this(oldSummon.level);
        owner = oldSummon.owner;
        entityData.set(VARIANT, oldSummon.getVariant());
        BlockPos position = oldSummon.blockPosition();
        setPos(position.getX(), position.getY(), position.getZ());
        ticksLeft = oldSummon.getTicksLeft();
        if (summoner != null) setOwnerID(summoner.getUUID());
        oldSummon.getActiveEffects().stream().filter(e -> e.getEffect().value().isBeneficial()).forEach(this::addEffect);
        tryResetGoals();
    }

    // A workaround for goals not registering correctly for a dynamic variable on reload as read() is called after constructor.
    public void tryResetGoals() {
        this.goalSelector.removeAllGoals((g) -> true);
        this.addGoalsAfterConstructor();
    }

    private void addGoalsAfterConstructor() {
        super.registerGoals();
        targetSelector.removeAllGoals((g) -> true);

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
    public PlayerTeam getTeam() {
        if (this.getSummoner() != null) return getSummoner().getTeam();
        return super.getTeam();
    }


    @Override
    public boolean isAlliedTo(@NotNull Entity pEntity) {
        LivingEntity summoner = this.getSummoner();

        if (summoner != null) {
            if (pEntity instanceof ISummon summon && summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID()))
                return true;
            return pEntity == summoner || summoner.isAlliedTo(pEntity);
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    protected boolean isDealsDamage() {
        return true;
    }

    @Override
    public void push(@NotNull Entity pEntity) {
        super.push(pEntity);
        if (pEntity instanceof LivingEntity entity && !pEntity.isAlliedTo(owner) && !pEntity.getUUID().equals(getOwnerUUID()) && !(pEntity instanceof ISummon sum && sum.getOwnerUUID() != null && sum.getOwnerUUID().equals(this.getOwnerUUID())))
            this.dealDamage(entity);
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!pEntity.getUUID().equals(getOwnerUUID())) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof ISummon summon) {
            if (summon.getOwnerUUID() != null && summon.getOwnerUUID().equals(this.getOwnerUUID())) return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected int getBaseExperienceReward() {
        return 0;
    }

    @Override
    public void setSize(int pSize, boolean pResetHealth) {
        super.setSize(pSize, pResetHealth);
        int i = Mth.clamp(pSize, 1, 127);
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (speed == null || attack == null) return;
        speed.setBaseValue(0.4F + 0.05F * (float) i);
        attack.setBaseValue(2 * i);
    }

    protected int getJumpDelay() {
        return this.random.nextInt(10) + 10;
    }

    @Override
    public @NotNull EntityType<? extends Slime> getType() {
        return ModEntities.SLIME_SUMMON.get();
    }

    @Override
    public void remove(@NotNull RemovalReason pReason) {
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
                SummonSlime slime = new SummonSlime(this, (Player) this.getOwner());
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
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(VARIANT, Variant.SUMMON.toString());
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
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        onSummonDeath(level, cause, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.ticksLeft = compound.getInt("left");
        UUID uuid = null;
        if (compound.hasUUID("owner")) {
            uuid = compound.getUUID("owner");
        }
        entityData.set(VARIANT, compound.getString("variant"));
        if (uuid != null) {
            this.setOwnerID(uuid);
            owner = level.getPlayerByUUID(uuid);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("left", ticksLeft);
        compound.putString("variant", getVariant());
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
    public @Nullable UUID getOwnerUUID() {
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
        return getOwnerFromID();
    }

    public LivingEntity getOwnerFromID() {
        try {
            UUID uuid = this.getOwnerUUID();

            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public String getVariant() {
        return entityData.get(VARIANT);
    }

    public void setVariant(Variant variant) {
        entityData.set(VARIANT, variant.toString());
    }

    public enum Variant {
        SUMMON,
        FIRE,
        WATER,
        EARTH,
        AIR,
        MANIPULATION,
        MK;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

}