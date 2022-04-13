package alexthw.ars_elemental.util;

import net.minecraftforge.fml.ModList;

public class CompatUtils {

    static boolean enoughGlyphs = false;
    static boolean botania = false;
    static boolean arsenal = false;

    public static boolean tooManyGlyphsLoaded() {
        return enoughGlyphs;
    }

    public static boolean isBotaniaLoaded() {
        return botania;
    }

    public static boolean isArsenalLoaded() {
        return arsenal;
    }

    public static void checkCompats() {

        ModList modList = ModList.get();

        enoughGlyphs = modList.isLoaded("toomanyglyphs");
        arsenal = modList.isLoaded("arsarsenal");
        botania = modList.isLoaded("botania");

    }

}
