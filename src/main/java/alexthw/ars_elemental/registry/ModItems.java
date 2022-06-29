package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ArsNouveauRegistry;
import alexthw.ars_elemental.client.ElementalTurretRenderer;
import alexthw.ars_elemental.common.blocks.ElementalTurret;
import alexthw.ars_elemental.common.blocks.SporeBlossomGround;
import alexthw.ars_elemental.common.blocks.UpstreamBlock;
import alexthw.ars_elemental.common.blocks.mermaid_block.MermaidRock;
import alexthw.ars_elemental.common.items.*;
import alexthw.ars_elemental.common.items.armor.ElementalArmor;
import alexthw.ars_elemental.common.items.armor.ElementalHat;
import alexthw.ars_elemental.common.items.bangles.*;
import alexthw.ars_elemental.common.items.foci.ElementalFocus;
import alexthw.ars_elemental.common.items.foci.GreaterElementalFocus;
import alexthw.ars_elemental.common.items.foci.NecroticFocus;
import alexthw.ars_elemental.world.WorldEvents;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.block.MagicLeaves;
import com.hollingsworth.arsnouveau.common.block.StrippableLog;
import com.hollingsworth.arsnouveau.common.items.RendererBlockItem;
import com.hollingsworth.arsnouveau.common.world.tree.MagicTree;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static com.hollingsworth.arsnouveau.setup.BlockRegistry.LOG_PROP;
import static com.hollingsworth.arsnouveau.setup.BlockRegistry.SAP_PROP;

