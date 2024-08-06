package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class AECurioProvider extends CuriosDataProvider {
    public AECurioProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(ArsElemental.MODID, output, fileHelper, registries);
    }


    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createEntities("ae_curios").addPlayer().addSlots(
                "bundle", "bracelet"
        );
        this.createSlot("bracelet").size(2).icon(ResourceLocation.fromNamespaceAndPath("curios","slot/bangle_slot"));
        this.createSlot("an_focus").icon(ResourceLocation.fromNamespaceAndPath("curios","slot/an_focus_slot"));
        this.createSlot("bundle").size(1);

    }

}
