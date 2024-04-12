package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ArsElemental.MODID);

    public static final RegistryObject<SimpleParticleType> SPARK = PARTICLES.register("spark", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> VENOM = PARTICLES.register("venom", () -> new SimpleParticleType(false));
}
