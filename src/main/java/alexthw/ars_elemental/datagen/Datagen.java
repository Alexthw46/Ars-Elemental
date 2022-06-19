package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModItems;
import com.google.common.collect.ImmutableList;
import com.hollingsworth.arsnouveau.common.block.SummonBlock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(true, new AEBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(true, new AEItemModelProvider(gen, existingFileHelper));
        BlockTagsProvider BTP = new AETagsProvider.BlockTagsProvider(gen, existingFileHelper);
        gen.addProvider(true, BTP);
        gen.addProvider(true, new AETagsProvider.ItemTagsProvider(gen, BTP, existingFileHelper));

        gen.addProvider(true, new ModRecipeProvider(gen));
        gen.addProvider(true, new ModLootTables(gen));

        gen.addProvider(true, new ANProviders.ImbuementProvider(gen));
        gen.addProvider(true, new ANProviders.GlyphProvider(gen));
        gen.addProvider(true, new AEApparatusProvider(gen));

        //TODO restore gen.addProvider(new AEPatchouliProvider(gen));
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
