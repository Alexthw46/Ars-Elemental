package alexthw.ars_elemental.client.firenando;

import com.hollingsworth.arsnouveau.api.client.IVariantTextureProvider;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import static alexthw.ars_elemental.common.entity.FirenandoEntity.COLOR;

@SuppressWarnings("unchecked")
public class FirenandoRenderer extends GeoEntityRenderer {

    ParticleColor color1 = new ParticleColor(230, 45, 15);
    ParticleColor color2 = new ParticleColor(150, 200, 15);

    ParticleColor color3 = new ParticleColor(15, 100, 200);

    public FirenandoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FirenandoModel());
    }

    public RenderType getRenderType(Object animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntity instance) {
        if (instance instanceof IVariantTextureProvider variant) {
            return variant.getTexture(instance);
        }
        return super.getTextureLocation(instance);
    }

    @Override
    public void render(LivingEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        ParticleColor color = entity.getEntityData().get(COLOR).equals("soul") ? color3 : color1;
        entity.getLevel().addParticle(GlowParticleData.createData(color, false, 0.30f, 0.7f, 15),
                entity.getX() + ParticleUtil.inRange(-0.1, 0.1) / 4, entity.getY() + 0.6, entity.getZ() + ParticleUtil.inRange(-0.1, 0.1) / 4,
                0, -0.05F, 0);
        entity.getLevel().addParticle(GlowParticleData.createData(color2, false, 0.15f, 0.7f, 15),
                entity.getX() + ParticleUtil.inRange(-0.1, 0.1) / 4, entity.getY() + 0.6, entity.getZ() + ParticleUtil.inRange(-0.1, 0.1) / 4,
                0, -0.05F, 0);
    }
}
