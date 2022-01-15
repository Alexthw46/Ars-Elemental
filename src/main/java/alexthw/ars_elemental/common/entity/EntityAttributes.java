package alexthw.ars_elemental.common.entity;


import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModRegistry.SIREN_ENTITY.get(), MermaidEntity.createAttributes());
        event.put(ModRegistry.SIREN_FAMILIAR.get(), FamiliarEntity.attributes().build());
        event.put(ModRegistry.SKELEHORSE_SUMMON.get(), AbstractHorse.createBaseHorseAttributes().build());
        event.put(ModRegistry.DIREWOLF_SUMMON.get(), SummonDirewolf.createAttributes().build());
        event.put(ModRegistry.VHEX_SUMMON.get(), AllyVhexEntity.createAttributes().build());
    }
}
