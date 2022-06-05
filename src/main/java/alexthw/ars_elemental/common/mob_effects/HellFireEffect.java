package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import net.minecraftforge.event.entity.living.LivingHealEvent;

import java.util.ArrayList;
import java.util.List;

public class HellFireEffect extends MobEffect implements IForgeMobEffect {

    public HellFireEffect() {
        super(MobEffectCategory.HARMFUL, 14981690);
        MinecraftForge.EVENT_BUS.addListener(this::burn);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    public void burn(LivingHealEvent event){
        if (event.getEntityLiving().hasEffect(ModRegistry.HELLFIRE.get())){
            MobEffectInstance inst = event.getEntityLiving().getEffect(ModRegistry.HELLFIRE.get());
            if (inst == null) return;
            int amplifier = Math.max(2, inst.getAmplifier());
            event.setAmount(event.getAmount() / amplifier);
            event.getEntityLiving().invulnerableTime = 0;
        }
    }
}
