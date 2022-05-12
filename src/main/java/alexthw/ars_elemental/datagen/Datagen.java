package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.blocks.StrippableLog;
import alexthw.ars_elemental.common.blocks.UpstreamBlock;
import alexthw.ars_elemental.common.items.ISchoolItem;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.google.common.collect.ImmutableList;
import com.hollingsworth.arsnouveau.common.block.SummonBlock;
import com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
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
import software.bernie.geckolib3.core.IAnimatable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        gen.addProvider(BTP);
        gen.addProvider(new ModItemTagsProvider(gen, BTP, existingFileHelper));

        gen.addProvider(new ModRecipeProvider(gen));
        gen.addProvider(new ModLootTables(gen));

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
            Set<RegistryObject<Item>> items = new HashSet<>(ModItems.ITEMS.getEntries());

            takeAll(items, i -> i.get() instanceof IAnimatable);
            takeAll(items, i -> i.get() instanceof BlockItem bi && bi.getBlock() instanceof FenceBlock).forEach(this::fenceBlockItem);
            takeAll(items, i -> i.get() instanceof BlockItem bi && bi.getBlock() instanceof SaplingBlock).forEach(this::blockGeneratedItem);
            takeAll(items, i -> i.get() instanceof BlockItem).forEach(this::blockItem);
            takeAll(items, i -> i.get() instanceof DiggerItem).forEach(this::handheldItem);
            takeAll(items, i -> i.get() instanceof SpawnEggItem).forEach(this::spawnEgg);
            takeAll(items, i -> i.get() instanceof ISchoolItem).forEach(this::focusModel);
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

    public static class ModBlockStateProvider extends BlockStateProvider{

        public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, ArsElemental.MODID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            Set<RegistryObject<Block>> blocks = new HashSet<>(ModItems.BLOCKS.getEntries());
            takeAll(blocks, b -> b.get() instanceof UpstreamBlock || b.get() instanceof SummonBlock);
            takeAll(blocks, b -> b.get() instanceof RotatedPillarBlock || b.get() instanceof StrippableLog).forEach(this::logBlock);
            takeAll(blocks, b -> b.get() instanceof SlabBlock).forEach(this::slabBlock);
            takeAll(blocks, b -> b.get() instanceof StairBlock).forEach(this::stairsBlock);
            takeAll(blocks, b -> b.get() instanceof LeavesBlock);
            takeAll(blocks, b -> b.get() instanceof SaplingBlock);
            blocks.forEach(this::basicBlock);
        }

        public void slabBlock(RegistryObject<Block> blockRegistryObject) {
            String name = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath();
            String baseName = name.substring(0, name.length() - 5);
            slabBlock((SlabBlock) blockRegistryObject.get(), ArsElemental.prefix(baseName), ArsElemental.prefix("block/" + baseName));
        }

        public void logBlock(RegistryObject<Block> blockRegistryObject) {

        }

        public void stairsBlock(RegistryObject<Block> blockRegistryObject) {
            String name = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath();
            String baseName = name.substring(0, name.length() - 7);
            stairsBlock((StairBlock) blockRegistryObject.get(), ArsElemental.prefix("block/" + baseName));
        }

        public void basicBlock(RegistryObject<Block> blockRegistryObject) {
            simpleBlock(blockRegistryObject.get());
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
            tag(ModRegistry.CURIO_BAGGABLE).add(ItemsRegistry.AMETHYST_GOLEM_CHARM, ItemsRegistry.BOOKWYRM_CHARM, ItemsRegistry.DRYGMY_CHARM, ItemsRegistry.WIXIE_CHARM, ItemsRegistry.STARBUNCLE_CHARM, ItemsRegistry.WHIRLISPRIG_CHARM,
                    ModItems.SIREN_CHARM.get(), ModItems.FIRENANDO_CHARM.get(), ItemsRegistry.DOMINION_ROD, ItemsRegistry.JAR_OF_LIGHT, ItemsRegistry.VOID_JAR, ItemsRegistry.RUNIC_CHALK, ItemsRegistry.WARP_SCROLL, ItemsRegistry.SPELL_PARCHMENT);
            this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
            this.copy(BlockTags.LEAVES, ItemTags.LEAVES);
            this.copy(BlockTags.LOGS, ItemTags.LOGS);
            this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
            tag(ItemTagProvider.ARCHWOOD_LOG_TAG).add(
                    ModItems.FLASHING_ARCHWOOD_LOG.get().asItem(),
                    ModItems.FLASHING_ARCHWOOD.get().asItem(),
                    ModItems.FLASHING_ARCHWOOD_LOG_STRIPPED.get().asItem(),
                    ModItems.FLASHING_ARCHWOOD_STRIPPED.get().asItem()
            );

        }

        @Override
        public @NotNull String getName() {
            return "Ars Elemental Item Tags";
        }
    }

    public static class ModBlockTagsProvider extends BlockTagsProvider {
        TagKey<Block> ARCHWOOD_LEAVES = BlockTags.create(new ResourceLocation("minecraft", "leaves/archwood_leaves"));

        public ModBlockTagsProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, ArsElemental.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            addPickMineable(1, ModItems.UPSTREAM_BLOCK.get());
            logsTag(ModItems.FLASHING_ARCHWOOD_LOG.get(),
                    ModItems.FLASHING_ARCHWOOD.get(),
                    ModItems.FLASHING_ARCHWOOD_STRIPPED.get(),
                    ModItems.FLASHING_ARCHWOOD_LOG_STRIPPED.get()
            );
            tag(BlockTags.LEAVES).add(ModItems.FLASHING_LEAVES.get());
            tag(ARCHWOOD_LEAVES).add(ModItems.FLASHING_LEAVES.get());
            tag(BlockTags.SAPLINGS).add(ModItems.FLASHING_SAPLING.get());
            tag(BlockTagProvider.MAGIC_SAPLINGS).add(ModItems.FLASHING_SAPLING.get());

        }

        void logsTag(Block... blocks) {
            tag(BlockTags.LOGS).add(blocks);
            tag(BlockTags.LOGS_THAT_BURN).add(blocks);
            tag(BlockTags.MINEABLE_WITH_AXE).add(blocks);
        }

        void addPickMineable(int level, Block... blocks) {
            for (Block block : blocks) {
                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                switch (level) {
                    case (1) -> tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    case (2) -> tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    case (3) -> tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block);
                }
            }

        }

        @Override
        public @NotNull String getName() {
            return "Ars Elemental Block Tags";
        }
    }

    public static class ModLootTables extends LootTableProvider {

        public ModLootTables(DataGenerator dataGeneratorIn) {
            super(dataGeneratorIn);
        }

        private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

        public static class BlockLootTable extends BlockLoot {
            public List<Block> list = new ArrayList<>();

            @Override
            protected void addTables() {
                Set<RegistryObject<Block>> blocks = new HashSet<>(ModItems.BLOCKS.getEntries());
                takeAll(blocks, b -> b.get() instanceof LeavesBlock);
                takeAll(blocks, b -> !(b.get() instanceof SummonBlock)).forEach(b -> registerDropSelf(b.get()));
                registerLeavesAndSticks(ModItems.FLASHING_LEAVES.get(), ModItems.FLASHING_SAPLING.get());
            }

            public void registerLeavesAndSticks(Block leaves, Block sapling) {
                list.add(leaves);
                this.add(leaves, l_state -> createLeavesDrops(l_state, sapling, DEFAULT_SAPLING_DROP_RATES));
            }

            public void registerDropSelf(Block block) {
                list.add(block);
                dropSelf(block);
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return list;
            }
        }

        private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(
                Pair.of(BlockLootTable::new, LootContextParamSets.BLOCK)
        );

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return tables;
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
            map.forEach((resourceLocation, lootTable) -> LootTables.validate(validationTracker, resourceLocation, lootTable));
        }

        @Override
        public @NotNull String getName() {
            return "Ars Elemental Loot Tables";
        }

    }

}
