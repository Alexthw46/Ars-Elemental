package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.world.ModWorldgen;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.ArsElemental.prefix;

public class AEBiomeModifiersProvider {

    public static void generateBiomeModifiers(GatherDataEvent event) {

        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
        Map<ResourceLocation, BiomeModifier> modifierMap = new HashMap<>();

        HolderSet.Named<Biome> OVERWORLD_TAG = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), BiomeTags.IS_OVERWORLD);
        HolderSet.Named<Biome> COMMON_ARCHWOOD_TAG = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), AETagsProvider.AEBiomeTagsProvider.FLASHING_TREE_COMMON_BIOME);
        HolderSet.Named<Biome> SIREN_SPAWN_TAG = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), AETagsProvider.AEBiomeTagsProvider.SIREN_SPAWN_TAG);

        modifierMap.put(prefix("siren_spawns"), ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(SIREN_SPAWN_TAG, new MobSpawnSettings.SpawnerData(ModEntities.SIREN_ENTITY.get(),
                3, 1, 3)));

        HolderSet<PlacedFeature> TREESET_CMN = new HolderSet.Named<>(ops.registry(Registry.PLACED_FEATURE_REGISTRY).orElseThrow(), TagKey.create(Registry.PLACED_FEATURE_REGISTRY, ModWorldgen.COMMON_FLASHING_CONFIGURED.getId()));

        modifierMap.put(ModWorldgen.COMMON_FLASHING_CONFIGURED.getId(), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(COMMON_ARCHWOOD_TAG, TREESET_CMN, GenerationStep.Decoration.VEGETAL_DECORATION));

        event.getGenerator().addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(event.getGenerator(), event.getExistingFileHelper(), MODID,
                ops, ForgeRegistries.Keys.BIOME_MODIFIERS, modifierMap));
    }


}
