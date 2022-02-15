package alexthw.ars_elemental;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeEffect;
import net.minecraftforge.event.entity.living.LivingHealEvent;

import static alexthw.ars_elemental.datagen.Datagen.prefix;

public class HellFireEffect extends Effect implements IForgeEffect {

    protected static final ResourceLocation EFFECT_TEXTURE = prefix("textures/mob_effect/hellfire.png");

    public HellFireEffect() {
        super(EffectType.HARMFUL, 14981690);
        MinecraftForge.EVENT_BUS.addListener(this::burn);

    }

    public void burn(LivingHealEvent event){
        if (event.getEntityLiving().hasEffect(ModRegistry.HELLFIRE.get())){
            EffectInstance inst = event.getEntityLiving().getEffect(ModRegistry.HELLFIRE.get());
            if (inst == null) return;
            event.setAmount(event.getAmount()/inst.getAmplifier());
        }
    }
}
