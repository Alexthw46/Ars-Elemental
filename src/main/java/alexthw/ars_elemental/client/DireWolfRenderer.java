package alexthw.ars_elemental.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class DireWolfRenderer extends MobRenderer<Wolf, WolfModel<Wolf>> {

    static final ResourceLocation DireWolfTexture = prefix("textures/entity/direwolf.png");

    public DireWolfRenderer(EntityRendererProvider.Context p_174452_) {
        super(p_174452_, new WolfModel<>(p_174452_.bakeLayer(ModelLayers.WOLF)), 0.5F);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float getBob(Wolf pLivingBase, float pPartialTicks) {
        return pLivingBase.getTailAngle();
    }

    public void render(Wolf pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isWet()) {
            float f = pEntity.getWetShade(pPartialTicks);
            this.model.setColor(f, f, f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        if (pEntity.isWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }

    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(Wolf entity) {
        return DireWolfTexture;
    }

    @Override
    protected void scale(Wolf pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {
        super.scale(pLivingEntity, pMatrixStack, pPartialTickTime);
        pMatrixStack.scale(1.2F, 1.3F, 1.4F);
    }

}
