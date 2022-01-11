package alexthw.ars_elemental;

import alexthw.ars_elemental.entity.AllyVhexEntity;
import alexthw.ars_elemental.entity.SummonDirewolf;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class Events {
    @SubscribeEvent
    public static void registerEntities(final EntityAttributeCreationEvent event) {
        event.put(ModRegistry.SKELEHORSE_SUMMON.get(), AbstractHorseEntity.createBaseHorseAttributes().build());
        event.put(ModRegistry.DIREWOLF_SUMMON.get(), SummonDirewolf.createAttributes().build());
        event.put(ModRegistry.VHEX_SUMMON.get(), AllyVhexEntity.createAttributes().build());
    }
}