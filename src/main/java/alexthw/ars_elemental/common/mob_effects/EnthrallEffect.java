package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.util.EntityCarryMEI;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public class EnthrallEffect extends MobEffect {

    public EnthrallEffect() {
        super(MobEffectCategory.NEUTRAL, 0);
        MinecraftForge.EVENT_BUS.addListener(this::onTarget);
    }

    public void onTarget(LivingSetAttackTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;
        if (event.getEntityLiving() instanceof Mob thrall && isEnthralledBy(thrall, player)) {
            if (player.getLastHurtMob() != null && player.getLastHurtMob() != thrall){
                thrall.setTarget(player.getLastHurtMob());
            }else if (player.getLastHurtByMob() != null && player.getLastHurtByMob() != thrall){
                thrall.setTarget(player.getLastHurtByMob());
            }else {
                thrall.setTarget(null);
            }
        }
    }

    private boolean isEnthralledBy(LivingEntity entity, Player player) {
        if (entity.hasEffect(this)){
            EntityCarryMEI instance = (EntityCarryMEI) entity.getEffect(this);
            if (instance != null) return instance.getOwner() == player;
        }
        return false;
    }

}
