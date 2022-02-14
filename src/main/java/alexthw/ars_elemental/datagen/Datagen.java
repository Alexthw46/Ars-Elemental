package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Predicate;

import static alexthw.ars_elemental.ModRegistry.ITEMS;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator gen = event.getGenerator();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
            gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

            gen.addProvider(new ANProviders.ImbuementProvider(gen));
            gen.addProvider(new ANProviders.GlyphProvider(gen));
            gen.addProvider(new ANProviders.EnchantingAppProvider(gen));

            gen.addProvider(new ANProviders.PatchouliProvider(gen));
        }

    public static <T> Collection<T> takeAll(Collection<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext())
        {
            T item = iter.next();
            if (pred.test(item))
            {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            return Collections.emptyList();
        }
        return ret;
    }


    public static class ModItemModelProvider extends ItemModelProvider {
        public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, ArsElemental.MODID, existingFileHelper);
        }

        private static final ResourceLocation GENERATED = new ResourceLocation("item/generated");
        private static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");
        private static final ResourceLocation SPAWN_EGG = new ResourceLocation("item/template_spawn_egg");

        @Override
            protected void registerModels() {
                Set<RegistryObject<Item>> items = new HashSet<>(ITEMS.getEntries());
                takeAll(items, i -> i.get() instanceof BlockItem && ((BlockItem) i.get()).getBlock() instanceof FenceBlock).forEach(this::fenceBlockItem);
                takeAll(items, i -> i.get() instanceof BlockItem).forEach(this::blockItem);
                takeAll(items, i -> i.get() instanceof DiggerItem).forEach(this::handheldItem);
                takeAll(items, i -> i.get() instanceof SpawnEggItem).forEach(this::spawnEgg);

                items.forEach(this::generatedItem);

            }

            private void spawnEgg(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                withExistingParent(name, SPAWN_EGG);
            }

            private void handheldItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                withExistingParent(name, HANDHELD).texture("layer0", ArsElemental.prefix("item/" + name));
            }

            private void generatedItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                withExistingParent(name, GENERATED).texture("layer0", ArsElemental.prefix("item/" + name));
            }

            private void blockGeneratedItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                withExistingParent(name, GENERATED).texture("layer0", ArsElemental.prefix("block/" + name));
            }

            private void blockItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                getBuilder(name).parent(new ModelFile.UncheckedModelFile(ArsElemental.prefix("block/" + name)));
            }

            private void fenceBlockItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                String baseName = name.substring(0, name.length() - 6);
                fenceInventory(name, ArsElemental.prefix("block/" + baseName));
            }

    }

    public static class ModBlockStateProvider extends BlockStateProvider{

        public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, ArsElemental.MODID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            Set<RegistryObject<Block>> blocks = new HashSet<>(ModRegistry.BLOCKS.getEntries());
            takeAll(blocks, b -> b.get() instanceof SlabBlock).forEach(this::slabBlock);
            takeAll(blocks, b -> b.get() instanceof StairBlock).forEach(this::stairsBlock);
            blocks.forEach(this::basicBlock);
        }

        public void slabBlock(RegistryObject<Block> blockRegistryObject) {
            String name = Registry.BLOCK.getKey(blockRegistryObject.get()).getPath();
            String baseName = name.substring(0, name.length() - 5);
            slabBlock((SlabBlock) blockRegistryObject.get(), ArsElemental.prefix(baseName), ArsElemental.prefix("block/" + baseName));
        }

        public void stairsBlock(RegistryObject<Block> blockRegistryObject) {
            String name = Registry.BLOCK.getKey(blockRegistryObject.get()).getPath();
            String baseName = name.substring(0, name.length() - 7);
            stairsBlock((StairBlock) blockRegistryObject.get(), ArsElemental.prefix("block/" + baseName));
        }

        private void basicBlock(RegistryObject<Block> blockRegistryObject) {
            simpleBlock(blockRegistryObject.get());
        }
    }


}
