package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.blocks.UpstreamBlock;
import alexthw.ars_elemental.common.blocks.mermaid_block.MermaidRock;
import alexthw.ars_elemental.common.items.*;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> MERMAID_ROCK;
    public static final RegistryObject<Block> UPSTREAM_BLOCK;

    public static final RegistryObject<Item> FIRE_FOCUS;
    public static final RegistryObject<Item> AIR_FOCUS;
    public static final RegistryObject<Item> WATER_FOCUS;
    public static final RegistryObject<Item> EARTH_FOCUS;
    public static final RegistryObject<Item> NECRO_FOCUS;

    public static final RegistryObject<Item> CURIO_BAG;
    public static final RegistryObject<Item> DEBUG_ICON;

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

        SIREN_CHARM = ITEMS.register("siren_charm", () -> new SirenCharm(addTabProp()));
        FIRENANDO_CHARM = ITEMS.register("firenando_charm", () -> new FirenandoCharm(addTabProp()));

        DEBUG_ICON = ITEMS.register("debug", () -> new Debugger(new Item.Properties()));

        SPELL_HORN = ITEMS.register("spell_horn", () -> new SpellHorn(addTabProp()));

        CURIO_BAG = ITEMS.register("curio_bag", () -> new CurioHolder(addTabProp().fireResistant().stacksTo(1)));
        FIRE_FOCUS = ITEMS.register("fire_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_FIRE));
        WATER_FOCUS = ITEMS.register("water_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_WATER));
        AIR_FOCUS = ITEMS.register("air_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_AIR));
        EARTH_FOCUS = ITEMS.register("earth_focus", () -> new ElementalFocus(FocusProp(), SpellSchools.ELEMENTAL_EARTH));
        NECRO_FOCUS = ITEMS.register("necrotic_focus", () -> new NecroticFocus(FocusProp()));

        //blocks
        MERMAID_ROCK = addBlock("mermaid_rock", new MermaidRock(blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.CORAL_BLOCK).strength(2.0f, 6.0f)));
        UPSTREAM_BLOCK = addBlock("water_upstream", new UpstreamBlock(blockProps(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.STONE).strength(2.0f, 6.0f)));

    }

    static RegistryObject<Block> addBlock(String name, Block block) {
        ITEMS.register(name, () -> new BlockItem(block, addTabProp()));
        return BLOCKS.register(name, () -> block);
    }

    static RegistryObject<Block> addSlab(String name, SlabBlock slab) {
        return addBlock(name + "_slab", slab);
    }

    static RegistryObject<Block> addStair(String name, StairBlock stair) {
        return addBlock(name + "_stairs", stair);
    }

    static BlockBehaviour.Properties blockProps(Material mat, MaterialColor color) {
        return BlockBehaviour.Properties.of(mat, color);
    }
}
