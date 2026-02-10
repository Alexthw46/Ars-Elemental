package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityLavaGeyser extends EntityGeyser {

    static final ParticleColor color = new ParticleColor(230, 50, 20);

    public EntityLavaGeyser(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityLavaGeyser(Level world, BlockPos pos, int duration, float height, float aoe) {
        super(ModEntities.FIRE_GEYSER.get(), world);
        this.level = world;
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); // Center in block
        this.maxDuration = duration;
        this.entityData.set(HEIGHT, height);
        this.entityData.set(AOE, 1 + aoe);
    }

    protected void pushEntitiesUp() {
        // Get all entities inside the bounding box
        List<Entity> targets = this.level().getEntities(this, this.getBoundingBox(), Entity::isAlive);

        for (Entity e : targets) {
            // Apply Upward Velocity
            Vec3 motion = e.getDeltaMovement();

            if (e instanceof LivingEntity le)
                le.setRemainingFireTicks(le.getRemainingFireTicks() + 20);
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

            // 3. Spawn Core Stream
            this.level().addParticle(GlowParticleData.createData(color, 0.5f, 0.75f, 15),
                    this.getX() + xOffset, this.getY() + yOffset, this.getZ() + zOffset,
                    0, 0.1, 0); // Slight upward drift

            // 4. Spawn Outer Splash (The "Wall" of the geyser)
            // If we want a defined "edge" to the geyser, spawn splashes specifically at the radius
            if (this.random.nextFloat() < 0.2f) { // 20% chance for edge particles
                double edgeX = radius * Math.cos(theta);
                double edgeZ = radius * Math.sin(theta);

                this.level().addParticle(ParticleTypes.FALLING_LAVA,
                        this.getX() + edgeX, this.getY() + yOffset, this.getZ() + edgeZ,
                        0, 0.05, 0);
            }
        }

        // 5. Optional: Surface Foam (Top of the geyser)
        // Spawns only at the very top to show where the water "breaks"
        for (int i = 0; i < radius * 5; i++) {
            double r = radius * Math.sqrt(this.random.nextDouble());
            double theta = this.random.nextDouble() * 2 * Math.PI;

            this.level().addParticle(ParticleTypes.LARGE_SMOKE,
                    this.getX() + (r * Math.cos(theta)),
                    this.getY() + height,
                    this.getZ() + (r * Math.sin(theta)),
                    0, 0, 0);
        }
    }

}
