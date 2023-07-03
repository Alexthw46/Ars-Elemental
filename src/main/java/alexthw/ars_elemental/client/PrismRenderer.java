package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.blocks.prism.AdvancedPrismTile;
import com.hollingsworth.arsnouveau.client.renderer.item.GenericItemBlockRenderer;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import static net.minecraft.world.item.ItemDisplayContext.HEAD;
import static software.bernie.geckolib.util.ClientUtils.getLevel;


public class PrismRenderer extends GeoBlockRenderer<AdvancedPrismTile> {

    public static GenericItemBlockRenderer getISTER() {

        return new GenericItemBlockRenderer(new GenericModel<>("advanced_prism"));
    }

    public PrismRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(new GenericModel<>("advanced_prism") {

            @Override
            public void setCustomAnimations(AdvancedPrismTile animatable, long instanceId, AnimationState<AdvancedPrismTile> animationState) {
                CoreGeoBone master = this.getAnimationProcessor().getBone("master");
                master.setRotY((animatable.getRotationX() + 90) * Mth.DEG_TO_RAD);
                master.setRotX(animatable.getRotationY() * Mth.DEG_TO_RAD);
            }
        });
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
    }

    MultiBufferSource buffer;
    ResourceLocation text;


    @Override
    public void preRender(PoseStack poseStack, AdvancedPrismTile animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.buffer = bufferSource;
        this.text = this.getTextureLocation(animatable);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, AdvancedPrismTile animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("core") && animatable != null && animatable.getLens() != null) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, -0.15);
            poseStack.scale(0.85F, 0.85F, 0.65F);
            Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getLens(), HEAD, packedLight, OverlayTexture.NO_OVERLAY, poseStack,
                                this.buffer, getLevel(), packedLight);
            poseStack.popPose();
            buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(text));

        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

    }
}
