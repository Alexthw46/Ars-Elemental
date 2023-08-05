package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.common.block.ArchfruitPod;
import com.hollingsworth.arsnouveau.common.block.SummonBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class AELootTables extends LootTableProvider {

    public AELootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn.getPackOutput(), new HashSet<>(), List.of(new SubProviderEntry(BlockLootTable::new, LootContextParamSets.BLOCK)));
    }

    private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

    public static class BlockLootTable extends BlockLootSubProvider {
        public List<Block> list = new ArrayList<>();

        protected BlockLootTable() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), new HashMap<>());
        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> p_249322_) {
            this.generate();
            Set<ResourceLocation> set = new HashSet<>();

            for (Block block : list) {
                if (block.isEnabled(this.enabledFeatures)) {
                    ResourceLocation resourcelocation = block.getLootTable();
                    if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                        LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                        if (loottable$builder == null) {
                            continue;
                        }

                        p_249322_.accept(resourcelocation, loottable$builder);
                    }
                }
            }
        }

        @Override
        protected void generate() {
            Set<RegistryObject<Block>> blocks = new HashSet<>(ModItems.BLOCKS.getEntries());
            Datagen.takeAll(blocks, b -> b.get() instanceof LeavesBlock);
            Datagen.takeAll(blocks, b -> !(b.get() instanceof SummonBlock)).forEach(b -> registerDropSelf(b.get()));
            registerLeavesAndSticks(ModItems.FLASHING_LEAVES.get(), ModItems.FLASHING_SAPLING.get());

            list.add(ModItems.MERMAID_ROCK.get());
            dropOther(ModItems.MERMAID_ROCK.get(), Blocks.PRISMARINE);

            list.add(ModItems.POT_FLASHING_SAPLING.get());
            dropPottedContents(ModItems.POT_FLASHING_SAPLING.get());

            this.add(ModItems.FLASHING_POD.get(), (block) -> LootTable.lootTable().withPool(POD_BUILDER(block.asItem(), block)));
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
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return list;
        }
    }

    public static LootPool.Builder POD_BUILDER(Item item, Block block) {
        return LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ArchfruitPod.AGE, 2)))));
    }

    public @NotNull String getOldName() {
        return "Ars Elemental Loot Tables";
    }

}
