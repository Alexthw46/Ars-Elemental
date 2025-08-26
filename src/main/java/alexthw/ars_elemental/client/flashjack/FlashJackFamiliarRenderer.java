package alexthw.ars_elemental.client.flashjack;

import alexthw.ars_elemental.common.entity.familiars.FlashjackFamiliar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlashJackFamiliarRenderer<M extends FlashjackFamiliar> extends FlashJackRenderer<M> {

    public FlashJackFamiliarRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull M animatable) {
        return animatable instanceof FlashjackFamiliar f ? f.getTexture() : super.getTextureLocation(animatable);
    }

}
