package alexthw.ars_elemental;

import alexthw.ars_elemental.client.ClientEvents;
import alexthw.ars_elemental.client.SpellFocusRenderer;
import alexthw.ars_elemental.registry.ModAdvTriggers;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModPotions;
import alexthw.ars_elemental.registry.ModRegistry;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.world.TerrablenderAE;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ArsElemental.MODID)
public class ArsElemental {

    public static final ResourceLocation FOCUS_SLOT = new ResourceLocation("curios:slot/an_focus_slot");
    public static final ResourceLocation BANGLE_SLOT = new ResourceLocation("curios:slot/bangle_slot");

    public static final String MODID = "ars_elemental";
    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return ModItems.DEBUG_ICON.get().getDefaultInstance();
        }
    };

    public static final UUID Dev = UUID.fromString("0e918660-22bf-4bed-8426-ece3b4bbd01d");
    public static boolean terrablenderLoaded = false;

    public ArsElemental() {
        terrablenderLoaded = ModList.get().isLoaded("terrablender");

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
        ModRegistry.registerRegistries(modbus);
        ArsNouveauRegistry.init();
        modbus.addListener(this::setup);
        modbus.addListener(this::sendImc);
        modbus.addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.register(this);
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
            modbus.addListener(this::doClientStuff);
            return new Object();
        });
        ModAdvTriggers.init();
        ArsNouveauAPI.ENABLE_DEBUG_NUMBERS = !FMLEnvironment.production;
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path);
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModPotions.addPotionRecipes();
            ArsNouveauRegistry.postInit();
            CompatUtils.checkCompats();
        });
        if (terrablenderLoaded && ConfigHandler.Common.EXTRA_BIOMES.get() > 0) {
            event.enqueueWork(TerrablenderAE::registerBiomes);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {
        if (!ConfigHandler.Client.EnableSFRendering.get()) return;
        CuriosRendererRegistry.register(ModItems.FIRE_FOCUS.get(), SpellFocusRenderer::new);
        CuriosRendererRegistry.register(ModItems.WATER_FOCUS.get(), SpellFocusRenderer::new);
        CuriosRendererRegistry.register(ModItems.AIR_FOCUS.get(), SpellFocusRenderer::new);
        CuriosRendererRegistry.register(ModItems.EARTH_FOCUS.get(), SpellFocusRenderer::new);
        CuriosRendererRegistry.register(ModItems.NECRO_FOCUS.get(), SpellFocusRenderer::new);
    }

    public void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("bundle").size(1).icon(new ResourceLocation(Curios.MODID, "slot/empty_curio_slot")).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("bangle").size(1).icon(BANGLE_SLOT).build());
        InterModComms.sendTo("curios", SlotTypeMessage.MODIFY_TYPE, () -> new SlotTypeMessage.Builder("an_focus").size(1).icon(FOCUS_SLOT).build());
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.putIfAbsent(ModItems.FLASHING_SAPLING.get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.putIfAbsent(ModItems.FLASHING_POD.get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.putIfAbsent(ModItems.FLASHING_LEAVES.get().asItem(), 0.3f);

            FlowerPotBlock potBlock = (FlowerPotBlock) Blocks.FLOWER_POT;

            potBlock.addPlant(prefix("yellow_archwood_sapling"), ModItems.POT_FLASHING_SAPLING);
        });
    }
}
