package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class CompostablesProvider extends DataMapProvider {

    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    protected CompostablesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(ModItems.FLASHING_SAPLING.getId(), new Compostable(.3F),false)
                .add(ModItems.FLASHING_POD.getId(),  new Compostable(0.3F),false)
                .add(ModItems.FLASHING_LEAVES.getId(),  new Compostable(0.3F),false)
                .build();
    }
}