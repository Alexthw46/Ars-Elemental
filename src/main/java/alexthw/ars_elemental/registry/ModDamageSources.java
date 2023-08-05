package alexthw.ars_elemental.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class ModDamageSources {

    static public DamageSource source(LevelAccessor level, ResourceKey<DamageType> key) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key));
    }

    static public DamageSource source(LevelAccessor level, ResourceKey<DamageType> key, @Nullable Entity entity) {
        return source(level, key, entity, null);
    }

    static public DamageSource source(LevelAccessor level, ResourceKey<DamageType> key, @Nullable Entity entity, @Nullable Entity direct) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        if (entity != null && direct != null)
            return new DamageSource(registry.getHolderOrThrow(key), entity, direct);
        else if (entity != null)
            return new DamageSource(registry.getHolderOrThrow(key), entity);
        else
            return new DamageSource(registry.getHolderOrThrow(key));
    }


}
