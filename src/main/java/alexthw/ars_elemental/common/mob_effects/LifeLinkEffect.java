package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.util.EntityCarryMEI;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

public class LifeLinkEffect extends MobEffect {

    public LifeLinkEffect() {
        super(MobEffectCategory.NEUTRAL, 1);
        MinecraftForge.EVENT_BUS.addListener(this::healForHeal);
        MinecraftForge.EVENT_BUS.addListener(this::hurtForHurt);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    public void healForHeal(LivingHealEvent event) {
        if (event.getEntity() != null && event.getEntity().hasEffect(this)) {
            MobEffectInstance instance = event.getEntity().getEffect(this);
            if (instance instanceof EntityCarryMEI mei && mei.getTarget() == event.getEntity()) {
                if (mei.getOwner().isAlive()) {
                    int shared = (int) (event.getAmount() / 2F);
                    mei.getOwner().heal(shared);
                    event.setAmount(shared);
                } else {
                    event.getEntity().removeEffect(this);
                }
            }
        }
    }

    public void hurtForHurt(LivingHurtEvent event) {
        if (event.getEntity() != null && event.getEntity().hasEffect(this)) {
            MobEffectInstance instance = event.getEntity().getEffect(this);
            if (instance instanceof EntityCarryMEI mei && mei.getOwner() == event.getEntity()) {
                if (mei.getTarget().isAlive()) {
                    int shared = (int) (event.getAmount() / 2F);
                    mei.getTarget().hurt(event.getSource(), shared);
                    event.setAmount(shared);
                } else {
                    event.getEntity().removeEffect(this);
                }
            }
        }
    }

}
