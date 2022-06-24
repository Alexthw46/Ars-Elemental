package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.datagen.BlockTagProvider;
import com.hollingsworth.arsnouveau.common.datagen.ItemTagProvider;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class AETagsProvider {

    public static class AEItemTagsProvider extends ItemTagsProvider {
        public static final TagKey<Item> CURIO_SPELL_FOCUS = ItemTags.create(new ResourceLocation(CuriosApi.MODID, "an_focus"));
        public static final TagKey<Item> CURIO_BANGLE = ItemTags.create(new ResourceLocation(CuriosApi.MODID, "bangle"));

        public AEItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, blockTagsProvider, ArsElemental.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(CURIO_SPELL_FOCUS).add(ModItems.AIR_FOCUS.get(), ModItems.FIRE_FOCUS.get(), ModItems.EARTH_FOCUS.get(), ModItems.NECRO_FOCUS.get(), ModItems.WATER_FOCUS.get());
            tag(CURIO_BANGLE).add(ModItems.AIR_BANGLE.get(), ModItems.FIRE_BANGLE.get(), ModItems.EARTH_BANGLE.get(), ModItems.WATER_BANGLE.get(), ModItems.ENCHANTER_BANGLE.get());
            tag(ModRegistry.CURIO_BAGGABLE).add(ItemsRegistry.DOMINION_ROD.get(), ItemsRegistry.JAR_OF_LIGHT.get(), ItemsRegistry.VOID_JAR.get(), ItemsRegistry.RUNIC_CHALK.get(), ItemsRegistry.WARP_SCROLL.get(), ItemsRegistry.SPELL_PARCHMENT.get());
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

    public static class AEBlockTagsProvider extends BlockTagsProvider {
        final TagKey<Block> ARCHWOOD_LEAVES = BlockTags.create(new ResourceLocation("minecraft", "leaves/archwood_leaves"));

        public AEBlockTagsProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
            super(gen, ArsElemental.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            addPickMineable(1, ModItems.UPSTREAM_BLOCK.get());
            addPickMineable(0, ModItems.AIR_TURRET.get(), ModItems.FIRE_TURRET.get(), ModItems.EARTH_TURRET.get(), ModItems.WATER_TURRET.get());
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

    public static class AEBiomeTagsProvider extends BiomeTagsProvider {
        public AEBiomeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, ArsElemental.MODID, existingFileHelper);
        }

        public static final TagKey<Biome> SIREN_SPAWN_TAG = TagKey.create(Registry.BIOME_REGISTRY, prefix("siren_spawn"));

        @Override
        protected void addTags() {

            this.tag(SIREN_SPAWN_TAG).addTag(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL);

        }
    }

}
