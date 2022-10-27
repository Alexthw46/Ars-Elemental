package alexthw.ars_elemental.common.entity;


import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import alexthw.ars_elemental.common.entity.summon.AllyVhexEntity;
import alexthw.ars_elemental.common.entity.summon.SummonDirewolf;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributes {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SIREN_ENTITY.get(), MermaidEntity.createAttributes());
        event.put(ModEntities.SIREN_FAMILIAR.get(), FamiliarEntity.attributes().build());

        event.put(ModEntities.FIRENANDO_ENTITY.get(), FirenandoEntity.createAttributes().build());
        event.put(ModEntities.FIRENANDO_FAMILIAR.get(), FamiliarEntity.attributes().build());

        event.put(ModEntities.FLASHING_WEALD_WALKER.get(), WealdWalker.attributes().build());
        event.put(ModEntities.SKELEHORSE_SUMMON.get(), AbstractHorse.createBaseHorseAttributes().build());
        event.put(ModEntities.DIREWOLF_SUMMON.get(), SummonDirewolf.createAttributes().build());
        event.put(ModEntities.WSKELETON_SUMMON.get(), SummonSkeleton.createAttributes().build());
        event.put(ModEntities.VHEX_SUMMON.get(), AllyVhexEntity.createAttributes().build());
        event.put(ModEntities.DOLPHIN_SUMMON.get(), Dolphin.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 1.5).build());
        event.put(ModEntities.STRIDER_SUMMON.get(), Strider.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).build());

        event.put(ModEntities.FIRE_MAGE.get(), EntityMageBase.createAttributes().build());
        event.put(ModEntities.WATER_MAGE.get(), EntityMageBase.createAttributes().build());
        event.put(ModEntities.AIR_MAGE.get(), EntityMageBase.createAttributes().build());
        event.put(ModEntities.EARTH_MAGE.get(), EntityMageBase.createAttributes().build());

    }
}
