package alexthw.ars_elemental.util;

import net.minecraftforge.fml.ModList;

public class CompatUtils {

    static boolean enoughGlyphs = false;
    static boolean botania = false;

    public static boolean tooManyGlyphsLoaded() {
        return enoughGlyphs;
    }

    public static boolean isBotaniaLoaded() {
        return botania;
    }


    public static void checkCompats() {

        ModList modList = ModList.get();

        enoughGlyphs = modList.isLoaded("toomanyglyphs");
        botania = modList.isLoaded("botania");

    }

}
