package alexthw.ars_elemental.common;

import alexthw.ars_elemental.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import net.minecraftforge.event.entity.living.LivingHealEvent;

import static alexthw.ars_elemental.Datagen.prefix;

public class HellFireEffect extends MobEffect implements IForgeMobEffect {

    protected static final ResourceLocation EFFECT_TEXTURE = prefix("textures/mob_effect/hellfire.png");

    public HellFireEffect() {
        super(MobEffectCategory.HARMFUL, 14981690);
        MinecraftForge.EVENT_BUS.addListener(this::burn);

    }

    public void burn(LivingHealEvent event){
        if (event.getEntityLiving().hasEffect(ModRegistry.HELLFIRE.get())){
            MobEffectInstance inst = event.getEntityLiving().getEffect(ModRegistry.HELLFIRE.get());
            if (inst == null) return;
            event.setAmount(event.getAmount()/inst.getAmplifier());
        }
    }
}
