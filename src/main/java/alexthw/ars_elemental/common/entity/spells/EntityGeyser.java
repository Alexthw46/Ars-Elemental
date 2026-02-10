package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityGeyser extends Entity {
    // Sync the height so clients know how high to draw particles
    protected static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(EntityGeyser.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityGeyser.class, EntityDataSerializers.FLOAT);
    static final ParticleColor color = new ParticleColor(20, 50, 230);
    protected int maxDuration = 100;
    protected int age = 0;

    public EntityGeyser(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityGeyser(Level world, BlockPos pos, int duration, float height, float aoe) {
        super(ModEntities.GEYSER.get(), world);
        this.level = world;
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); // Center in block
        this.maxDuration = duration;
        this.entityData.set(HEIGHT, height);
        this.entityData.set(AOE, 1 + aoe);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            spawnParticles();
        } else {
            // Server Logic: Push Entities
            pushEntitiesUp();

            // Age and Die
            age++;
            if (age >= maxDuration) {
                this.discard();
            }
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable((this.entityData.get(AOE) * 1.5f), this.entityData.get(HEIGHT));
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (HEIGHT.equals(key) || AOE.equals(key)) {
            this.refreshDimensions(); // This forces a call to getDimensions() + setBoundingBox()
        }
        super.onSyncedDataUpdated(key);
    }

    protected void pushEntitiesUp() {
        // Get all entities inside the bounding box
        List<Entity> targets = this.level().getEntities(this, this.getBoundingBox(), Entity::isAlive);

        for (Entity e : targets) {
            // Apply Upward Velocity
            Vec3 motion = e.getDeltaMovement();

            if (e instanceof LivingEntity le)
                le.addEffect(new MobEffectInstance(ModPotions.SOAKED_EFFECT, 40, 0, false, false));
            // If they are at the very top, hold them there (bobbing effect)
            // If they are below the top, launch them up
            if (e.getY() < this.getY() + this.entityData.get(HEIGHT)) {
                // 0.4 is roughly bubble column speed
                e.setDeltaMovement(motion.x, 0.4, motion.z);
                e.hasImpulse = true;
                e.hurtMarked = true;
                e.fallDistance = 0; // Prevent fall damage while riding
            }
        }
    }

    protected void spawnParticles() {
        float height = this.entityData.get(HEIGHT);
        float radius = this.entityData.get(AOE);

        // Scale particle count based on volume so it doesn't look empty when big
        // Volume = pi * r^2 * h. We use a simplified multiplier.
        int particleCount = (int) (height * radius * 10);

        for (int i = 0; i < particleCount; i++) {
            // 1. Pick a random height
            double yOffset = this.random.nextDouble() * height;

            // 2. Pick a random point within the circle (Uniform distribution)
            // r = radius * sqrt(random) ensures particles aren't Clumped in the center
            double r = radius * Math.sqrt(this.random.nextDouble()) - 0.5;
            double theta = this.random.nextDouble() * 2 * Math.PI;

            double xOffset = r * Math.cos(theta);
            double zOffset = r * Math.sin(theta);

            // 3. Spawn Core Bubbles (The "Water" body)
            this.level().addParticle(GlowParticleData.createData(color, 0.5f, 0.75f, 15),
                    this.getX() + xOffset, this.getY() + yOffset, this.getZ() + zOffset,
                    0, 0.1, 0); // Slight upward drift

            // 4. Spawn Outer Splash (The "Wall" of the geyser)
            // If we want a defined "edge" to the geyser, spawn splashes specifically at the radius
            if (this.random.nextFloat() < 0.2f) { // 20% chance for edge particles
                double edgeX = radius * Math.cos(theta);
                double edgeZ = radius * Math.sin(theta);

                this.level().addParticle(ParticleTypes.SPLASH,
                        this.getX() + edgeX, this.getY() + yOffset, this.getZ() + edgeZ,
                        0, 0.05, 0);
            }
        }

        // 5. Optional: Surface Foam (Top of the geyser)
        // Spawns only at the very top to show where the water "breaks"
        for (int i = 0; i < radius * 5; i++) {
            double r = radius * Math.sqrt(this.random.nextDouble());
            double theta = this.random.nextDouble() * 2 * Math.PI;

            this.level().addParticle(ParticleTypes.CLOUD,
                    this.getX() + (r * Math.cos(theta)),
                    this.getY() + height,
                    this.getZ() + (r * Math.sin(theta)),
                    0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(HEIGHT, 3.0f);
        builder.define(AOE, 1.0f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.age = tag.getInt("Age");
        this.maxDuration = tag.getInt("MaxDuration");
        this.entityData.set(HEIGHT, tag.getFloat("Height"));
        this.entityData.set(AOE, tag.getFloat("AOE"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", age);
        tag.putInt("MaxDuration", maxDuration);
        tag.putFloat("Height", this.entityData.get(HEIGHT));
        tag.putFloat("AOE", this.entityData.get(AOE));
    }
}
