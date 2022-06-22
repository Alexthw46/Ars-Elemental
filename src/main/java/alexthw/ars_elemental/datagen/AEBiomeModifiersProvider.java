package alexthw.ars_elemental.datagen;

import com.google.gson.JsonElement;
import com.hollingsworth.arsnouveau.common.datagen.BiomeTagProvider;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

import static alexthw.ars_elemental.ArsElemental.MODID;

public class AEBiomeModifiersProvider {

    public static void generateBiomeModifiers(GatherDataEvent event) {

        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
        Map<ResourceLocation, BiomeModifier> modifierMap = new HashMap<>();

        //TODO add trees and sirens to spawn
        HolderSet.Named<Biome> ARCHWOOD_TAG = new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).orElseThrow(), BiomeTagProvider.RARE_ARCHWOOD_BIOME_TAG);

        event.getGenerator().addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(event.getGenerator(), event.getExistingFileHelper(), MODID,
                ops, ForgeRegistries.Keys.BIOME_MODIFIERS, modifierMap));
    }


}
