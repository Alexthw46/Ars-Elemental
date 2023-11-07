package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.entity.DripstoneSpikeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.ars_nouveau.geckolib3.model.AnimatedGeoModel;
import software.bernie.ars_nouveau.geckolib3.renderers.geo.GeoProjectilesRenderer;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SpikeRenderer extends GeoProjectilesRenderer<DripstoneSpikeEntity> {
    public SpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DripGeoModel());
    }

    public SpikeRenderer(EntityRendererProvider.Context renderManager, ResourceLocation textureLocation) {
        super(renderManager, new DripGeoModel(textureLocation));
    }

    @Override
    public float getWidthScale(DripstoneSpikeEntity animatable) {
        return (float) animatable.getAoe();
    }

    @Override
    public float getHeightScale(DripstoneSpikeEntity entity) {
        return (float) (entity.getPierce() + entity.getAoe() - 1);
    }

    private static class DripGeoModel extends AnimatedGeoModel<DripstoneSpikeEntity> {

        public DripGeoModel() {
            super();
        }

        public DripGeoModel(ResourceLocation textureLocation) {
            super();
            AltTexture = textureLocation;
        }

        ResourceLocation AltTexture;
        static final ResourceLocation MODEL = prefix("geo/spike.geo.json");
        static final ResourceLocation TEXTURE = prefix("textures/entity/spike.png");
        static final ResourceLocation ANIMATIONS = prefix("animations/spike.animation.json");

        @Override
        public ResourceLocation getModelResource(DripstoneSpikeEntity dripstoneSpikeEntity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(DripstoneSpikeEntity dripstoneSpikeEntity) {
            return AltTexture != null ? AltTexture : TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(DripstoneSpikeEntity dripstoneSpikeEntity) {
            return ANIMATIONS;
        }
    }
}