@SuppressWarnings("SameParameterValue")
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> MERMAID_ROCK;
    public static final RegistryObject<Block> FLASHING_ARCHWOOD_LOG;
    public static final RegistryObject<Block> FLASHING_ARCHWOOD_LOG_STRIPPED;
    public static final RegistryObject<Block> FLASHING_ARCHWOOD_STRIPPED;
    public static final RegistryObject<Block> FLASHING_ARCHWOOD;
    public static final RegistryObject<Block> FLASHING_SAPLING;
    public static final RegistryObject<Block> FLASHING_LEAVES;

    public static final RegistryObject<Block> GROUND_BLOSSOM;

    public static final RegistryObject<Block> UPSTREAM_BLOCK;
    public static final RegistryObject<Block> FIRE_TURRET;
    public static final RegistryObject<Block> WATER_TURRET;
    public static final RegistryObject<Block> AIR_TURRET;
    public static final RegistryObject<Block> EARTH_TURRET;


    public static final RegistryObject<Item> FIRE_FOCUS;
    public static final RegistryObject<Item> AIR_FOCUS;
    public static final RegistryObject<Item> WATER_FOCUS;
    public static final RegistryObject<Item> EARTH_FOCUS;
    public static final RegistryObject<Item> NECRO_FOCUS;

    public static final RegistryObject<Item> LESSER_FIRE_FOCUS;
    public static final RegistryObject<Item> LESSER_AIR_FOCUS;
    public static final RegistryObject<Item> LESSER_WATER_FOCUS;
    public static final RegistryObject<Item> LESSER_EARTH_FOCUS;

    public static final ArmorSet FIRE_ARMOR = new ArmorSet("fire", SpellSchools.ELEMENTAL_FIRE);
    public static final ArmorSet AIR_ARMOR = new ArmorSet("air", SpellSchools.ELEMENTAL_AIR);
    public static final ArmorSet EARTH_ARMOR = new ArmorSet("earth", SpellSchools.ELEMENTAL_EARTH);
    public static final ArmorSet WATER_ARMOR = new ArmorSet("aqua", SpellSchools.ELEMENTAL_WATER);

    public static final RegistryObject<Item> FIRE_CTOME;
    public static final RegistryObject<Item> AIR_CTOME;
    public static final RegistryObject<Item> WATER_CTOME;
    public static final RegistryObject<Item> EARTH_CTOME;
    public static final RegistryObject<Item> NECRO_CTOME;

    public static final RegistryObject<Item> ENCHANTER_BANGLE;
    public static final RegistryObject<Item> FIRE_BANGLE;
    public static final RegistryObject<Item> WATER_BANGLE;
    public static final RegistryObject<Item> AIR_BANGLE;
    public static final RegistryObject<Item> EARTH_BANGLE;

    public static final RegistryObject<Item> CURIO_BAG;
    public static final RegistryObject<Item> DEBUG_ICON;
    public static final RegistryObject<Item> MARK_OF_MASTERY;
    public static final RegistryObject<Item> SIREN_SHARDS;
    public static final RegistryObject<Item> SIREN_CHARM;
    public static final RegistryObject<Item> FIRENANDO_CHARM;

    public static final RegistryObject<Item> SPELL_HORN;

    static Item.Properties addTabProp() {
        return new Item.Properties().tab(ArsElemental.TAB);
    }

    static Item.Properties FocusProp() {
        return addTabProp().stacksTo(1).fireResistant().rarity(Rarity.RARE);
    }

    static {

        SIREN_SHARDS = ITEMS.register("siren_shards", () -> new Item(addTabProp()));
        SIREN_CHARM = ITEMS.register("siren_charm", () -> new SirenCharm(addTabProp()));
        FIRENANDO_CHARM = ITEMS.register("firenando_charm", () -> new FirenandoCharm(addTabProp().fireResistant()));

        DEBUG_ICON = ITEMS.register("debug", () -> new Debugger(new Item.Properties()));
        MARK_OF_MASTERY = ITEMS.register("mark_of_mastery", () -> new Item(addTabProp()));

        SPELL_HORN = ITEMS.register("spell_horn", () -> new SpellHorn(addTabProp()));

        //curio
        CURIO_BAG = ITEMS.register("curio_bag", () -> new CurioHolder(addTabProp().fireResistant().stacksTo(1)));
        FIRE_FOCUS = ITEMS.register("fire_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_FIRE));
        WATER_FOCUS = ITEMS.register("water_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_WATER));
        AIR_FOCUS = ITEMS.register("air_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_AIR));
        EARTH_FOCUS = ITEMS.register("earth_focus", () -> new GreaterElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_FOCUS = ITEMS.register("necrotic_focus", () -> new NecroticFocus(FocusProp()));

        LESSER_FIRE_FOCUS = ITEMS.register("lesser_fire_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_FIRE));
        LESSER_WATER_FOCUS = ITEMS.register("lesser_water_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_WATER));
        LESSER_AIR_FOCUS = ITEMS.register("lesser_air_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_AIR));
        LESSER_EARTH_FOCUS = ITEMS.register("lesser_earth_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_EARTH));

        //bangles
        ENCHANTER_BANGLE = ITEMS.register("base_bangle", () -> new BaseBangle(addTabProp().stacksTo(1)));
        FIRE_BANGLE = ITEMS.register("fire_bangle", () -> new FireBangles(FocusProp()));
        WATER_BANGLE = ITEMS.register("water_bangle", () -> new WaterBangles(FocusProp()));
        AIR_BANGLE = ITEMS.register("air_bangle", () -> new AirBangles(FocusProp()));
        EARTH_BANGLE = ITEMS.register("earth_bangle", () -> new EarthBangles(FocusProp()));

        //caster tomes
        FIRE_CTOME = ITEMS.register("fire_caster_tome", () -> new ElementalCasterTome(addTabProp().stacksTo(1), SpellSchools.ELEMENTAL_FIRE));
        WATER_CTOME = ITEMS.register("water_caster_tome", () -> new ElementalCasterTome(addTabProp().stacksTo(1), SpellSchools.ELEMENTAL_WATER));
        AIR_CTOME = ITEMS.register("air_caster_tome", () -> new ElementalCasterTome(addTabProp().stacksTo(1), SpellSchools.ELEMENTAL_AIR));
        EARTH_CTOME = ITEMS.register("earth_caster_tome", () -> new ElementalCasterTome(addTabProp().stacksTo(1), SpellSchools.ELEMENTAL_EARTH));
        NECRO_CTOME = ITEMS.register("anima_caster_tome", () -> new ElementalCasterTome(addTabProp().stacksTo(1), ArsNouveauRegistry.NECROMANCY));

        //blocks
        MERMAID_ROCK = addBlock("mermaid_rock", () -> new MermaidRock(blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.CORAL_BLOCK).strength(2.0f, 6.0f).noOcclusion().lightLevel(b -> 10)));
        UPSTREAM_BLOCK = addBlock("water_upstream", () -> new UpstreamBlock(blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.STONE).strength(2.0f, 6.0f)));
        GROUND_BLOSSOM = addBlock("spore_blossom_up", () -> new SporeBlossomGround(blockProps(Material.PLANT, MaterialColor.COLOR_PINK).sound(SoundType.SPORE_BLOSSOM).noOcclusion()));

        //turrets
        FIRE_TURRET = addSpecialBlock("fire_turret", () -> new ElementalTurret(blockProps(Material.METAL, MaterialColor.COLOR_RED).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_FIRE), "fire");
        WATER_TURRET = addSpecialBlock("water_turret", () -> new ElementalTurret(blockProps(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_WATER), "water");
        AIR_TURRET = addSpecialBlock("air_turret", () -> new ElementalTurret(blockProps(Material.METAL, MaterialColor.COLOR_YELLOW).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_AIR), "air");
        EARTH_TURRET = addSpecialBlock("earth_turret", () -> new ElementalTurret(blockProps(Material.METAL, MaterialColor.COLOR_GREEN).sound(SoundType.STONE).strength(2.0f, 6.0f), SpellSchools.ELEMENTAL_EARTH), "earth");

        //Trees
        FLASHING_SAPLING = addBlock("yellow_archwood_sapling", () -> new SaplingBlock(new MagicTree(() -> WorldEvents.FLASHING_TREE), SAP_PROP));
        FLASHING_LEAVES = addBlock("yellow_archwood_leaves", () -> new MagicLeaves(blockProps(Material.LEAVES, MaterialColor.COLOR_YELLOW).lightLevel(b -> 6).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(
                ModItems::allowsSpawnOnLeaves).isSuffocating(ModItems::isntSolid).isViewBlocking(ModItems::isntSolid)));
        FLASHING_ARCHWOOD_LOG_STRIPPED = addBlock("stripped_yellow_archwood_log", () -> new RotatedPillarBlock(LOG_PROP.color(MaterialColor.COLOR_YELLOW).lightLevel(b -> 4)));
        FLASHING_ARCHWOOD_STRIPPED = addBlock("stripped_yellow_archwood", () -> new RotatedPillarBlock(LOG_PROP.color(MaterialColor.COLOR_YELLOW).lightLevel(b -> 4)));
        FLASHING_ARCHWOOD_LOG = addBlock("yellow_archwood_log", () -> new StrippableLog(LOG_PROP.color(MaterialColor.COLOR_YELLOW).lightLevel(b -> 6), FLASHING_ARCHWOOD_LOG_STRIPPED));
        FLASHING_ARCHWOOD = addBlock("yellow_archwood", () -> new StrippableLog(LOG_PROP.color(MaterialColor.COLOR_YELLOW).lightLevel(b -> 6), FLASHING_ARCHWOOD_STRIPPED));

    }

    static RegistryObject<Block> addBlock(String name, Supplier<Block> blockSupp) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupp);
        ITEMS.register(name, () -> new BlockItem(block.get(), addTabProp()));
        return block;
    }

    static RegistryObject<Block> addSpecialBlock(String name, Supplier<Block> blockSupp, String model) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupp);
        ITEMS.register(name, () -> new RendererBlockItem(block.get(), addTabProp()) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Supplier<BlockEntityWithoutLevelRenderer> getRenderer() {
                return () -> ElementalTurretRenderer.getISTER(model);
            }
        });
        return block;
    }

    static BlockBehaviour.Properties blockProps(Material mat, MaterialColor color) {
        return BlockBehaviour.Properties.of(mat, color);
    }

    private static Boolean allowsSpawnOnLeaves(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }

    private static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }

    public static class ArmorSet {
        public ArmorSet(String name, SpellSchool element) {
            this.head = ITEMS.register(name + "_hat", () -> new ElementalHat(element, addTabProp()));
            this.chest = ITEMS.register(name + "_robes", () -> new ElementalArmor(EquipmentSlot.CHEST, element, addTabProp()));
            this.legs = ITEMS.register(name + "_leggings", () -> new ElementalArmor(EquipmentSlot.LEGS, element, addTabProp()));
            this.feet = ITEMS.register(name + "_boots", () -> new ElementalArmor(EquipmentSlot.FEET, element, addTabProp()));
        }

        RegistryObject<Item> head;
        RegistryObject<Item> chest;
        RegistryObject<Item> legs;
        RegistryObject<Item> feet;

        public Item getHat() {
            return head.get();
        }

        public Item getChest() {
            return chest.get();
        }

        public Item getLegs() {
            return legs.get();
        }

        public Item getBoots() {
            return feet.get();
        }

    }


}
