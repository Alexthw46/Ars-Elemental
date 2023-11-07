package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.entity.DripstoneSpikeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SpikeRenderer extends GeoEntityRenderer<DripstoneSpikeEntity> {
    public SpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DripGeoModel());
    }

    public SpikeRenderer(EntityRendererProvider.Context renderManager, ResourceLocation textureLocation) {
        super(renderManager, new DripGeoModel(textureLocation));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, DripstoneSpikeEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        widthScale = (float) animatable.getAoe();
        heightScale = (float) (animatable.getPierce() + animatable.getAoe() - 1);
        super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    private static class DripGeoModel extends GeoModel<DripstoneSpikeEntity> {

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
