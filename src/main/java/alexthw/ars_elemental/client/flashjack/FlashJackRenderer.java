package alexthw.ars_elemental.client.flashjack;

import alexthw.ars_elemental.common.entity.FlashjackEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlashJackRenderer<M extends LivingEntity & GeoEntity> extends GeoEntityRenderer<M> {


    public FlashJackRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FlashJackModel<>());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull M animatable) {
        return animatable instanceof FlashjackEntity f ? f.getTexture() : super.getTextureLocation(animatable);
    }
}
