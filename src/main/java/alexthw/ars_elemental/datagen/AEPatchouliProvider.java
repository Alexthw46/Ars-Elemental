package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.common.entity.familiars.FirenandoHolder;
import alexthw.ars_elemental.common.entity.familiars.MermaidHolder;
import alexthw.ars_elemental.common.rituals.DetectionRitual;
import alexthw.ars_elemental.common.rituals.SquirrelRitual;
import alexthw.ars_elemental.common.rituals.TeslaRitual;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.*;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.ArsNouveauRegistry.registeredSpells;

public class AEPatchouliProvider extends PatchouliProvider {

    public AEPatchouliProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void run(CachedOutput cache) throws IOException {

        for (AbstractSpellPart spell : registeredSpells) {
            addGlyphPage(spell);
        }

        addPage(new PatchouliBuilder(GETTING_STARTED, "elemental_tweaks")
                        .withIcon(ModItems.DEBUG_ICON.get())
                        .withTextPage("ars_elemental.page.elemental_tweaks")
                , getPath(GETTING_STARTED, "elemental_tweaks"));

        addPage(new PatchouliBuilder(RESOURCES, ModItems.FLASHING_ARCHWOOD.get())
                        .withTextPage("ars_elemental.page1.flashing_archwood")
                        .withPage(new EntityPage(prefix("flashing_weald_walker").toString()))
                , getPath(RESOURCES, "flashing_archwood"));

        addBasicItem(ModItems.UPSTREAM_BLOCK.get(), MACHINES, new ApparatusPage(ModItems.UPSTREAM_BLOCK.get()));
        addBasicItem(ModItems.CURIO_BAG.get(), EQUIPMENT, new CraftingPage(ModItems.CURIO_BAG.get()));
        addBasicItem(ModItems.ENCHANTER_BANGLE.get(), EQUIPMENT, new ApparatusPage(ModItems.ENCHANTER_BANGLE.get()));


        addPage(new PatchouliBuilder(MACHINES, "elemental_turrets")
                        .withIcon(ModItems.FIRE_TURRET.get())
                        .withTextPage("ars_elemental.page1.elemental_turrets")
                        .withPage(ImbuementPage(ModItems.FIRE_TURRET.get()))
                        .withPage(ImbuementPage(ModItems.WATER_TURRET.get()))
                        .withPage(ImbuementPage(ModItems.AIR_TURRET.get()))
                        .withPage(ImbuementPage(ModItems.EARTH_TURRET.get()))
                , getPath(MACHINES, "elemental_turrets"));

        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.SPELL_HORN.get())
                        .withIcon(ModItems.SPELL_HORN.get())
                        .withTextPage("ars_elemental.page1.spell_horn")
                        .withPage(new ApparatusPage(ModItems.SPELL_HORN.get()))
                , getPath(EQUIPMENT, "spell_horn"));

        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.NECRO_FOCUS.get())
                        .withIcon(ModItems.NECRO_FOCUS.get())
                        .withTextPage("ars_elemental.page1.necrotic_focus")
                        .withPage(new ApparatusPage(ModItems.NECRO_FOCUS.get()))
                        .withTextPage("ars_elemental.page2.necrotic_focus")
                , getPath(EQUIPMENT, "necrotic_focus")
        );

        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.FIRE_FOCUS.get())
                        .withIcon(ModItems.FIRE_FOCUS.get())
                        .withTextPage("ars_elemental.page1.fire_focus")
                        .withPage(ImbuementPage(ModItems.FIRE_FOCUS.get()))
                        .withTextPage("ars_elemental.page2.fire_focus")
                , getPath(EQUIPMENT, "fire_focus")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.FIRE_BANGLE.get())
                        .withIcon(ModItems.FIRE_BANGLE.get())
                        .withTextPage("ars_elemental.page1.fire_bangle")
                        .withPage(new ApparatusPage(ModItems.FIRE_BANGLE.get()))
                , getPath(EQUIPMENT, "fire_bangle")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.WATER_FOCUS.get())
                        .withIcon(ModItems.WATER_FOCUS.get())
                        .withTextPage("ars_elemental.page1.water_focus")
                        .withPage(ImbuementPage(ModItems.WATER_FOCUS.get()))
                        .withTextPage("ars_elemental.page2.water_focus")
                , getPath(EQUIPMENT, "water_focus")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.WATER_BANGLE.get())
                        .withIcon(ModItems.WATER_BANGLE.get())
                        .withTextPage("ars_elemental.page1.water_bangle")
                        .withPage(new ApparatusPage(ModItems.WATER_BANGLE.get()))
                , getPath(EQUIPMENT, "water_bangle")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.AIR_FOCUS.get())
                        .withIcon(ModItems.AIR_FOCUS.get())
                        .withTextPage("ars_elemental.page1.air_focus")
                        .withPage(ImbuementPage(ModItems.AIR_FOCUS.get()))
                        .withTextPage("ars_elemental.page2.air_focus")
                , getPath(EQUIPMENT, "air_focus")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.AIR_BANGLE.get())
                        .withIcon(ModItems.AIR_BANGLE.get())
                        .withTextPage("ars_elemental.page1.air_bangle")
                        .withPage(new ApparatusPage(ModItems.AIR_BANGLE.get()))
                , getPath(EQUIPMENT, "air_bangle")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.EARTH_FOCUS.get())
                        .withIcon(ModItems.EARTH_FOCUS.get())
                        .withTextPage("ars_elemental.page1.earth_focus")
                        .withPage(ImbuementPage(ModItems.EARTH_FOCUS.get()))
                        .withTextPage("ars_elemental.page2.earth_focus")
                , getPath(EQUIPMENT, "earth_focus")
        );
        addPage(new PatchouliBuilder(EQUIPMENT, ModItems.EARTH_BANGLE.get())
                        .withIcon(ModItems.EARTH_BANGLE.get())
                        .withTextPage("ars_elemental.page1.earth_bangle")
                        .withPage(new ApparatusPage(ModItems.EARTH_BANGLE.get()))
                , getPath(EQUIPMENT, "earth_bangle")
        );

        addPage(new PatchouliBuilder(AUTOMATION, ModItems.SIREN_CHARM.get())
                        .withIcon(ModItems.SIREN_CHARM.get())
                        .withTextPage("ars_elemental.page1.mermaid")
                        .withPage(new ApparatusPage(ModItems.SIREN_CHARM.get()))
                        .withPage(new EntityPage(prefix("siren_entity").toString()))
                        .withTextPage("ars_elemental.page2.mermaid")
                , getPath(AUTOMATION, "mermaid"));

        addPage(new PatchouliBuilder(AUTOMATION, ModItems.FIRENANDO_CHARM.get())
                        .withIcon(ModItems.FIRENANDO_CHARM.get())
                        .withTextPage("ars_elemental.page1.fire_golem")
                        .withPage(new EntityPage(prefix("firenando_entity").toString()))
                        .withPage(new ApparatusPage(ModItems.FIRENANDO_CHARM.get()))
                , getPath(AUTOMATION, "fire_golem"));

        addFamiliarPage(new MermaidHolder());
        addFamiliarPage(new FirenandoHolder());

        addRitualPage(new SquirrelRitual());
        addRitualPage(new TeslaRitual());
        addRitualPage(new DetectionRitual());

        addEnchantmentPage(ModRegistry.MIRROR.get());

        for (PatchouliPage patchouliPage : pages) {
            DataProvider.saveStable(cache, patchouliPage.build(), patchouliPage.path());
        }

    }

    @Override
    public void addBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage) {
        PatchouliBuilder builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                .withIcon(item.asItem())
                .withPage(new TextPage("ars_elemental.page." + getRegistryName(item.asItem()).getPath()))
                .withPage(recipePage);
        this.pages.add(new PatchouliPage(builder, getPath(category, getRegistryName(item.asItem()).getPath())));
    }

    public void addFamiliarPage(AbstractFamiliarHolder familiarHolder) {
        PatchouliBuilder builder = new PatchouliBuilder(FAMILIARS, "entity.ars_elemental." + familiarHolder.getId() + "_familiar")
                .withIcon("ars_nouveau:familiar_" + familiarHolder.getId())
                .withTextPage("ars_nouveau.familiar_desc." + familiarHolder.getId())
                .withPage(new EntityPage(prefix(familiarHolder.getEntityKey() + "_familiar").toString()));
        this.pages.add(new PatchouliPage(builder, getPath(FAMILIARS, familiarHolder.getId())));
    }

    public void addRitualPage(AbstractRitual ritual) {
        PatchouliBuilder builder = new PatchouliBuilder(RITUALS, "item.ars_nouveau.ritual_" + ritual.getID())
                .withIcon("ars_nouveau:ritual_" + ritual.getID())
                .withTextPage("ars_nouveau.ritual_desc." + ritual.getID())
                .withPage(new CraftingPage("ars_elemental:ritual_" + ritual.getID()));

        this.pages.add(new PatchouliPage(builder, getPath(RITUALS, ritual.getID())));
    }

    public void addEnchantmentPage(Enchantment enchantment) {
        PatchouliBuilder builder = new PatchouliBuilder(ENCHANTMENTS, enchantment.getDescriptionId())
                .withIcon(getRegistryName(Items.ENCHANTED_BOOK).toString())
                .withTextPage("ars_elemental.enchantment_desc." + getRegistryName(enchantment).getPath());

        for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); i++) {
            builder.withPage(new EnchantingPage("ars_nouveau:" + getRegistryName(enchantment).getPath() + "_" + i));
        }
        this.pages.add(new PatchouliPage(builder, getPath(ENCHANTMENTS, getRegistryName(enchantment).getPath())));
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "Ars Elemental Patchouli Datagen";
    }

    @Override
    public Path getPath(ResourceLocation category, String fileName) {
        return this.generator.getOutputFolder().resolve("data/ars_elemental/patchouli_books/elemental_notes/en_us/entries/" + category.getPath() + "/" + fileName + ".json");
    }

    ImbuementPage ImbuementPage(ItemLike item) {
        return new ImbuementPage("ars_elemental:imbuement_" + getRegistryName(item.asItem()).getPath());
    }

    private ResourceLocation getRegistryName(Item asItem) {
        return ForgeRegistries.ITEMS.getKey(asItem);
    }

    private ResourceLocation getRegistryName(Enchantment enchantment) {
        return ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
    }

}
