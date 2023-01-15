package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.item.SpellPrismLens;
import alexthw.ars_elemental.common.items.caster_tools.SpellHorn;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.common.block.ArchfruitPod;
import com.hollingsworth.arsnouveau.common.block.StrippableLog;
import com.hollingsworth.arsnouveau.common.items.AnimBlockItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static alexthw.ars_elemental.datagen.Datagen.takeAll;

@SuppressWarnings("ALL")
public class AEItemModelProvider extends ItemModelProvider {
    public AEItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ArsElemental.MODID, existingFileHelper);
    }

    private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");

    private static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");
    private static final ResourceLocation SPAWN_EGG = new ResourceLocation("item/template_spawn_egg");

    @Override
    protected void registerModels() {
        Set<RegistryObject<Item>> items = new HashSet<>(ModItems.ITEMS.getEntries());

        takeAll(items, i -> i.get() instanceof AnimBlockItem).forEach(this::blockItem);
        takeAll(items, i -> i.get() instanceof SpellHorn);
        takeAll(items, i -> i.get() instanceof SpellPrismLens);
        takeAll(items, i -> i.get() instanceof BlockItem bi && bi.getBlock() instanceof ArchfruitPod).forEach(this::generatedItem);
        takeAll(items, i -> i.get() instanceof BlockItem bi && bi.getBlock() instanceof FenceBlock).forEach(this::fenceBlockItem);
        takeAll(items, i -> i.get() instanceof BlockItem bi && bi.getBlock() instanceof SaplingBlock).forEach(this::blockGeneratedItem);
        takeAll(items, i -> i.get() instanceof BlockItem).forEach(this::blockItem);
        takeAll(items, i -> i.get() instanceof DiggerItem).forEach(this::handheldItem);
        takeAll(items, i -> i.get() instanceof SpawnEggItem).forEach(this::spawnEgg);
        items.forEach(this::generatedItem);

    }

    private void spawnEgg(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        withExistingParent(name, SPAWN_EGG);
    }

    private void handheldItem(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        withExistingParent(name, HANDHELD).texture("layer0", ArsElemental.prefix("item/" + name));
    }

    private void generatedItem(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        withExistingParent(name, GENERATED).texture("layer0", ArsElemental.prefix("item/" + name));
    }

    private void focusModel(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        withExistingParent("item/focus/" + name, GENERATED).texture("layer0", ArsElemental.prefix("item/" + name));
    }

    private void blockGeneratedItem(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        withExistingParent(name, GENERATED).texture("layer0", ArsElemental.prefix("block/" + name));
    }

    private void blockItem(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        String root = "block/";
        if (i.get() instanceof BlockItem bi && (bi.getBlock() instanceof RotatedPillarBlock || bi.getBlock() instanceof LeavesBlock || bi.getBlock() instanceof StrippableLog))
            root = "block/archwood/";
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(ArsElemental.prefix(root + name)));
    }

    private void fenceBlockItem(RegistryObject<Item> i) {
        String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
        String baseName = name.substring(0, name.length() - 6);
        fenceInventory(name, ArsElemental.prefix("block/" + baseName));
    }

    @NotNull
    @Override
    public String getName() {
        return "Ars Elemental Item Models";
    }
}
