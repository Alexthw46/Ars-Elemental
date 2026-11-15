package alexthw.ars_elemental.client.phalanx;

import alexthw.ars_elemental.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ModelProperty;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;
import com.hollingsworth.arsnouveau.api.registry.ParticlePropertyRegistry;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StyledPhalanxRender extends GeoEntityRenderer<EntityProjectileSpell> {

    ModelProperty modelProp;

    public StyledPhalanxRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CPStyledProjectileModel());
    }

    @Override
    public void render(EntityProjectileSpell entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        ProjectileTimeline timeline = entity.resolver().spell.particleTimeline().get(ArsNouveauRegistry.PHALANX_TIMELINE.get());
        ModelProperty modelProperty = timeline.trailEffect.motion().propertyMap.get(ParticlePropertyRegistry.MODEL_PROPERTY.get());
        if (modelProperty == null || modelProperty.selectedResource == ModelProperty.NONE) {
            return;
        }
        modelProp = modelProperty;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, EntityProjectileSpell animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));

        if (modelProp.selectedResource.supportsColor()) {
            colour = modelProp.subPropMap.getParticleColor().getColor();
        }

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }

    @Override
    public @Nullable RenderType getRenderType(EntityProjectileSpell animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutout(texture);
    }

    @Override
    public GeoModel<EntityProjectileSpell> getGeoModel() {
        return super.getGeoModel();
    }
}