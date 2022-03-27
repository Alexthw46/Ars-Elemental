package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.util.EntityCarryMEI;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LifeLinkEffect extends MobEffect {

    public LifeLinkEffect(){
        super(MobEffectCategory.NEUTRAL,1);
        MinecraftForge.EVENT_BUS.addListener(this::healForHeal);
        MinecraftForge.EVENT_BUS.addListener(this::hurtForHurt);
    }

    public void healForHeal(LivingHealEvent event){
        if (event.getEntityLiving() != null && event.getEntityLiving().hasEffect(this)){
            MobEffectInstance instance = event.getEntityLiving().getEffect(this);
            if (instance instanceof EntityCarryMEI mei && mei.getTarget() == event.getEntityLiving()) {
                if (mei.getOwner().isAlive()) {
                    int shared = (int) (event.getAmount() / 2);
                    mei.getOwner().heal(shared);
                    event.setAmount(shared);
                }else {
                    event.getEntityLiving().removeEffect(this);
                }
            }
        }
    }

    public void hurtForHurt(LivingHurtEvent event){
        if (event.getEntityLiving() != null && event.getEntityLiving().hasEffect(this)){
            EntityCarryMEI instance = (EntityCarryMEI) event.getEntityLiving().getEffect(this);
            if (instance != null && instance.getOwner() == event.getEntityLiving()) {
                if (instance.getTarget().isAlive()) {
                    int shared = (int) (event.getAmount() / 2);
                    instance.getTarget().hurt(event.getSource(), shared);
                    event.setAmount(shared);
                }else{
                    event.getEntityLiving().removeEffect(this);
                }
            }
        }
    }

}
