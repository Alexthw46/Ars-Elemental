package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectFlare.class)
public class FlareMixin {

    @Inject(method = "canDamage", remap = false, at = @At("TAIL"), cancellable = true)
    public void ars_elemental$canDamage(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValueZ() || livingEntity.hasEffect(ModPotions.MAGIC_FIRE.get()));
    }

}
