package alexthw.ars_elemental.client.firenando;

import alexthw.ars_elemental.common.entity.familiars.FirenandoFamiliar;
import com.hollingsworth.arsnouveau.api.client.IVariantTextureProvider;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FirenandoFamiliarRenderer <M extends FirenandoFamiliar> extends FirenandoRenderer<M>{

    public FirenandoFamiliarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    @Override
    public void render(M entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(0.85f, 0.85f, 0.85f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        ParticleColor color = entity.getColor().equals("soul") ? color2 : color1;
        entity.getLevel().addParticle(GlowParticleData.createData(color, false, 0.30f, 0.7f, 15),
                entity.getX() + ParticleUtil.inRange(-0.1, 0.1) / 4, entity.getY() + 0.6, entity.getZ() + ParticleUtil.inRange(-0.1, 0.1) / 4,
                0, -0.05F, 0);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(M instance) {
        return instance.getTexture(instance);
    }
}
