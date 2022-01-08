package alexthw.ars_elemental;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.FenceBlock;
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

    public static ResourceLocation prefix(String path){
        return new ResourceLocation(ArsElemental.MODID,path);
    }
        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator gen = event.getGenerator();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

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
                withExistingParent(name, HANDHELD).texture("layer0", prefix("item/" + name));
            }

            private void generatedItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                withExistingParent(name, GENERATED).texture("layer0", prefix("item/" + name));
            }

            private void blockGeneratedItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                withExistingParent(name, GENERATED).texture("layer0", prefix("block/" + name));
            }

            private void blockItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                getBuilder(name).parent(new ModelFile.UncheckedModelFile(prefix("block/" + name)));
            }

            private void fenceBlockItem(RegistryObject<Item> i) {
                String name = Registry.ITEM.getKey(i.get()).getPath();
                String baseName = name.substring(0, name.length() - 6);
                fenceInventory(name, prefix("block/" + baseName));
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
    }

}
