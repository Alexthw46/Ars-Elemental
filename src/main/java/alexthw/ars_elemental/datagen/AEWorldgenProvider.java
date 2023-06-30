package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.world.ModWorldgen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.world.ModWorldgen.COMMON_FLASHING_CONFIGURED;

public class AEWorldgenProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModWorldgen::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, ModWorldgen::bootstrapPlacedFeatures)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, AEWorldgenProvider::generateBiomeModifiers)
            .add(ForgeRegistries.Keys.BIOMES, ModWorldgen.Biomes::registerBiomes);

    public AEWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ArsElemental.MODID));
    }

    public static final ResourceKey<BiomeModifier> SIREN_SPAWN = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, prefix("siren_spawns"));
    public static final ResourceKey<BiomeModifier> COMMON_FLASHING_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ModWorldgen.COMMON_FLASHING_CONFIGURED.location());

    public static void generateBiomeModifiers(BootstapContext<BiomeModifier> context) {

        HolderSet<Biome> OVERWORLD_TAG = context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_OVERWORLD);
        HolderSet<Biome> COMMON_FLASH_ARCHWOOD_TAG = context.lookup(Registries.BIOME).getOrThrow(AETagsProvider.AEBiomeTagsProvider.FLASHING_TREE_COMMON_BIOME);
        HolderSet<Biome> SIREN_SPAWN_TAG = context.lookup(Registries.BIOME).getOrThrow(AETagsProvider.AEBiomeTagsProvider.SIREN_SPAWN_TAG);

        context.register(SIREN_SPAWN, ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(SIREN_SPAWN_TAG, new MobSpawnSettings.SpawnerData(ModEntities.SIREN_ENTITY.get(),
                3, 1, 3)));

        Holder.Reference<PlacedFeature> TREESET_CMN = context.lookup(Registries.PLACED_FEATURE).get(COMMON_FLASHING_CONFIGURED).get();

        context.register(COMMON_FLASHING_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(COMMON_FLASH_ARCHWOOD_TAG, HolderSet.direct(TREESET_CMN), GenerationStep.Decoration.VEGETAL_DECORATION));

    }


}
