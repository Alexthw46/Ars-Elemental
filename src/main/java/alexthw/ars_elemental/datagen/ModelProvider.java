package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashSet;
import java.util.Set;

import static alexthw.ars_elemental.ModRegistry.ITEMS;
import static alexthw.ars_elemental.datagen.Datagen.prefix;

public class ModelProvider extends ItemModelProvider {
    public ModelProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, ArsElemental.MODID, existingFileHelper);
    }

    private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");
    private static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");


    @Override
    protected void registerModels(){
        Set<RegistryObject<Item>> items = new HashSet<>(ITEMS.getEntries());
        items.forEach(this::generatedItem);
    }
    private void generatedItem(RegistryObject<Item> i) {
        String name = Registry.ITEM.getKey(i.get()).getPath();
        withExistingParent(name, GENERATED).texture("layer0", prefix("item/" + name));
    }
}
