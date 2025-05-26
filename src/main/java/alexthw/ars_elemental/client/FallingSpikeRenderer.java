package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.entity.spikes.EnchantedDripstoneEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import static alexthw.ars_elemental.ArsElemental.prefix;

@OnlyIn(Dist.CLIENT)
public class FallingSpikeRenderer extends GeoEntityRenderer<EnchantedDripstoneEntity> {

    public FallingSpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FallingSpikeGeoModel());
    }

    public FallingSpikeRenderer(EntityRendererProvider.Context renderManager, ResourceLocation texture) {
        super(renderManager, new FallingSpikeGeoModel(texture));
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, EnchantedDripstoneEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        super.scaleModelForRender(0.75F, 0.75F, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    private static class FallingSpikeGeoModel extends GeoModel<EnchantedDripstoneEntity> {

        static final ResourceLocation MODEL = prefix("geo/spike.geo.json");
        static final ResourceLocation TEXTURE = prefix("textures/entity/spike.png");
        static final ResourceLocation ANIMATIONS = prefix("animations/spike.animation.json");
        ResourceLocation AltTexture;
        public FallingSpikeGeoModel() {
            super();
        }
        public FallingSpikeGeoModel(ResourceLocation textureLocation) {
            super();
            AltTexture = textureLocation;
        }

        @Override
        public ResourceLocation getModelResource(EnchantedDripstoneEntity dripstoneSpikeEntity) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(EnchantedDripstoneEntity dripstoneSpikeEntity) {
            return AltTexture != null ? AltTexture : TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(EnchantedDripstoneEntity dripstoneSpikeEntity) {
            return ANIMATIONS;
        }

        @Override
        public void setCustomAnimations(EnchantedDripstoneEntity animatable, long instanceId, AnimationState<EnchantedDripstoneEntity> animationState) {
            super.setCustomAnimations(animatable, instanceId, animationState);

            Vec3 velocity = animatable.getDeltaMovement();
            GeoBone master = this.getAnimationProcessor().getBone("master");

            if (master != null && !velocity.equals(Vec3.ZERO)) {
                Vec3 direction = velocity.normalize();

                // This assumes model points UP by default, so we want to rotate from (0, 1, 0) to direction
                float pitch = (float) Math.acos(direction.y); // angle between Y+ and the direction
                float yaw = (float) Math.atan2(direction.x, direction.z);

                // Fix pitch: angle from Y+ axis to direction
                if (direction.y < 1) pitch = -pitch;

                // Convert degrees to radians (setRotX/Y expects radians)
                master.setRotX(pitch);
                master.setRotY(yaw);
            }
        }

    }

}