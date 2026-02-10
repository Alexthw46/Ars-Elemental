package alexthw.ars_elemental.util;

import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.function.Predicate;

import static com.alexthw.sauce.api.item.ISchoolFocus.getFociSchools;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_AIR;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_FIRE;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_WATER;

public class CompatUtils {
    static boolean botania = false;
    static boolean creo = false;
    static boolean SSR = false;

    public static boolean isBotaniaLoaded() {
        return botania;
    }

    public static boolean isCreoLoaded() {
        return creo;
    }

    public static boolean isSummonRework() {
        return SSR;
    }

    public static void checkCompats() {

        ModList modList = ModList.get();

        botania = modList.isLoaded("botania");
        creo = modList.isLoaded("ars_creo");
        IModFileInfo info = modList.getModFileById("ars_nouveau");
        String version = info.versionString();

        // examples
        if (version.contains("SSR")) {
            SSR = true;
        }
    }

    public static SlotResult getCurio(LivingEntity player, Predicate<ItemStack> predicate) {
        var lazy = CuriosApi.getCuriosInventory(player);
        SlotResult noResult = new SlotResult(null, ItemStack.EMPTY);
        if (lazy.isPresent()) {
            var curioInv = lazy.get();
            return curioInv.findFirstCurio(predicate).orElse(noResult);
        }
        return noResult;
    }

    public static List<SlotResult> getCurios(LivingEntity player, Predicate<ItemStack> predicate) {
        var lazy = CuriosApi.getCuriosInventory(player);
        List<SlotResult> noResult = NonNullList.withSize(2, new SlotResult(null, ItemStack.EMPTY));
        if (lazy.isPresent()) {
            var curioInv = lazy.get();
            return curioInv.findCurios(predicate);
        }
        return noResult;
    }

    public static boolean fireCheck(SpellResolver resolver) {
        return resolver.hasFocus(ModItems.LESSER_FIRE_FOCUS.get()) || getFociSchools(resolver.spellContext.getUnwrappedCaster()).contains(ELEMENTAL_FIRE);
    }

    public static boolean waterCheck(SpellResolver resolver) {
        return resolver.hasFocus(ModItems.LESSER_WATER_FOCUS.get()) || getFociSchools(resolver.spellContext.getUnwrappedCaster()).contains(ELEMENTAL_WATER);
    }

    public static boolean earthCheck(SpellResolver resolver) {
        return resolver.hasFocus(ModItems.LESSER_EARTH_FOCUS.get()) || getFociSchools(resolver.spellContext.getUnwrappedCaster()).contains(ELEMENTAL_EARTH);
    }

    public static boolean airCheck(SpellResolver resolver) {
        return resolver.hasFocus(ModItems.LESSER_AIR_FOCUS.get()) || getFociSchools(resolver.spellContext.getUnwrappedCaster()).contains(ELEMENTAL_AIR);
    }

}
