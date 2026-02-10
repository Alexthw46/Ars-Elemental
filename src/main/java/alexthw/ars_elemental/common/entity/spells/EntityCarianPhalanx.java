package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.common.glyphs.MethodCarianPhalanx;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.event.SpellProjectileHitEvent;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.setup.registry.DataSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class EntityCarianPhalanx extends EntityProjectileSpell {

    // Data accessors
    public static final EntityDataAccessor<Vec3> LAST_POS = SynchedEntityData.defineId(EntityCarianPhalanx.class, DataSerializers.VEC.get());
    public static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.defineId(EntityCarianPhalanx.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ACCELERATES = SynchedEntityData.defineId(EntityCarianPhalanx.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityCarianPhalanx.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> IS_LAUNCHED = SynchedEntityData.defineId(EntityCarianPhalanx.class, EntityDataSerializers.BOOLEAN);
    // Formation constants
    private static final double FORMATION_DISTANCE = 0.2; // Distance behind player
    private static final double FORMATION_HEIGHT = 0.8;   // Height above player's head
    private static final double FORMATION_SPREAD = 1.2;   // Horizontal spread
    private static final double FORMATION_ARC_DROP = 0.8; // How much sides drop (increased)
    private final int detectionRange = 12; // Range to detect enemies
    public int extendTimes;
    private State state = State.ORBITING;
    private LivingEntity target;
    private List<Predicate<LivingEntity>> ignore;

    public EntityCarianPhalanx(EntityType<? extends EntityCarianPhalanx> entityType, Level world) {
        super(entityType, world);
    }

    public EntityCarianPhalanx(Level worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.PHALANX_PROJ.get();
    }

    @Override
    public void attemptRemoval() {
        super.attemptRemoval();
    }

    public int getIndex() {
        return entityData.get(INDEX);
    }

    // Setters and getters
    public void setIndex(int index) {
        entityData.set(INDEX, index);
    }

    public int getAccelerates() {
        return entityData.get(ACCELERATES);
    }

    public void setAccelerates(int accelerates) {
        entityData.set(ACCELERATES, accelerates);
    }

    public float getAoe() {
        return entityData.get(AOE);
    }

    public void setAoe(float aoe) {
        entityData.set(AOE, aoe);
    }

    public List<Predicate<LivingEntity>> getIgnored() {
        return this.ignore;
    }

    public void setIgnored(List<Predicate<LivingEntity>> ignore) {
        this.ignore = ignore;
    }

    public boolean isLaunched() {
        return entityData.get(IS_LAUNCHED);
    }

    private void setLaunched(boolean launched) {
        entityData.set(IS_LAUNCHED, launched);
        if (launched) {
            state = State.LAUNCHED;
        }
    }

    @Override
    public void tick() {
        super.tick();

        // If owner is gone, fade out
        if (getOwner() == null || getOwner().isRemoved()) {
            attemptRemoval();
            return;
        }

        // Only check for targets while orbiting
        if (state == State.ORBITING && tickCount % 20 == getIndex() * 2) {
            // update rotation
            this.setYRot(-getOwner().getYRot());
            this.setXRot(-20.0f); // Slight downward
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
            if (level.isClientSide()) return;
            checkForTargets();
            // If the age is more than half the max and is still orbiting, launch it anyway
            if (this.age > this.getExpirationTime() / 2 && target == null) {
                setLaunched(true);
                var shooter = getOwner();
                shoot(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 0.5f, 0.8f);
                MethodCarianPhalanx.onPhalanxLaunched(this);
            }
        }

    }

    private void checkForTargets() {
        if (getOwner() == null) return;

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(detectionRange),
                this::shouldTarget
        );

        if (!entities.isEmpty()) {
            // Find the closest target
            target = entities.stream()
                    .min(Comparator.comparingDouble(e -> e.distanceToSqr(this)))
                    .orElse(null);

            if (target != null) {
                launchAtTarget();
            }
        }
    }

    private void launchAtTarget() {
        setLaunched(true);
        // Set initial velocity towards target
        Vec3 direction = target.position().subtract(this.position()).normalize();
        this.setDeltaMovement(direction.scale(0.5));

        // Unregister from the tracking map since we're no longer orbiting
        MethodCarianPhalanx.onPhalanxLaunched(this);
    }

    @Override
    public Vec3 getNextHitPosition() {
        if (state == State.LAUNCHED) {
            // Use parent's default ray tracing for launched state
            return super.getNextHitPosition();
        } else {
            if (level().isClientSide()) return this.position();
            // Trace ahead for formation position (small amount for collision detection)
            return getFormationPosition();
        }
    }

    @Override
    public @NotNull Vec3 getLookAngle() {
        return super.getLookAngle();
    }

    @Override
    public void tickNextPosition() {
        if (level.isClientSide()) return;
        if (!this.isRemoved()) {
            if (state == State.ORBITING) {
                // Move to formation position with smooth interpolation
                Vec3 targetPos = getFormationPosition();
                Vec3 currentPos = this.position();

                // Smooth movement towards target position
                double lerpSpeed = 0.5; // How quickly it follows (0.0 to 1.0)
                Vec3 newPos = currentPos.lerp(targetPos, lerpSpeed);

                this.setPos(newPos.x, newPos.y, newPos.z);
            } else if (state == State.LAUNCHED) {
                // Home in on target
                if (target != null && target.isAlive() && target.distanceToSqr(this) < 50 * 50) {
                    homeTo(target.blockPosition());
                } else {
                    // Target lost, continue in straight line
                    super.tickNextPosition();
                }
            }
        }
    }

    /**
     * Calculates the formation position for this projectile based on its index
     * Creates an arc formation behind and above the player
     */
    private Vec3 getFormationPosition() {
        Entity owner = getOwner();

        if (owner == null || owner.isRemoved()) {
            // Fallback to last known position
            return entityData.get(LAST_POS);
        }

        int index = getIndex();
        int total = MethodCarianPhalanx.PLAYER_PHALANX_MAP.getOrDefault(owner.getUUID(), List.of()).size();

        // Get the player's position and looking direction
        Vec3 ownerPos = owner.position().add(0, owner.getEyeHeight(), 0);
        float yaw = owner.getYRot();

        // Calculate horizontal offset based on index
        // Center the formation by offsetting based on total count
        double horizontalOffset = 0;
        double normalizedIndex = 0; // -1 to 1, where 0 is center

        if (total > 1) {
            // Spread projectiles evenly across the horizontal
            // For example: 3 projectiles -> -1, 0, 1
            normalizedIndex = (index - (total - 1) / 2.0) / ((total - 1) / 2.0);
            horizontalOffset = normalizedIndex * FORMATION_SPREAD;
        }

        // Calculate vertical offset - create a parabolic arc
        // Center is highest, sides drop down in a curve
        double verticalOffset = FORMATION_HEIGHT;
        if (total > 1) {
            // Use quadratic function for more pronounced arc
            // normalizedIndex ranges from -1 to 1, squaring it gives 0 (center) to 1 (edges)
            double arcCurve = normalizedIndex * normalizedIndex;
            verticalOffset = FORMATION_HEIGHT - (arcCurve * FORMATION_ARC_DROP);
        }

        // Add slight variation with time for floating effect
        double bobAmount = 0.08;
        double bobSpeed = 0.04;
        double bob = Math.sin((tickCount + index * 10) * bobSpeed) * bobAmount;

        // Apply AOE multiplier to spread (and slightly to distance)
        double aoeMultiplier = 1.0 + (getAoe() * 0.3);
        horizontalOffset *= aoeMultiplier;
        double distanceMultiplier = 1.0 + (getAoe() * 0.1);

        // Calculate position behind and above player
        // Convert yaw to radians for the calculation
        double yawRad = Math.toRadians(yaw);

        // Behind the player
        double backwardX = -Math.sin(yawRad) * FORMATION_DISTANCE * distanceMultiplier;
        double backwardZ = Math.cos(yawRad) * FORMATION_DISTANCE * distanceMultiplier;

        // To the side (perpendicular to looking direction)
        double sideX = Math.cos(yawRad) * horizontalOffset;
        double sideZ = Math.sin(yawRad) * horizontalOffset;

        // Combine all offsets
        Vec3 formationPos = new Vec3(
                ownerPos.x + backwardX + sideX,
                ownerPos.y + verticalOffset + bob,
                ownerPos.z + backwardZ + sideZ
        );

        // Store for fallback
        entityData.set(LAST_POS, formationPos);

        return formationPos;
    }

    private void homeTo(BlockPos dest) {
        double posX = getX();
        double posY = getY();
        double posZ = getZ();
        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (dest.getX() != 0 || dest.getY() != 0 || dest.getZ() != 0) {
            double targetX = dest.getX() + 0.5;
            double targetY = dest.getY() + 0.75;
            double targetZ = dest.getZ() + 0.5;
            Vec3 targetVector = new Vec3(targetX - posX, targetY - posY, targetZ - posZ);
            double length = targetVector.length();
            targetVector = targetVector.scale(0.3 / length);
            double weight = 0;
            if (length <= 3) {
                weight = (3.0 - length) * 0.3;
            }

            motionX = (0.9 - weight) * motionX + (0.1 + weight) * targetVector.x;
            motionY = (0.9 - weight) * motionY + (0.1 + weight) * targetVector.y;
            motionZ = (0.9 - weight) * motionZ + (0.1 + weight) * targetVector.z;
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        this.setPos(posX, posY, posZ);
        this.setDeltaMovement(motionX, motionY, motionZ);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        boolean b = super.canHitEntity(entity);
        if (entity instanceof LivingEntity) {
            b &= shouldTarget((LivingEntity) entity);
        }
        return b;
    }

    private boolean shouldTarget(LivingEntity e) {
        // Don't target owner
        if (e.equals(getOwner())) return false;
        // Check ignore predicates
        if (ignore != null) {
            for (Predicate<LivingEntity> p : getIgnored()) {
                if (p.test(e)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.level.getEntity(this.entityData.get(OWNER_ID));
    }

    @Override
    public boolean canTraversePortals() {
        return false;
    }

    @Override
    public int getExpirationTime() {
        return 60 * 20 + 30 * 20 * extendTimes;
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        if (level.isClientSide || result.getType() == HitResult.Type.MISS)
            return;

        SpellProjectileHitEvent event = new SpellProjectileHitEvent(this, result);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }

        if (result instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getEntity().equals(this.getOwner())) {
                return;
            }
            if (this.resolver() != null) {
                this.resolver().onResolveEffect(level, result);
            }

            sendResolveParticles();
            attemptRemoval();
        } else if (numSensitive > 0 && result instanceof BlockHitResult blockraytraceresult && !this.isRemoved()) {
            if (this.resolver() != null) {
                this.resolver().onResolveEffect(this.level, blockraytraceresult);
            }

            sendResolveParticles();
            attemptRemoval();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(INDEX, 0);
        pBuilder.define(ACCELERATES, 0);
        pBuilder.define(AOE, 0f);
        pBuilder.define(LAST_POS, Vec3.ZERO);
        pBuilder.define(IS_LAUNCHED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("index", getIndex());
        tag.putFloat("aoe", getAoe());
        tag.putInt("accelerate", getAccelerates());
        tag.putDouble("lastX", entityData.get(LAST_POS).x);
        tag.putDouble("lastY", entityData.get(LAST_POS).y);
        tag.putDouble("lastZ", entityData.get(LAST_POS).z);
        tag.putBoolean("launched", isLaunched());
        tag.putInt("extendTimes", extendTimes);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setIndex(tag.getInt("index"));
        setAoe(tag.getFloat("aoe"));
        setAccelerates(tag.getInt("accelerate"));
        entityData.set(LAST_POS, new Vec3(tag.getDouble("lastX"), tag.getDouble("lastY"), tag.getDouble("lastZ")));
        setLaunched(tag.getBoolean("launched"));
        extendTimes = tag.getInt("extendTimes");
    }

    // State tracking
    private enum State {
        ORBITING,
        LAUNCHED
    }
}
