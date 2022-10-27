package alexthw.ars_elemental.client.mermaid;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import alexthw.ars_elemental.common.entity.familiars.MermaidFamiliar;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class MermaidRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {

    public MermaidRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MermaidModel<>());
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        if (instance instanceof MermaidEntity var) return var.getTexture(var);
        if (instance instanceof MermaidFamiliar var) return var.getTexture(var);
        return super.getTextureLocation(instance);
    }

    public RenderType getRenderType(T animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

}
