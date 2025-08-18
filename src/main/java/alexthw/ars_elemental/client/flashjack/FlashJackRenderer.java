package alexthw.ars_elemental.client.flashjack;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlashJackRenderer<M extends LivingEntity & GeoEntity> extends GeoEntityRenderer<M> {


    public FlashJackRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FlashJackModel<>());
    }


}
