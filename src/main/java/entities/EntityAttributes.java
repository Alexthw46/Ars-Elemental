package entities;


import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModRegistry.SIREN_ENTITY.get(), MermaidEntity.createAttributes());
        event.put(ModRegistry.SIREN_FAMILIAR.get(), FamiliarEntity.attributes().build());

    }
}
