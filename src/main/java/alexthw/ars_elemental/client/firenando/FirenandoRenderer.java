package alexthw.ars_elemental.client.firenando;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.ars_nouveau.geckolib.animatable.GeoEntity;
import software.bernie.ars_nouveau.geckolib.renderer.GeoEntityRenderer;

public class FirenandoRenderer<M extends LivingEntity & GeoEntity> extends GeoEntityRenderer<M> {

    ParticleColor color1 = new ParticleColor(230, 45, 15);
    ParticleColor color2 = new ParticleColor(15, 100, 200);
    ParticleColor sideColor = new ParticleColor(150, 200, 15);

    public FirenandoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FirenandoModel<>());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull M instance) {
        return instance instanceof FirenandoEntity var ? var.getTexture(var) : super.getTextureLocation(instance);
    }

    @Override
    public RenderType getRenderType(M animatable, ResourceLocation texture, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

    @Override
    public void render(@NotNull M entity, float entityYaw, float partialTicks, @NotNull PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        if (entity instanceof FirenandoEntity fe && fe.isActive()) {
            ParticleColor color = fe.getColor(fe).equals("soul") ? color2 : color1;
            entity.level().addParticle(GlowParticleData.createData(color, false, 0.30f, 0.7f, 15),
                    entity.getX() + ParticleUtil.inRange(-0.1, 0.1) / 4, entity.getY() + 0.6, entity.getZ() + ParticleUtil.inRange(-0.1, 0.1) / 4,
                    0, -0.05F, 0);
            entity.level().addParticle(GlowParticleData.createData(sideColor, false, 0.15f, 0.7f, 15),
                    entity.getX() + ParticleUtil.inRange(-0.1, 0.1) / 4, entity.getY() + 0.6, entity.getZ() + ParticleUtil.inRange(-0.1, 0.1) / 4,
                    0, -0.05F, 0);
        }
    }

}
