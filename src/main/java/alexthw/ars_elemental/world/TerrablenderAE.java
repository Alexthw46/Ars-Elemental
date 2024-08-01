package alexthw.ars_elemental.world;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ConfigHandler;
import com.hollingsworth.arsnouveau.common.world.biome.ArchwoodRegion;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Regions;

import java.util.function.Consumer;

public class TerrablenderAE {
    public static void registerBiomes() {
        Regions.register(new ArchwoodRegion(ResourceLocation.fromNamespaceAndPath(ArsElemental.MODID, "overworld"), ConfigHandler.Common.EXTRA_BIOMES.get()){
            @Override
            public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
                this.addModifiedVanillaOverworldBiomes(mapper, (builder -> {
                    builder.replaceBiome(Biomes.STONY_PEAKS, ModWorldgen.Biomes.FLASHING_FOREST_KEY);
                    builder.replaceBiome(Biomes.SAVANNA, ModWorldgen.Biomes.BLAZING_FOREST_KEY);
                    builder.replaceBiome(Biomes.SWAMP, ModWorldgen.Biomes.CASCADING_FOREST_KEY);
                    builder.replaceBiome(Biomes.FLOWER_FOREST, ModWorldgen.Biomes.FLOURISHING_FOREST_KEY);
                }));
            }
        });
    }
}
