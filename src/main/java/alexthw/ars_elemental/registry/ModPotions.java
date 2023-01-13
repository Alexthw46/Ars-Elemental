package alexthw.ars_elemental.registry;

import alexthw.ars_elemental.common.mob_effects.*;
import alexthw.ars_elemental.mixin.PotionBrewingMixin;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static alexthw.ars_elemental.ArsElemental.MODID;
import static com.hollingsworth.arsnouveau.common.lib.LibPotions.longPotion;
import static com.hollingsworth.arsnouveau.common.lib.LibPotions.potion;

public class ModPotions {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, MODID);

    public static final RegistryObject<MobEffect> HELLFIRE;
    public static final RegistryObject<MobEffect> WATER_GRAVE;
    public static final RegistryObject<MobEffect> ENTHRALLED;
    public static final RegistryObject<MobEffect> HYMN_OF_ORDER;
    public static final RegistryObject<LifeLinkEffect> LIFE_LINK;
    public static final RegistryObject<EnderferenceEffect> ENDERFERENCE;
    public static final RegistryObject<LightningLureEffect> LIGHTNING_LURE;
    public static final RegistryObject<RepelEffect> REPEL;


    public static final RegistryObject<Potion> ENDERFERENCE_POTION;
    public static final RegistryObject<Potion> LONG_ENDERFERENCE_POTION;

    public static final RegistryObject<Potion> SHOCK_POTION;
    public static final RegistryObject<Potion> LONG_SHOCK_POTION;


    public static void addPotionRecipes() {
        ItemStack AWKWARD = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);

        ItemStack enderPot = PotionUtils.setPotion(new ItemStack(Items.POTION), ENDERFERENCE_POTION.get());
        ItemStack enderPotLong = PotionUtils.setPotion(new ItemStack(Items.POTION), LONG_ENDERFERENCE_POTION.get());

        ItemStack shockPot = PotionUtils.setPotion(new ItemStack(Items.POTION), SHOCK_POTION.get());
        ItemStack shockPotLong = PotionUtils.setPotion(new ItemStack(Items.POTION), LONG_SHOCK_POTION.get());

        PotionBrewingMixin invoker = (PotionBrewingMixin) new PotionBrewing();

        invoker.callAddMix(Potions.AWKWARD, ItemsRegistry.END_FIBER.asItem(), ENDERFERENCE_POTION.get());
        invoker.callAddMix(ENDERFERENCE_POTION.get(), Items.GLOWSTONE_DUST, LONG_ENDERFERENCE_POTION.get());


        invoker.callAddMix(Potions.AWKWARD, ModItems.FLASHING_POD.get().asItem(), SHOCK_POTION.get());
        invoker.callAddMix(SHOCK_POTION.get(), Items.GLOWSTONE_DUST, LONG_SHOCK_POTION.get());

    }

    static {
        HELLFIRE = EFFECTS.register("hellfire", HellFireEffect::new);
        WATER_GRAVE = EFFECTS.register("watery_grave", WaterGraveEffect::new);
        ENTHRALLED = EFFECTS.register("enthralled", EnthrallEffect::new);
        LIFE_LINK = EFFECTS.register("life_link", LifeLinkEffect::new);
        HYMN_OF_ORDER = EFFECTS.register("hymn_of_order", OrderEffect::new);
        ENDERFERENCE = EFFECTS.register("enderference", EnderferenceEffect::new);
        LIGHTNING_LURE = EFFECTS.register("static_charged", LightningLureEffect::new);
        REPEL = EFFECTS.register("repel", RepelEffect::new);

        ENDERFERENCE_POTION = POTIONS.register(potion("enderference"), () -> new Potion(new MobEffectInstance(ENDERFERENCE.get(), 400)));
        LONG_ENDERFERENCE_POTION = POTIONS.register(longPotion("enderference"), () -> new Potion(new MobEffectInstance(ENDERFERENCE.get(), 1200)));
        SHOCK_POTION = POTIONS.register(potion("shock"), () -> new Potion(new MobEffectInstance(LIGHTNING_LURE.get(), 600)));
        LONG_SHOCK_POTION = POTIONS.register(longPotion("shock"), () -> new Potion(new MobEffectInstance(LIGHTNING_LURE.get(), 400)));

    }

}
