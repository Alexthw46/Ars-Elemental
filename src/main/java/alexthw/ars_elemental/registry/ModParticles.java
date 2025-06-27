package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ArsElemental;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ArsElemental.MODID);

    public static final Supplier<SimpleParticleType> SPARK = PARTICLES.register("spark", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> VENOM = PARTICLES.register("venom", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, PropertyParticleType> SPARK_2 = PARTICLES.register("spark_spell", PropertyParticleType::new);

    public static final DeferredHolder<ParticleType<?>, PropertyParticleType> VENOM_2 = PARTICLES.register("venom_spell", PropertyParticleType::new);
}
