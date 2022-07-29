package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectFlare.class)
public class FlareMixin {

    @Redirect(method = "onResolveEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isOnFire()Z" ))
    public boolean isOnFire(LivingEntity instance) {
        return instance.hasEffect(ModPotions.HELLFIRE.get()) || (instance.isOnFire() && !instance.isInWaterOrBubble());
    }

}
