package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityLingeringSpell;
import com.hollingsworth.arsnouveau.common.entity.EntityWallSpell;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityGeyser extends Entity {
    // Sync the height so clients know how high to draw particles
    protected static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityGeyser.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityGeyser.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> FACING = SynchedEntityData.defineId(EntityGeyser.class, EntityDataSerializers.INT);

    static final ParticleColor color = new ParticleColor(20, 50, 230);
    protected int maxDuration = 100;
    protected int age = 0;

    public EntityGeyser(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    protected final double push = 0.4;
    Vec3 dir, right, forward = null;

    public EntityGeyser(Level world, BlockPos pos, int duration, float height, float aoe, Direction dir) {
        super(ModEntities.GEYSER.get(), world);
        this.level = world;
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); // Center in block
        this.maxDuration = duration;
        this.entityData.set(HEIGHT, height);
        this.entityData.set(AOE, 1 + aoe);
        this.entityData.set(FACING, dir.get3DDataValue());
    }

    public Direction getFacing() {
        return Direction.from3DDataValue(this.entityData.get(FACING));
    }

    public Vec3 getDirectionVec() {
        return Vec3.atLowerCornerOf(getFacing().getNormal());
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            spawnParticles();
        } else {
            // Server Logic: Push Entities
            if (getFacing() == Direction.UP)
                pushEntitiesUp();
            else pushEntities();

            // Age and Die
            age++;
            if (age >= maxDuration) {
                this.discard();
            }
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        if (getFacing() != Direction.UP) return super.getDimensions(pose);
        return EntityDimensions.scalable((this.entityData.get(AOE) * 1.5f), this.entityData.get(HEIGHT));
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (HEIGHT.equals(key) || AOE.equals(key)) {
            this.refreshDimensions(); // This forces a call to getDimensions() + setBoundingBox()
            recalcVecs();
        }
        super.onSyncedDataUpdated(key);
    }

    protected void recalcVecs() {
        dir = getDirectionVec();

        // Build an orthonormal basis around the direction
        right = dir.cross(new Vec3(0, 1, 0));
        if (right.lengthSqr() < 0.01)
            right = new Vec3(1, 0, 0);

        right = right.normalize();
        forward = dir.cross(right).normalize();
    }

    protected void pushEntitiesUp() {
        // Get all entities inside the bounding box
        List<Entity> targets = this.level().getEntities(this, this.getBoundingBox(), Entity::isAlive);

        for (Entity e : targets) {
            // Apply Upward Velocity
            Vec3 motion = e.getDeltaMovement();

            if (e instanceof LivingEntity le)
                applyEffect(le);
            // If they are at the very top, hold them there (bobbing effect)
            // If they are below the top, launch them up
            if (e.getY() < this.getY() + this.entityData.get(HEIGHT)) {
                if (e instanceof EntityLingeringSpell || e instanceof EntityWallSpell)
                    // force move it on the top of the geyser, since delta movement doesn't work
                    e.setPos(e.getX(), this.getY() + this.entityData.get(HEIGHT) + 0.1, e.getZ());
                // 0.4 is roughly bubble column speed
                e.setDeltaMovement(motion.x, push, motion.z);
                e.hasImpulse = true;
                e.hurtMarked = true;
                e.fallDistance = 0; // Prevent fall damage while riding
            }
        }
    }

    protected void applyEffect(LivingEntity le) {
        le.addEffect(new MobEffectInstance(ModPotions.SOAKED_EFFECT, 40, 0));
    }

    protected void pushEntities() {

        Vec3 dir = getDirectionVec();
        double length = this.entityData.get(HEIGHT);

        AABB box = this.getBoundingBox().expandTowards(dir.scale(length));

        List<Entity> targets = this.level().getEntities(this, box, Entity::isAlive);

        Direction d = getFacing();

        for (Entity e : targets) {
            // Apply Upward Velocity
            Vec3 motion = e.getDeltaMovement();

            if (e instanceof LivingEntity le)
                applyEffect(le);
            // If they are at the very top, hold them there (bobbing effect)
            // If they are below the top, launch them up
            double delta = switch (d.getAxis()) {
                case X -> e.getX() - this.getX();
                case Y -> e.getY() - this.getY();
                case Z -> e.getZ() - this.getZ();
            };

            // invert sign for negative directions
            if (d.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
                delta = -delta;

            if (delta < length) {
                switch (d) {
                    case UP -> e.setDeltaMovement(motion.x, push, motion.z);
                    case DOWN -> e.setDeltaMovement(motion.x, -push, motion.z);
                    case NORTH -> e.setDeltaMovement(motion.x, motion.y, -push);
                    case SOUTH -> e.setDeltaMovement(motion.x, motion.y, push);
                    case EAST -> e.setDeltaMovement(push, motion.y, motion.z);
                    case WEST -> e.setDeltaMovement(-push, motion.y, motion.z);
                }
                e.hasImpulse = true;
                e.hurtMarked = true;
                e.fallDistance = 0; // Reset fall damage while on it
            }
        }
    }

    protected void spawnParticles() {

        float length = this.entityData.get(HEIGHT);
        float radius = this.entityData.get(AOE);
        if (dir == null || right == null || forward == null) recalcVecs();

        int particleCount = (int) (length * radius * 10);

        for (int i = 0; i < particleCount; i++) {

            // distance along geyser axis
            double dist = this.random.nextDouble() * length;

            // circular distribution
            double r = radius * Math.sqrt(this.random.nextDouble());
            double theta = this.random.nextDouble() * Math.PI * 2;

            Vec3 radial =
                    right.scale(Math.cos(theta) * r)
                            .add(forward.scale(Math.sin(theta) * r));

            Vec3 offset = dir.scale(dist).add(radial);

            // === CORE WATER BODY ===
            this.level().addParticle(
                    GlowParticleData.createData(color, 0.5f, 0.75f, 15),
                    this.getX() + offset.x,
                    this.getY() + offset.y,
                    this.getZ() + offset.z,
                    dir.x * 0.1,
                    dir.y * 0.1,
                    dir.z * 0.1
            );

            // === EDGE SPLASH WALL ===
            if (this.random.nextFloat() < 0.2f) {

                Vec3 edge =
                        dir.scale(dist)
                                .add(right.scale(Math.cos(theta) * radius))
                                .add(forward.scale(Math.sin(theta) * radius));

                this.level().addParticle(
                        ParticleTypes.SPLASH,
                        this.getX() + edge.x,
                        this.getY() + edge.y,
                        this.getZ() + edge.z,
                        dir.x * 0.05,
                        dir.y * 0.05,
                        dir.z * 0.05
                );
            }
        }

        // === SURFACE FOAM ===
        Vec3 end = dir.scale(length);

        for (int i = 0; i < radius * 5; i++) {

            double r = radius * Math.sqrt(this.random.nextDouble());
            double theta = this.random.nextDouble() * Math.PI * 2;

            Vec3 radial =
                    right.scale(Math.cos(theta) * r)
                            .add(forward.scale(Math.sin(theta) * r));

            Vec3 pos = end.add(radial);

            this.level().addParticle(
                    ParticleTypes.CLOUD,
                    this.getX() + pos.x,
                    this.getY() + pos.y,
                    this.getZ() + pos.z,
                    0, 0, 0
            );
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEIGHT, 3.0f);
        builder.define(AOE, 1.0f);
        builder.define(FACING, Direction.UP.get3DDataValue());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.age = tag.getInt("Age");
        this.maxDuration = tag.getInt("MaxDuration");
        this.entityData.set(HEIGHT, tag.getFloat("Height"));
        this.entityData.set(AOE, tag.getFloat("AOE"));
        this.entityData.set(FACING, tag.getInt("Facing"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", age);
        tag.putInt("MaxDuration", maxDuration);
        tag.putFloat("Height", this.entityData.get(HEIGHT));
        tag.putFloat("AOE", this.entityData.get(AOE));
        tag.putInt("Facing", this.entityData.get(FACING));
    }

}
