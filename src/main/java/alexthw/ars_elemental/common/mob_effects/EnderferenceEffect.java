package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsNouveau.MODID)
public class EnderferenceEffect extends MobEffect {

    public EnderferenceEffect() {
        super(MobEffectCategory.HARMFUL, 8080895);
    }

    @SubscribeEvent
    public static void enderference(EntityTeleportEvent event){
        if (event instanceof EntityTeleportEvent.TeleportCommand) return;
        if (event.getEntity() instanceof LivingEntity living){
            if (living.hasEffect(ModPotions.ENDERFERENCE.get())){
                event.setCanceled(true);
            }
        }
    }

}
