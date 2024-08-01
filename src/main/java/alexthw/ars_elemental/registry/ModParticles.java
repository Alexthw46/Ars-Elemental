package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ArsElemental.MODID);

    public static final Supplier<SimpleParticleType> SPARK = PARTICLES.register("spark", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> VENOM = PARTICLES.register("venom", () -> new SimpleParticleType(false));
}
