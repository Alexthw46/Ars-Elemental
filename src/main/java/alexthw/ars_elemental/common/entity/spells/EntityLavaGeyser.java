package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityLavaGeyser extends EntityGeyser {

    static final ParticleColor color = new ParticleColor(230, 50, 20);

    public EntityLavaGeyser(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityLavaGeyser(Level world, BlockPos pos, int duration, float height, float aoe, Direction dir) {
        super(ModEntities.FIRE_GEYSER.get(), world);
        this.level = world;
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); // Center in block
        this.maxDuration = duration;
        this.entityData.set(HEIGHT, height);
        this.entityData.set(AOE, 1 + aoe);
        this.entityData.set(FACING, dir.get3DDataValue());
    }

    @Override
    protected void applyEffect(LivingEntity le) {
        le.setRemainingFireTicks(le.getRemainingFireTicks() + 20);
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
                        ParticleTypes.FALLING_LAVA,
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
                    ParticleTypes.LARGE_SMOKE,
                    this.getX() + pos.x,
                    this.getY() + pos.y,
                    this.getZ() + pos.z,
                    0, 0, 0
            );
        }
    }

}
