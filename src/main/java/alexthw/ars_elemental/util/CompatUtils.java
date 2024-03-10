package alexthw.ars_elemental.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.function.Predicate;

public class CompatUtils {
    static boolean botania = false;

    public static boolean isBotaniaLoaded() {
        return botania;
    }


    public static void checkCompats() {

        ModList modList = ModList.get();

        botania = modList.isLoaded("botania");

    }

    public static SlotResult getCurio(LivingEntity player, Predicate<ItemStack> predicate) {
        var lazy = CuriosApi.getCuriosInventory(player);
        if (lazy.isPresent()) {
            var optional = lazy.resolve();
            if (optional.isPresent()) {
                var curioInv = optional.get();
                return curioInv.findFirstCurio(predicate).orElse(null);
            }
        }
        return null;
    }


}
