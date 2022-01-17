package alexthw.ars_elemental;

import alexthw.ars_elemental.common.items.ElementalFocus;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)

public class Events {

    @SubscribeEvent
    public static void bypassRes(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity living) {
            ElementalFocus focus = ElementalFocus.hasFocus(event.getEntity().level, player);
            if (focus != null) {
                switch (focus.getSchool().getId()) {
                    case "fire" -> {
                        if (event.getSource().isFire() && living.fireImmune()) {
                            event.setCanceled(true);
                            living.hurt(DamageSource.playerAttack(player), event.getAmount());
                        }
                    }
                    case "air" -> {
                        if (living.invulnerableTime > 0 && event.getSource().isMagic()) {
                            living.invulnerableTime = 0;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void boostHealing(LivingHealEvent event){
        if (event.getEntity() instanceof Player player && ElementalFocus.hasFocus(player.getLevel(), player) == ModRegistry.EARTH_FOCUS.get()) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }

}