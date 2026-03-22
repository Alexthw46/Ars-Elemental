package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.client.PrismRenderer;
import alexthw.ars_elemental.common.blocks.EverfullUrnBlock;
import alexthw.ars_elemental.common.blocks.SporeBlossomGround;
import alexthw.ars_elemental.common.blocks.mermaid_block.MermaidRock;
import alexthw.ars_elemental.common.blocks.prism.*;
import alexthw.ars_elemental.common.blocks.relays.AirRelay;
import alexthw.ars_elemental.common.blocks.relays.EarthRelay;
import alexthw.ars_elemental.common.blocks.relays.FireRelay;
import alexthw.ars_elemental.common.blocks.relays.WaterRelay;
import alexthw.ars_elemental.common.blocks.upstream.AirUpstreamTile;
import alexthw.ars_elemental.common.blocks.upstream.MagmaUpstreamTile;
import alexthw.ars_elemental.common.blocks.upstream.UpstreamBlock;
import alexthw.ars_elemental.common.blocks.upstream.WaterUpstreamTile;
import alexthw.ars_elemental.common.items.CasterHolder;
import alexthw.ars_elemental.common.items.CurioHolder;
import alexthw.ars_elemental.common.items.Debugger;
import alexthw.ars_elemental.common.items.FirenandoCharm;
import alexthw.ars_elemental.common.items.FlashjackCharm;
import alexthw.ars_elemental.common.items.SirenCharm;
import alexthw.ars_elemental.common.items.armor.ArmorSet;
import alexthw.ars_elemental.common.items.bangles.*;
import alexthw.ars_elemental.common.items.caster_tools.SpellHorn;
import alexthw.ars_elemental.common.items.foci.ElementalFocus;
import alexthw.ars_elemental.common.items.foci.GreaterElementalFocus;
import alexthw.ars_elemental.common.items.foci.NecroticFocus;
import alexthw.ars_elemental.datagen.AETagsProvider;
import alexthw.ars_elemental.world.ModWorldgen;
import com.alexthw.sauce.client.FocusTurretRenderer;
import com.alexthw.sauce.common.block.FocusEnhancedSpellTurret;
import com.alexthw.sauce.common.item.SchoolCasterTome;
import com.alexthw.sauce.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.client.renderer.item.GenericItemBlockRenderer;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.hollingsworth.arsnouveau.common.block.ArchfruitPod;
import com.hollingsworth.arsnouveau.common.block.MagicLeaves;
import com.hollingsworth.arsnouveau.common.block.StrippableLog;
import com.hollingsworth.arsnouveau.common.items.ModItem;
import com.hollingsworth.arsnouveau.common.items.RendererBlockItem;
import com.hollingsworth.arsnouveau.common.items.data.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.common.world.tree.MagicTree;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.registry.ModPotions.LIGHTNING_LURE;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.LOG_PROP;
import static com.hollingsworth.arsnouveau.setup.registry.BlockRegistry.SAP_PROP;
import static net.minecraft.world.level.block.CaveVines.BERRIES;

