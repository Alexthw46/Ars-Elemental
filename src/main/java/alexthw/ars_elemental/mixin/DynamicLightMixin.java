package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.common.light.DynamLightUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DynamLightUtil.class)
public class DynamicLightMixin {

    @Inject(method = "lightForEntity", at = @At("TAIL"), cancellable = true, remap = false)
    private static void ars_elemental$lightForEntity(Entity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof LivingEntity l && l.hasEffect(ModPotions.MAGIC_FIRE.get())) {
            cir.setReturnValue(15);
        }
    }

}
