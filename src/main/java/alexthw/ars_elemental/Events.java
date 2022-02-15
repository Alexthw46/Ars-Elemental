package alexthw.ars_elemental;

import alexthw.ars_elemental.entity.AllyVhexEntity;
import alexthw.ars_elemental.entity.SummonDirewolf;
import alexthw.ars_elemental.item.ElementalFocus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class Events {

    public static void registerEntities(final EntityAttributeCreationEvent event) {
        event.put(ModRegistry.SKELEHORSE_SUMMON.get(), AbstractHorseEntity.createBaseHorseAttributes().build());
        event.put(ModRegistry.DIREWOLF_SUMMON.get(), SummonDirewolf.createAttributes().build());
        event.put(ModRegistry.VHEX_SUMMON.get(), AllyVhexEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void bypassRes(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof PlayerEntity && event.getEntity() instanceof LivingEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
            ElementalFocus focus = ElementalFocus.hasFocus(event.getEntity().level, player);
            if (focus != null) {
                switch (focus.getSchool().getId()) {
                    case "fire": {
                        if (event.getSource().isFire() && event.getEntity().fireImmune()) {
                            event.setCanceled(true);
                            event.getEntity().hurt(DamageSource.playerAttack(player), event.getAmount());
                        }
                        break;
                    }
                    case "air": {
                        if (event.getEntity().invulnerableTime > 0 && event.getSource().isMagic()) {
                            event.getEntity().invulnerableTime = 0;
                        }
                    }
                }
            }
        }

    }
}