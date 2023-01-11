package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.common.entity.Starbuncle;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.ars_nouveau.geckolib3.core.PlayState;
import software.bernie.ars_nouveau.geckolib3.core.builder.AnimationBuilder;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;

@Mixin(value = Starbuncle.class, remap = false)
public abstract class StarbyDancerMixin {

    @Shadow
    public abstract boolean isTamed();

    @Shadow
    public abstract ItemStack getHeldStack();

    @Shadow public abstract ItemStack getCosmeticItem();

    @Inject(method = "dancePredicate", at = @At("TAIL"), cancellable = true)
    void dancePredicate(AnimationEvent<?> event, CallbackInfoReturnable<PlayState> cir) {
        if (cir.getReturnValue() == PlayState.STOP && !isTamed() && (this.getHeldStack().getItem() == BlockRegistry.LIGHT_BLOCK.asItem() || this.getCosmeticItem().getItem() == BlockRegistry.LIGHT_BLOCK.asItem())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dance_master"));
            cir.setReturnValue(PlayState.CONTINUE);
        }
    }
}
