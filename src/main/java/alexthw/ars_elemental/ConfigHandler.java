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

        public final ForgeConfigSpec.ConfigValue<Boolean> EnableGlyphEmpowering;
        public final ForgeConfigSpec.ConfigValue<Boolean> EnableRegenBonus;

        public static ForgeConfigSpec.BooleanValue HOMING_GLOWING;
        public static ForgeConfigSpec.BooleanValue FIRENANDO_KILL;

        public static ForgeConfigSpec.IntValue SIREN_MANA_COST;
        public static ForgeConfigSpec.IntValue SIREN_MAX_PROGRESS;
        public static ForgeConfigSpec.IntValue SIREN_BASE_ITEM;
        public static ForgeConfigSpec.IntValue SIREN_UNIQUE_BONUS;
        public static ForgeConfigSpec.DoubleValue SIREN_TREASURE_BONUS;
        public static ForgeConfigSpec.IntValue SIREN_QUANTITY_CAP;

        public static ForgeConfigSpec.IntValue SQUIRREL_REFRESH_RATE;

        public static final Integer SIREN_WEIGHT = 2;
        public static ForgeConfigSpec.IntValue MAGES_WEIGHT;
        public static ForgeConfigSpec.BooleanValue MAGES_AGGRO;

        public static final Integer TREE_SPAWN_RATE = 200;


        public Common(ForgeConfigSpec.Builder builder) {

            FocusDiscount = builder.comment("Adjust this value to define how much the matching spell cost gets discounted by the focus")
                    .defineInRange("Elemental Focus discount", 0.15D, 0.0D, 0.99D);

            builder.comment("Adjust these values to balance how much a spell gets amplified by the matching spell focus, doubled for major foci.")
                    .push("Elemental Spell Foci - Amplify");

            FireMasteryBuff = builder.define("Fire Focus buff", 1.0D);
            WaterMasteryBuff = builder.define("Water Focus buff", 1.0D);
            AirMasteryBuff = builder.define("Air Focus buff", 1.0D);
            EarthMasteryBuff = builder.define("Earth Focus buff", 1.0D);

            builder.pop();

            builder.comment("Adjust these values to balance how much an elemental spell gets dampened by a not-matching lesser spell focus")
                    .push("Elemental Spell Foci - Dampening");
            FireMasteryDebuff = builder.define("Fire Focus debuff", -1.0D);
            WaterMasteryDebuff = builder.define("Water Focus debuff", -1.0D);
            AirMasteryDebuff = builder.define("Air Focus debuff", -1.0D);
            EarthMasteryDebuff = builder.define("Earth Focus debuff", -1.0D);

            builder.pop();

            builder.comment("Enable or disable the passive bonus of the foci").push("Elemental Spell Foci - Abilities");

            EnableGlyphEmpowering = builder.define("Enable glyph empowering", true);
            EnableRegenBonus = builder.define("Enable regen bonus under special conditions", true);

            builder.pop();

            builder.push("Mermaid Fishing");
            SIREN_MANA_COST = builder.comment("How much source mermaids consume per generation").defineInRange("mermaidManaCost", 1000, 0, 10000);
            SIREN_MAX_PROGRESS = builder.comment("How many channels must occur before a siren produces loot.").defineInRange("mermaidMaxProgress", 30, 0, 300);
            SIREN_UNIQUE_BONUS = builder.comment("Max number of extra item rolls a shrine produces if the mood is high.").defineInRange("mermaidScoreBonus", 2, 0, 10);
            SIREN_TREASURE_BONUS = builder.comment("Chance multiplier to produce a treasure relative to the siren shrine score.").defineInRange("mermaidTreasureBonus", 0.002D, 0D, 1D);
            SIREN_BASE_ITEM = builder.comment("Base number of items rolls a shrine produces per cycle.").defineInRange("mermaidBaseItems", 1, 0, 300);
            SIREN_QUANTITY_CAP = builder.comment("Max number of items a siren shrine can produce per cycle.").defineInRange("mermaidQuantityCap", 5, 0, 300);
            builder.pop();

            builder.push("Spawn and Worldgen");

            MAGES_WEIGHT = builder.comment("How often mages spawn").defineInRange("magesSpawnWeight", 0, 0, 200);
            MAGES_AGGRO = builder.comment("If true, the wandering mages will target players too, unless they wear the focus of the same school.").define("magesAggro", true);

            builder.pop();

            builder.push("Misc");
            FIRENANDO_KILL = builder.comment("If enabled, flarecannons will simply die and drop the charm, instead of deactivating, if killed by their owner").define("flarecannon_owner_kill", false);
            HOMING_GLOWING = builder.comment("If enabled, homing will be able to target mobs only if they're glowing").define("homing_nerf", false);
            SQUIRREL_REFRESH_RATE = builder.comment("Define the refresh rate of the Squirrel Ritual buff, in ticks.").defineInRange("squirrelRefreshRate", 600, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public static class Client{
        public static ForgeConfigSpec.ConfigValue<Boolean> EnableSFRendering;

        public Client(ForgeConfigSpec.Builder builder){
            builder.push("Visual Configs");

            EnableSFRendering = builder.comment("Enables the rendering of the spell focus while equipped").define("Enable SpellFocusRender", true);

            builder.pop();
        }
    }

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {

        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        final Pair<Client,ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specClientPair.getRight();
        CLIENT = specClientPair.getLeft();

    }

}
