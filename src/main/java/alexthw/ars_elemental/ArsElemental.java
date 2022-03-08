package alexthw.ars_elemental;

import alexthw.ars_elemental.client.ClientEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.SlotTypeMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ArsElemental.MODID)
public class ArsElemental
{
    public static ForgeConfigSpec SERVER_CONFIG;
    public static final ResourceLocation FOCUS_SLOT = prefix("gui/an_focus_slot");
    public static final String MODID = "ars_elemental";
    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return ModRegistry.FIRE_FOCUS.get().getDefaultInstance();
        }
    };

    private static final Logger LOGGER = LogManager.getLogger();

    public ArsElemental() {

        GeckoLib.initialize();
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        ModRegistry.registerRegistries(modbus);
        ArsNouveauRegistry.registerGlyphs();
        ArsNouveauRegistry.registerFamiliars();
        ArsNouveauRegistry.addCustomThings();
        GlyphConfigs.registerGlyphConfigs();
        modbus.addListener(this::setup);
        modbus.addListener(this::doClientStuff);
        modbus.addListener(this::sendImc);

        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
            return new Object();
        });


    }

    public static ResourceLocation prefix(String path){
        return new ResourceLocation(MODID,path);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }
    public void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("bundle").size(1).build());
        InterModComms.sendTo("curios", SlotTypeMessage.MODIFY_TYPE, () -> new SlotTypeMessage.Builder("an_focus").size(1).icon(FOCUS_SLOT).cosmetic().build());
    }

}
