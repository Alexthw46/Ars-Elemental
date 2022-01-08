package alexthw.ars_elemental;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {

    public static class Common {

        public final ForgeConfigSpec.ConfigValue<Double> FocusDiscount;

        public final ForgeConfigSpec.ConfigValue<Double> FireMasteryBuff;
        public final ForgeConfigSpec.ConfigValue<Double> WaterMasteryBuff;
        public final ForgeConfigSpec.ConfigValue<Double> AirMasteryBuff;
        public final ForgeConfigSpec.ConfigValue<Double> EarthMasteryBuff;

        public final ForgeConfigSpec.ConfigValue<Double> FireMasteryDebuff;
        public final ForgeConfigSpec.ConfigValue<Double> WaterMasteryDebuff;
        public final ForgeConfigSpec.ConfigValue<Double> AirMasteryDebuff;
        public final ForgeConfigSpec.ConfigValue<Double> EarthMasteryDebuff;

        public Common(ForgeConfigSpec.Builder builder) {

            FocusDiscount = builder.comment("Adjust this value to define how much the matching spell cost gets discounted by the focus")
                    .defineInRange("Elemental Focus discount", 0.2D, 0.0D, 0.99D);

            builder.comment("Adjust these values to balance how much a spell gets amplified by the matching spell focus")
                    .push("Elemental Spell Foci - Amplify");

            FireMasteryBuff = builder.define("Fire Focus buff", 2.0D);
            WaterMasteryBuff = builder.define("Water Focus buff", 2.0D);
            AirMasteryBuff = builder.define("Air Focus buff", 2.0D);
            EarthMasteryBuff = builder.define("Earth Focus buff", 2.0D);

            builder.pop();

            builder.comment("Adjust these values to balance how much an elemental spell gets dampened by a not-matching spell focus")
                    .push("Elemental Spell Foci - Dampening");
            FireMasteryDebuff = builder.define("Fire Focus debuff", -1.0D);
            WaterMasteryDebuff = builder.define("Water Focus debuff", -1.0D);
            AirMasteryDebuff = builder.define("Air Focus debuff", -1.0D);
            EarthMasteryDebuff = builder.define("Earth Focus debuff", -1.0D);

            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

}
