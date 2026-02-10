package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityMistCloud extends Entity {

    static final ParticleColor color = new ParticleColor(20, 50, 230);
    private static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityMistCloud.class, EntityDataSerializers.FLOAT);
    private int maxDuration = 100;
    private int age = 0;

    public EntityMistCloud(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public EntityMistCloud(Level world, BlockPos pos, int duration, float aoe) {
        super(ModEntities.MIST_CLOUD.get(), world);
        this.level = world;
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5); // Center in block
        this.maxDuration = duration;
        this.entityData.set(AOE, 1 + aoe);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            spawnParticles();
        } else {
            applyMist();

            // Age and Die
            age++;
            if (age >= maxDuration) {
                this.discard();
            }
        }
    }

    private void spawnParticles() {
        float radius = this.entityData.get(AOE);

        // Scale particle count based on size (roughly 2 particles per block of radius)
        // Cap particles to prevent lag on huge AOEs
        int particleCount = (int) Math.min(60, radius * radius);

        ParticleColor color = new ParticleColor(200, 200, 230);
        for (int i = 0; i < particleCount; i++) {
            // Random point within the bounding box
            double offsetX = (this.random.nextDouble() - 0.5) * 2 * radius;
            double offsetZ = (this.random.nextDouble() - 0.5) * 2 * radius;
            double offsetY = (this.random.nextDouble() - 0.5) * 2 * radius;

            // Optional: Only spawn if within the circular radius (trims the square corners)
            if ((offsetX * offsetX) + (offsetY * offsetY) + (offsetZ * offsetZ) <= (radius * radius)) {
                this.level().addParticle(GlowParticleData.createData(color, 0.35f, 0.35f, 80),
                        this.getX() + offsetX,
                        this.getY() + offsetY + 0.5,
                        this.getZ() + offsetZ,
                        0, -0.01, 0); // Zero velocity so it hangs in the air
            }
        }
    }

    private void applyMist() {
        if (this.age % 20 == 0) return;
        // Apply the mist status effect (3 sec duration) to all entities in the radius every second
        float radius = this.entityData.get(AOE);

        // Create a search box centered on the entity
        AABB searchArea = this.getBoundingBox().inflate(radius, radius, radius);

        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, searchArea);

        for (LivingEntity target : targets) {
            // Check distance to ensure circular shape (otherwise it's a square box)
            if (target.distanceToSqr(this) <= (radius * radius)) {
                // if the entity didn't have the status reset its aggro
                if (target instanceof Mob mob && !target.hasEffect(ModPotions.MIST))
                    mob.setTarget(null);
                target.addEffect(new MobEffectInstance(ModPotions.MIST, 35, 0, false, false));
            }
        }
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(AOE, 1.0f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.age = tag.getInt("Age");
        this.maxDuration = tag.getInt("MaxDuration");
        this.entityData.set(AOE, tag.getFloat("AOE"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", age);
        tag.putInt("MaxDuration", maxDuration);
        tag.putFloat("AOE", this.entityData.get(AOE));
    }

}
