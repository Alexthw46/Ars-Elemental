package alexthw.ars_elemental;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArsElemental.MODID)
public class ArsElemental
{

    public static final String MODID = "ars_elemental";

    private static final Logger LOGGER = LogManager.getLogger();

    public static ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModRegistry.FIRE_FOCUS.get());
        }
    };


    public ArsElemental() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        ModRegistry.registerRegistries(modbus);
        ArsNouveauRegistry.registerGlyphs();
        //ExampleConfig.registerGlyphConfigs();
        modbus.addListener(Events::registerEntities);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

}