@SuppressWarnings("SameParameterValue")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredHolder<Block, ? extends Block> MERMAID_ROCK;
    public static final DeferredHolder<Block, ? extends Block> WATER_URN;
    public static final DeferredHolder<Block, ? extends Block> FLASHING_ARCHWOOD_LOG;
    public static final DeferredHolder<Block, ? extends Block> FLASHING_ARCHWOOD_LOG_STRIPPED;
    public static final DeferredHolder<Block, ? extends Block> FLASHING_ARCHWOOD_STRIPPED;
    public static final DeferredHolder<Block, ? extends Block> FLASHING_ARCHWOOD;
    public static final DeferredHolder<Block, ? extends Block> FLASHING_SAPLING;
    public static final DeferredHolder<Block, ? extends Block> FLASHING_LEAVES;
    public static final DeferredHolder<Block, ArchfruitPod> FLASHING_POD;
    public static final DeferredHolder<Block, FlowerPotBlock> POT_FLASHING_SAPLING;
    public static final DeferredHolder<Block, ? extends Block> SPARKFLOWER;
    public static final DeferredHolder<Block, FlowerPotBlock> POT_SPARKFLOWER;

    public static final DeferredHolder<Block, ? extends Block> GROUND_BLOSSOM;
    public static final DeferredHolder<Block, CaveVinesBlock> SOURCE_VINES;
    public static final DeferredHolder<Block, CaveVinesPlantBlock> SOURCE_VINES_PLANT;

    public static final DeferredHolder<Block, ? extends Block> WATER_UPSTREAM_BLOCK;
    public static final DeferredHolder<Block, ? extends Block> LAVA_UPSTREAM_BLOCK;
    public static final DeferredHolder<Block, ? extends Block> AIR_UPSTREAM_BLOCK;

    public static final DeferredHolder<Block, ? extends Block> FIRE_RELAY;
    public static final DeferredHolder<Block, ? extends Block> WATER_RELAY;
    public static final DeferredHolder<Block, ? extends Block> AIR_RELAY;
    public static final DeferredHolder<Block, ? extends Block> EARTH_RELAY;

    public static final DeferredHolder<Block, ? extends Block> FIRE_TURRET;
    public static final DeferredHolder<Block, ? extends Block> WATER_TURRET;
    public static final DeferredHolder<Block, ? extends Block> AIR_TURRET;
    public static final DeferredHolder<Block, ? extends Block> EARTH_TURRET;
    public static final DeferredHolder<Block, ? extends Block> SHAPING_TURRET;

    public static final DeferredHolder<Block, AdvancedPrism> ADVANCED_PRISM;
    public static final DeferredHolder<Block, ? extends Block> SPELL_MIRROR;

    public static final DeferredHolder<Item, GreaterElementalFocus> FIRE_FOCUS;
    public static final DeferredHolder<Item, GreaterElementalFocus> AIR_FOCUS;
    public static final DeferredHolder<Item, GreaterElementalFocus> WATER_FOCUS;
    public static final DeferredHolder<Item, GreaterElementalFocus> EARTH_FOCUS;
    public static final DeferredHolder<Item, NecroticFocus> NECRO_FOCUS;

    public static final DeferredHolder<Item, ElementalFocus> LESSER_FIRE_FOCUS;
    public static final DeferredHolder<Item, ElementalFocus> LESSER_AIR_FOCUS;
    public static final DeferredHolder<Item, ElementalFocus> LESSER_WATER_FOCUS;
    public static final DeferredHolder<Item, ElementalFocus> LESSER_EARTH_FOCUS;

    public static final ArmorSet FIRE_ARMOR = new ArmorSet.Medium("fire", SpellSchools.ELEMENTAL_FIRE);
    public static final ArmorSet AIR_ARMOR = new ArmorSet.Medium("air", SpellSchools.ELEMENTAL_AIR);
    public static final ArmorSet EARTH_ARMOR = new ArmorSet.Medium("earth", SpellSchools.ELEMENTAL_EARTH);
    public static final ArmorSet WATER_ARMOR = new ArmorSet.Medium("aqua", SpellSchools.ELEMENTAL_WATER);

    public static final ArmorSet FIRE_ARMOR_L;
    public static final ArmorSet AIR_ARMOR_L;
    public static final ArmorSet EARTH_ARMOR_L;
    public static final ArmorSet WATER_ARMOR_L;

    public static final ArmorSet FIRE_ARMOR_H;
    public static final ArmorSet AIR_ARMOR_H;
    public static final ArmorSet EARTH_ARMOR_H;
    public static final ArmorSet WATER_ARMOR_H;

    public static final DeferredHolder<Item, SchoolCasterTome> FIRE_CTOME;
    public static final DeferredHolder<Item, SchoolCasterTome> AIR_CTOME;
    public static final DeferredHolder<Item, SchoolCasterTome> WATER_CTOME;
    public static final DeferredHolder<Item, SchoolCasterTome> EARTH_CTOME;
    public static final DeferredHolder<Item, SchoolCasterTome> NECRO_CTOME;
    public static final DeferredHolder<Item, SchoolCasterTome> SHAPERS_CTOME;

    public static final DeferredHolder<Item, BaseBangle> ENCHANTER_BANGLE;
    public static final DeferredHolder<Item, FireBangles> FIRE_BANGLE;
    public static final DeferredHolder<Item, WaterBangles> WATER_BANGLE;
    public static final DeferredHolder<Item, AirBangles> AIR_BANGLE;
    public static final DeferredHolder<Item, EarthBangles> EARTH_BANGLE;
    public static final DeferredHolder<Item, SummonBangles> SUMMON_BANGLE;
    public static final DeferredHolder<Item, AnimaBangles> ANIMA_BANGLE;

    public static final DeferredHolder<Item, CurioHolder> CURIO_BAG;
    public static final DeferredHolder<Item, CasterHolder> CASTER_BAG;

    public static final DeferredHolder<Item, Debugger> DEBUG_ICON;
    public static final DeferredItem<Item> MARK_OF_MASTERY;

    public static final DeferredHolder<Item, HomingPrismLens> HOMING_LENS;
    public static final DeferredHolder<Item, ArcPrismLens> ARC_LENS;
    public static final DeferredHolder<Item, AccelerationPrismLens> ACC_LENS;
    public static final DeferredHolder<Item, DecelerationPrismLens> DEC_LENS;
    public static final DeferredHolder<Item, PiercingPrismLens> PIERCE_LENS;
    @SuppressWarnings("removal")
    public static final DeferredHolder<Item, RainbowPrismLens> RGB_LENS = ITEMS.register("rainbow_prism_lens", () -> new RainbowPrismLens(itemProps()));
    public static final DeferredHolder<Item, ChainingPrismLens> CHAIN_LENS;

    public static final DeferredHolder<Item, ? extends Item> ANIMA_ESSENCE = ModRegistry.ANIMA_ESSENCE;

    public static final DeferredHolder<Item, ModItem> SIREN_SHARDS;
    public static final DeferredHolder<Item, SirenCharm> SIREN_CHARM;
    public static final DeferredHolder<Item, FirenandoCharm> FIRENANDO_CHARM;
    public static final DeferredHolder<Item, ModItem> FLASHJACK_SHARDS;
    public static final DeferredHolder<Item, FlashjackCharm> FLASHJACK_CHARM;

    public static final DeferredHolder<Item, SpellHorn> SPELL_HORN;
    public static FoodProperties FLASHPINE_FOOD = new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 30 * 20), .4f)
            .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 30 * 20), .4f)
            .effect(() -> new MobEffectInstance(ModPotions.SHOCKED_EFFECT, 30 * 20, 0), .8f)
            .effect(() -> new MobEffectInstance(LIGHTNING_LURE, 30 * 20, 0), .2f)
            .alwaysEdible().build();

    static {

        WATER_ARMOR_H = new ArmorSet.Heavy("aqua", SpellSchools.ELEMENTAL_WATER);
        EARTH_ARMOR_H = new ArmorSet.Heavy("earth", SpellSchools.ELEMENTAL_EARTH);
        AIR_ARMOR_H = new ArmorSet.Heavy("air", SpellSchools.ELEMENTAL_AIR);
        WATER_ARMOR_L = new ArmorSet.Light("aqua", SpellSchools.ELEMENTAL_WATER);
        EARTH_ARMOR_L = new ArmorSet.Light("earth", SpellSchools.ELEMENTAL_EARTH);
        AIR_ARMOR_L = new ArmorSet.Light("air", SpellSchools.ELEMENTAL_AIR);
        FIRE_ARMOR_L = new ArmorSet.Light("fire", SpellSchools.ELEMENTAL_FIRE);
        FIRE_ARMOR_H = new ArmorSet.Heavy("fire", SpellSchools.ELEMENTAL_FIRE);


        SIREN_SHARDS = ITEMS.register("siren_shards", () -> new ModItem(itemProps()).withTooltip(Component.translatable("tooltip.siren_shards")));
        FLASHJACK_SHARDS = ITEMS.register("flashjack_shards", () -> new ModItem(itemProps()).withTooltip(Component.translatable("tooltip.flashjack_shards")));

        SIREN_CHARM = ITEMS.register("siren_charm", () -> new SirenCharm(itemProps()));
        FIRENANDO_CHARM = ITEMS.register("firenando_charm", () -> new FirenandoCharm(itemProps()));
        FLASHJACK_CHARM = ITEMS.register("flashjack_charm", () -> new FlashjackCharm(itemProps()));

        DEBUG_ICON = ITEMS.register("debug", () -> new Debugger(new Item.Properties()));
        MARK_OF_MASTERY = (DeferredItem<Item>) ITEMS.register("mark_of_mastery", () -> new Item(itemProps()));

        SPELL_HORN = ITEMS.register("spell_horn", () -> new SpellHorn(itemProps()));

        HOMING_LENS = ITEMS.register("homing_prism_lens", () -> new HomingPrismLens(itemProps()));
        ARC_LENS = ITEMS.register("arc_prism_lens", () -> new ArcPrismLens(itemProps()));
        ACC_LENS = ITEMS.register("acceleration_prism_lens", () -> new AccelerationPrismLens(itemProps()));
        DEC_LENS = ITEMS.register("deceleration_prism_lens", () -> new DecelerationPrismLens(itemProps()));
        PIERCE_LENS = ITEMS.register("piercing_prism_lens", () -> new PiercingPrismLens(itemProps()));
        CHAIN_LENS = ITEMS.register("chaining_prism_lens", () -> new ChainingPrismLens(itemProps()));

        //curio
        CURIO_BAG = ITEMS.register("curio_bag", () -> new CurioHolder(itemProps().fireResistant().stacksTo(1)));
        CASTER_BAG = ITEMS.register("caster_bag", () -> new CasterHolder(itemProps().fireResistant().stacksTo(1).component(DataComponents.BASE_COLOR, DyeColor.RED)));

        FIRE_FOCUS = ITEMS.register("fire_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_FIRE));
        WATER_FOCUS = ITEMS.register("water_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_WATER));
        AIR_FOCUS = ITEMS.register("air_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_AIR));
        EARTH_FOCUS = ITEMS.register("earth_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_FOCUS = ITEMS.register("necrotic_focus", () -> new NecroticFocus(FocusProp()));

        LESSER_FIRE_FOCUS = ITEMS.register("lesser_fire_focus", () -> new ElementalFocus(UncommonProp(), SpellSchools.ELEMENTAL_FIRE));
        LESSER_WATER_FOCUS = ITEMS.register("lesser_water_focus", () -> new ElementalFocus(UncommonProp(), SpellSchools.ELEMENTAL_WATER));
        LESSER_AIR_FOCUS = ITEMS.register("lesser_air_focus", () -> new ElementalFocus(UncommonProp(), SpellSchools.ELEMENTAL_AIR));
        LESSER_EARTH_FOCUS = ITEMS.register("lesser_earth_focus", () -> new ElementalFocus(UncommonProp(), SpellSchools.ELEMENTAL_EARTH));

        //bangles
        ENCHANTER_BANGLE = ITEMS.register("base_bangle", () -> new BaseBangle(itemProps().stacksTo(1)));
        FIRE_BANGLE = ITEMS.register("fire_bangle", () -> new FireBangles(UncommonProp()));
        WATER_BANGLE = ITEMS.register("water_bangle", () -> new WaterBangles(UncommonProp()));
        AIR_BANGLE = ITEMS.register("air_bangle", () -> new AirBangles(UncommonProp()));
        EARTH_BANGLE = ITEMS.register("earth_bangle", () -> new EarthBangles(UncommonProp()));
        SUMMON_BANGLE = ITEMS.register("summon_bangle", () -> new SummonBangles(UncommonProp()));
        ANIMA_BANGLE = ITEMS.register("anima_bangle", () -> new AnimaBangles(UncommonProp()));

        //caster tomes
        FIRE_CTOME = ITEMS.register("fire_caster_tome", () -> new SchoolCasterTome(itemProps(), SpellSchools.ELEMENTAL_FIRE));
        WATER_CTOME = ITEMS.register("water_caster_tome", () -> new SchoolCasterTome(itemProps(), SpellSchools.ELEMENTAL_WATER));
        AIR_CTOME = ITEMS.register("air_caster_tome", () -> new SchoolCasterTome(itemProps(), SpellSchools.ELEMENTAL_AIR));
        EARTH_CTOME = ITEMS.register("earth_caster_tome", () -> new SchoolCasterTome(itemProps(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_CTOME = ITEMS.register("anima_caster_tome", () -> new SchoolCasterTome(itemProps(), SpellSchools.NECROMANCY));
        SHAPERS_CTOME = ITEMS.register("manipulation_caster_tome", () -> new SchoolCasterTome(itemProps(), SpellSchools.MANIPULATION));

        //blocks
        WATER_URN = addBlock("everfull_urn", () -> new EverfullUrnBlock(blockProps(Blocks.CLAY, MapColor.COLOR_BROWN).sound(SoundType.PACKED_MUD).noOcclusion()));
        MERMAID_ROCK = addBlock("mermaid_rock", () -> new MermaidRock(blockProps(Blocks.STONE, MapColor.COLOR_LIGHT_BLUE).sound(SoundType.CORAL_BLOCK).strength(2.0f, 6.0f).noOcclusion().lightLevel(b -> 10)));
        GROUND_BLOSSOM = addBlock("spore_blossom_up", () -> new SporeBlossomGround(blockProps(Blocks.SPORE_BLOSSOM, MapColor.COLOR_PINK).sound(SoundType.SPORE_BLOSSOM).noOcclusion()));
        WATER_UPSTREAM_BLOCK = addBlock("water_upstream", () -> new UpstreamBlock(blockProps(Blocks.STONE, MapColor.COLOR_LIGHT_BLUE).sound(SoundType.STONE).strength(2.0f, 6.0f)) {
            @Override
            public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new WaterUpstreamTile(pPos, pState);
            }
        });
        LAVA_UPSTREAM_BLOCK = addBlock("magma_upstream", () -> new UpstreamBlock(blockProps(Blocks.STONE, MapColor.COLOR_RED).sound(SoundType.STONE).strength(2.0f, 6.0f)) {
            @Override
            public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new MagmaUpstreamTile(pPos, pState);
            }
        });
        AIR_UPSTREAM_BLOCK = addBlock("air_upstream", () -> new UpstreamBlock(blockProps(Blocks.STONE, MapColor.COLOR_YELLOW).sound(SoundType.STONE).strength(2.0f, 6.0f)) {
            @Override
            public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
                return new AirUpstreamTile(pPos, pState);
            }
        });

        //turrets
        FIRE_TURRET = addTurret("fire_turret", () -> new FocusEnhancedSpellTurret(blockProps(Blocks.GOLD_BLOCK, MapColor.COLOR_RED).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_FIRE), "fire");
        WATER_TURRET = addTurret("water_turret", () -> new FocusEnhancedSpellTurret(blockProps(Blocks.GOLD_BLOCK, MapColor.COLOR_LIGHT_BLUE).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_WATER), "water");
        AIR_TURRET = addTurret("air_turret", () -> new FocusEnhancedSpellTurret(blockProps(Blocks.GOLD_BLOCK, MapColor.COLOR_YELLOW).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_AIR), "air");
        EARTH_TURRET = addTurret("earth_turret", () -> new FocusEnhancedSpellTurret(blockProps(Blocks.GOLD_BLOCK, MapColor.COLOR_GREEN).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_EARTH), "earth");
        SHAPING_TURRET = addTurret("manipulation_turret", () -> new FocusEnhancedSpellTurret(blockProps(Blocks.GOLD_BLOCK, MapColor.COLOR_ORANGE).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.MANIPULATION), "manipulation");

        //relays
        FIRE_RELAY = addRelay("fire_relay", FireRelay::new, "source_collector");
        WATER_RELAY = addRelay("water_relay", WaterRelay::new, "source_splitter");
        AIR_RELAY = addRelay("air_relay", AirRelay::new, "source_warp");
        EARTH_RELAY = addRelay("earth_relay", EarthRelay::new, "source_deposit");

        ADVANCED_PRISM = BLOCKS.register("advanced_prism", () -> new AdvancedPrism(blockProps(Blocks.STONE, MapColor.TERRACOTTA_WHITE)));
        ITEMS.register("advanced_prism", () -> new RendererBlockItem(ADVANCED_PRISM.get(), itemProps()) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Supplier<BlockEntityWithoutLevelRenderer> getRenderer() {
                return PrismRenderer::getISTER;
            }
        });
        SPELL_MIRROR = addBlock("spell_mirror", () -> new SpellMirror(blockProps(Blocks.STONE, MapColor.TERRACOTTA_WHITE)));

        //Trees
        FLASHING_SAPLING = addBlock("yellow_archwood_sapling", () -> new SaplingBlock(MagicTree.getGrower("flashing_tree", ModWorldgen.FLASHING_TREE_SAPLING), SAP_PROP));
        FLASHING_LEAVES = addBlock("yellow_archwood_leaves", () -> new MagicLeaves(blockProps(Blocks.OAK_LEAVES, MapColor.COLOR_YELLOW).lightLevel(b -> ConfigHandler.Startup.FLASHING_LEAVES_LIGHT.get()).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(
                ModItems::allowsSpawnOnLeaves).isSuffocating((state1, reader1, pos1) -> false).isViewBlocking((state, reader, pos) -> false)));
        FLASHING_ARCHWOOD_LOG_STRIPPED = addBlock("stripped_yellow_archwood_log", () -> new RotatedPillarBlock(LOG_PROP.mapColor(MapColor.COLOR_YELLOW).lightLevel(b -> ConfigHandler.Startup.FLASHING_WOOD_STRIP_LIGHT.get())));
        FLASHING_ARCHWOOD_STRIPPED = addBlock("stripped_yellow_archwood", () -> new RotatedPillarBlock(LOG_PROP.mapColor(MapColor.COLOR_YELLOW).lightLevel(b -> ConfigHandler.Startup.FLASHING_WOOD_STRIP_LIGHT.get())));
        FLASHING_ARCHWOOD_LOG = addBlock("yellow_archwood_log", () -> new StrippableLog(LOG_PROP.mapColor(MapColor.COLOR_YELLOW).lightLevel(b -> ConfigHandler.Startup.FLASHING_WOOD_LIGHT.get()), FLASHING_ARCHWOOD_LOG_STRIPPED::get));
        FLASHING_ARCHWOOD = addBlock("yellow_archwood", () -> new StrippableLog(LOG_PROP.mapColor(MapColor.COLOR_YELLOW).lightLevel(b -> ConfigHandler.Startup.FLASHING_WOOD_LIGHT.get()), FLASHING_ARCHWOOD_STRIPPED::get));
        FLASHING_POD = BLOCKS.register("flashpine_pod", () -> new ArchfruitPod(AETagsProvider.AEBlockTagsProvider.FLASHING_LOGS));
        ITEMS.register("flashpine_pod", () -> new ItemNameBlockItem(FLASHING_POD.get(), itemProps().food(FLASHPINE_FOOD)));
        POT_FLASHING_SAPLING = BLOCKS.register("potted_yellow_archwood_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, FLASHING_SAPLING, blockProps(Blocks.FLOWER_POT, MapColor.COLOR_YELLOW).instabreak().noOcclusion()));
        SPARKFLOWER = addBlock("sparkflower", () -> new FlowerBlock(ModPotions.SHOCKED_EFFECT, 200, blockProps(Blocks.POPPY, MapColor.COLOR_YELLOW).lightLevel(b -> 8).sound(SoundType.GRASS).noOcclusion()));
        POT_SPARKFLOWER = BLOCKS.register("potted_sparkflower", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, SPARKFLOWER, blockProps(Blocks.FLOWER_POT, MapColor.COLOR_YELLOW).instabreak().noOcclusion()));


        // Source Vines
        SOURCE_VINES = BLOCKS.register(
                "source_vines",
                () -> new CaveVinesBlock(
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.PLANT)
                                .randomTicks()
                                .noCollission()
                                .lightLevel(CaveVines.emission(14))
                                .instabreak()
                                .sound(SoundType.CAVE_VINES)
                                .pushReaction(PushReaction.DESTROY)
                ) {

                    @Override
                    protected @NotNull Block getBodyBlock() {
                        return SOURCE_VINES_PLANT.get();
                    }

                    @Override
                    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
                        return BlockRegistry.SOURCEBERRY_BUSH.get().asItem().getDefaultInstance();
                    }

                    @Override
                    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState p_152980_, @NotNull Level p_152981_, @NotNull BlockPos p_152982_, @NotNull Player p_152983_, @NotNull BlockHitResult p_152985_) {
                        return harvestBerry(p_152983_, p_152980_, p_152981_, p_152982_);
                    }
                }
        );
        SOURCE_VINES_PLANT = BLOCKS.register(
                "source_vines_plant",
                () -> new CaveVinesPlantBlock(
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.PLANT)
                                .noCollission()
                                .lightLevel(CaveVines.emission(14))
                                .instabreak()
                                .sound(SoundType.CAVE_VINES)
                                .pushReaction(PushReaction.DESTROY)
                ) {

                    @Override
                    protected @NotNull GrowingPlantHeadBlock getHeadBlock() {
                        return SOURCE_VINES.get();
                    }

                    @Override
                    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
                        return BlockRegistry.SOURCEBERRY_BUSH.get().asItem().getDefaultInstance();
                    }

                    @Override
                    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState p_152980_, @NotNull Level p_152981_, @NotNull BlockPos p_152982_, @NotNull Player p_152983_, @NotNull BlockHitResult p_152985_) {
                        return harvestBerry(p_152983_, p_152980_, p_152981_, p_152982_);
                    }
                }
        );
    }

    static InteractionResult harvestBerry(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        if (state.getValue(BERRIES)) {
            Block.popResource(level, pos, BlockRegistry.SOURCEBERRY_BUSH.get().asItem().getDefaultInstance());
            float f = Mth.randomBetween(level.random, 0.8F, 1.2F);
            level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, f);
            BlockState blockstate = state.setValue(BERRIES, Boolean.FALSE);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    static Item.Properties itemProps() {
        return new Item.Properties();
    }

    static Item.Properties FocusProp() {
        return itemProps().stacksTo(1).fireResistant().rarity(Rarity.EPIC);
    }

    static Item.Properties UncommonProp() {
        return itemProps().stacksTo(1).rarity(Rarity.UNCOMMON);
    }

    public static Item.Properties ArmorProp() {
        return itemProps().stacksTo(1).rarity(Rarity.EPIC).component(DataComponentRegistry.ARMOR_PERKS, new ArmorPerkHolder());
    }

    static DeferredHolder<Block, ? extends Block> addBlock(String name, Supplier<Block> blockSupp) {
        DeferredHolder<Block, ? extends Block> block = BLOCKS.register(name, blockSupp);
        ITEMS.register(name, () -> new BlockItem(block.get(), itemProps()));
        return block;
    }

    static DeferredHolder<Block, ? extends Block> addTurret(String name, Supplier<Block> blockSupp, String model) {
        DeferredHolder<Block, ? extends Block> block = BLOCKS.register(name, blockSupp);
        ITEMS.register(name, () -> new RendererBlockItem(block.get(), itemProps()) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Supplier<BlockEntityWithoutLevelRenderer> getRenderer() {
                return () -> FocusTurretRenderer.getISTER(model);
            }
        });
        return block;
    }

    static DeferredHolder<Block, ? extends Block> addRelay(String name, Supplier<Block> blockSupp, String model) {
        DeferredHolder<Block, ? extends Block> block = BLOCKS.register(name, blockSupp);
        ResourceLocation texLoc = prefix("textures/block/" + name + ".png");
        ITEMS.register(name, () -> new RendererBlockItem(block.get(), itemProps()) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Supplier<BlockEntityWithoutLevelRenderer> getRenderer() {
                return () -> new GenericItemBlockRenderer(new GenericModel<>(model) {
                    @Override
                    public ResourceLocation getTextureResource(GeoAnimatable GeoAnimatable) {
                        return texLoc;
                    }
                });
            }
        });
        return block;
    }

    static BlockBehaviour.Properties blockProps(Block copyFrom, MapColor color) {
        return BlockBehaviour.Properties.ofFullCopy(copyFrom).mapColor(color);
    }

    private static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }

}
