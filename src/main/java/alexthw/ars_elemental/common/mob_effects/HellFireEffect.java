package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.ModRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import net.minecraftforge.event.entity.living.LivingHealEvent;

public class HellFireEffect extends MobEffect implements IForgeMobEffect {

    public HellFireEffect() {
        super(MobEffectCategory.HARMFUL, 14981690);
        MinecraftForge.EVENT_BUS.addListener(this::burn);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.isInWaterOrRain()) pLivingEntity.removeEffect(this);
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    public void burn(LivingHealEvent event){
        if (event.getEntityLiving().hasEffect(ModRegistry.HELLFIRE.get())){
            MobEffectInstance inst = event.getEntityLiving().getEffect(ModRegistry.HELLFIRE.get());
            if (inst == null) return;
            event.setAmount(event.getAmount()/inst.getAmplifier());
        }
    }
}
