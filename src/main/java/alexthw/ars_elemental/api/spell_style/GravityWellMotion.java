package alexthw.ars_elemental.api.spell_style;

import alexthw.ars_elemental.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleOptions;
import com.hollingsworth.arsnouveau.api.particle.configurations.IParticleMotionType;
import com.hollingsworth.arsnouveau.api.particle.configurations.ParticleMotion;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.*;
import com.hollingsworth.arsnouveau.api.registry.ParticlePropertyRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GravityWellMotion extends ParticleMotion {
    public static MapCodec<GravityWellMotion> CODEC = buildPropCodec(GravityWellMotion::new);

    public static StreamCodec<RegistryFriendlyByteBuf, GravityWellMotion> STREAM = buildStreamCodec(GravityWellMotion::new);

    public GravityWellMotion(PropMap propertyMap) {
        super(propertyMap);
    }

    public GravityWellMotion() {
        this(new PropMap());
    }

    @Override
    public void tick(PropertyParticleOptions particleOptions, Level level, double x, double y, double z,
                     double prevX, double prevY, double prevZ) {
        ParticleDensityProperty density = getDensity(particleOptions, 20, 0.0f);
        WallProperty wallProperty = particleOptions.map.getOrDefault(ParticlePropertyRegistry.WALL_PROPERTY.get(), new WallProperty(5, 5, 20, Direction.NORTH));
        RandomSource rand = level.random;

        // Center vector for spawning and inward target
        Vec3 centerVec = new Vec3(x + 0.5, y + 1.0, z + 0.5);

        double radius = wallProperty.range;
        SpawnType spawnType = density.spawnType().orElse(SpawnType.SPHERE);

        for (int i = 0; i < density.density(); i++) {
            if (rand.nextInt(wallProperty.chance) != 0) continue;

            // Spawn position randomly within sphere or cube volume
            Vec3 spawnPos = getMotionScaled(centerVec, radius, spawnType);

            // Calculate vector from spawnPos to center (particles move inward)
            Vec3 toCenter = centerVec.subtract(spawnPos).scale(0.05); // velocity magnitude tweakable

            level.addAlwaysVisibleParticle(
                    particleOptions,
                    true,
                    spawnPos.x, spawnPos.y, spawnPos.z,
                    toCenter.x, toCenter.y, toCenter.z
            );
        }
    }

    @Override
    public IParticleMotionType<?> getType() {
        return ArsNouveauRegistry.GRAVITY_FIELD_TYPE.get(); // Register a new type if not done yet
    }

    @Override
    public List<BaseProperty<?>> getProperties(PropMap propMap) {
        return List.of(
                propMap.createIfMissing(new ParticleTypeProperty()),
                propMap.createIfMissing(new ParticleDensityProperty()
                        .minDensity(20)
                        .maxDensity(100)
                        .densityStepSize(10)
                        .supportsShapes(false))
        );
    }
}
