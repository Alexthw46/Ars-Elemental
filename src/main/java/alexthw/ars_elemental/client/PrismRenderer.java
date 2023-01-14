package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.blocks.prism.AdvancedPrismTile;
import com.hollingsworth.arsnouveau.client.renderer.item.GenericItemBlockRenderer;
import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.processor.IBone;
import software.bernie.ars_nouveau.geckolib3.geo.render.built.GeoBone;
import software.bernie.ars_nouveau.geckolib3.renderers.geo.GeoBlockRenderer;

public class PrismRenderer extends GeoBlockRenderer<AdvancedPrismTile> {

    public static GenericItemBlockRenderer getISTER() {

        return new GenericItemBlockRenderer(new GenericModel<>("advanced_prism"));
    }

    public PrismRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(rendererProvider, new GenericModel<>("advanced_prism") {
            @Override
            public void setCustomAnimations(AdvancedPrismTile tile, int instanceId, AnimationEvent event) {
                IBone master = this.getAnimationProcessor().getBone("master");
                master.setRotationY((tile.getRotationX() + 90) * Mth.DEG_TO_RAD);
                master.setRotationX(tile.getRotationY() * Mth.DEG_TO_RAD);
            }
        });
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
    }

    MultiBufferSource buffer;
    ResourceLocation text;

    @Override
    public void renderEarly(AdvancedPrismTile animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.buffer = bufferSource;
        this.text = this.getTextureLocation(animatable);
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer bufferIn, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        AdvancedPrismTile tile = animatable;
        if (bone.getName().equals("core") && tile != null && tile.getLens() != null) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, -0.15);
            poseStack.scale(0.85F, 0.85F, 0.65F);
            Minecraft.getInstance().getItemRenderer().renderStatic(tile.getLens(), ItemTransforms.TransformType.HEAD, packedLight, OverlayTexture.NO_OVERLAY, poseStack,
                    this.buffer, packedLight);
            poseStack.popPose();
            bufferIn = buffer.getBuffer(RenderType.entityCutoutNoCull(text));

        }
        super.renderRecursively(bone, poseStack, bufferIn, packedLight, packedOverlay, red, green, blue, alpha);

    }
}
