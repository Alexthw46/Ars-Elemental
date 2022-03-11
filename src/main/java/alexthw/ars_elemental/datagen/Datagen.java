package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.block.SummonBlock;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

import static alexthw.ars_elemental.ModRegistry.BLOCKS;
import static alexthw.ars_elemental.ModRegistry.ITEMS;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator gen = event.getGenerator();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
            gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
            BlockTagsProvider BTP = new ModBlockTagsProvider(gen, existingFileHelper);
            gen.addProvider(new ModItemTagsProvider(gen, BTP, existingFileHelper));

            gen.addProvider(new ANProviders.ImbuementProvider(gen));
            gen.addProvider(new ANProviders.GlyphProvider(gen));
            gen.addProvider(new ANProviders.EnchantingAppProvider(gen));

            gen.addProvider(new ANProviders.PatchouliProvider(gen));
        }

    public static <T> Collection<T> takeAll(Collection<T> src, Predicate<T> predicate) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext())
        {
            T item = iter.next();
            if (predicate.test(item))
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

            private void blockGeneratedItem(RegistryObject<Item> i) {
                String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
                withExistingParent(name, GENERATED).texture("layer0", ArsElemental.prefix("block/" + name));
            }

            private void blockItem(RegistryObject<Item> i) {
                String name = ForgeRegistries.ITEMS.getKey(i.get()).getPath();
                getBuilder(name).parent(new ModelFile.UncheckedModelFile(ArsElemental.prefix("block/" + name)));
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

    public static class ModBlockStateProvider extends BlockStateProvider{

        public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, ArsElemental.MODID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            Set<RegistryObject<Block>> blocks = new HashSet<>(ModRegistry.BLOCKS.getEntries());
            takeAll(blocks, b-> b.get() instanceof RotatedPillarBlock).forEach(this::topSideBlock);
            takeAll(blocks, b -> b.get() instanceof SlabBlock).forEach(this::slabBlock);
            takeAll(blocks, b -> b.get() instanceof StairBlock).forEach(this::stairsBlock);
            takeAll(blocks, b -> b.get() instanceof SummonBlock);
            blocks.forEach(this::basicBlock);
        }

        public void slabBlock(RegistryObject<Block> blockRegistryObject) {
            String name = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath();
            String baseName = name.substring(0, name.length() - 5);
            slabBlock((SlabBlock) blockRegistryObject.get(), ArsElemental.prefix(baseName), ArsElemental.prefix("block/" + baseName));
        }

        public void stairsBlock(RegistryObject<Block> blockRegistryObject) {
            String name = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath();
            String baseName = name.substring(0, name.length() - 7);
            stairsBlock((StairBlock) blockRegistryObject.get(), ArsElemental.prefix("block/" + baseName));
        }

        public void basicBlock(RegistryObject<Block> blockRegistryObject) {
            simpleBlock(blockRegistryObject.get());
        }

        public void topSideBlock(RegistryObject<Block> blockRegistryObject){
            logBlock((RotatedPillarBlock) blockRegistryObject.get());
        }

        @NotNull
        @Override
        public String getName() {
            return "Ars Elemental BlockStates";
        }

    }

    public static class ModItemTagsProvider extends ItemTagsProvider{

        public ModItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, blockTagsProvider, ArsElemental.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(ModRegistry.CURIO_BAGGABLE).add(ItemsRegistry.AMETHYST_GOLEM_CHARM,ItemsRegistry.BOOKWYRM_CHARM, ItemsRegistry.DRYGMY_CHARM, ItemsRegistry.WIXIE_CHARM, ItemsRegistry.STARBUNCLE_CHARM, ItemsRegistry.WHIRLISPRIG_CHARM,
                    ModRegistry.SIREN_CHARM.get(), ItemsRegistry.DOMINION_ROD, ItemsRegistry.JAR_OF_LIGHT, ItemsRegistry.VOID_JAR, ItemsRegistry.RUNIC_CHALK, ItemsRegistry.WARP_SCROLL, ItemsRegistry.SPELL_PARCHMENT);
        }

        @Override
        public String getName() {
            return "Ars Elemental Item Tags";
        }
    }

    public static class ModBlockTagsProvider extends BlockTagsProvider{

        public ModBlockTagsProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, ArsElemental.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {

        }

        @Override
        public String getName() {
            return "Ars Elemental Block Tags";
        }
    }

}
