package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.util.EntityCarryMEI;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

import java.util.ArrayList;
import java.util.List;

public class EnthrallEffect extends MobEffect {

    public EnthrallEffect() {
        super(MobEffectCategory.NEUTRAL, 0);
        MinecraftForge.EVENT_BUS.addListener(this::onTarget);
    }

    public void onTarget(LivingSetAttackTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;
        if (event.getEntityLiving() instanceof Mob thrall && isEnthralledBy(thrall, player)) {
            if (player.getLastHurtMob() != null && player.getLastHurtMob() != thrall) {
                thrall.setTarget(player.getLastHurtMob());
                if (thrall instanceof NeutralMob angry)
                    angry.setPersistentAngerTarget(player.getLastHurtMob().getUUID());
            } else if (player.getLastHurtByMob() != null && player.getLastHurtByMob() != thrall) {
                thrall.setTarget(player.getLastHurtByMob());
                if (thrall instanceof NeutralMob angry)
                    angry.setPersistentAngerTarget(player.getLastHurtByMob().getUUID());
            } else {
                thrall.setTarget(null);
                if (thrall instanceof NeutralMob angry) angry.setRemainingPersistentAngerTime(0);
            }
        }
    }

    private boolean isEnthralledBy(LivingEntity entity, Player player) {
        if (entity.hasEffect(this)) {
            MobEffectInstance instance = entity.getEffect(this);
            if (instance instanceof EntityCarryMEI mei) return mei.getOwner() == player;
        }
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
