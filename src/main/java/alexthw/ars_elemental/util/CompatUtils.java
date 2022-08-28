package alexthw.ars_elemental.util;

import net.minecraftforge.fml.ModList;

public class CompatUtils {
    static boolean botania = false;

    public static boolean isBotaniaLoaded() {
        return botania;
    }


    public static void checkCompats() {

        ModList modList = ModList.get();

        botania = modList.isLoaded("botania");

    }

}
