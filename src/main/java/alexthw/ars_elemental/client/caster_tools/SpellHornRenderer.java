package alexthw.ars_elemental.client.caster_tools;

import alexthw.ars_elemental.common.items.caster_tools.SpellHorn;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;


public class SpellHornRenderer extends GeoItemRenderer<SpellHorn> {

    public SpellHornRenderer() {
        super(new SpellHornModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, SpellHorn animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        //we override the color getter for a specific bone, this means the other ones need to use the neutral color
        if (bone.getName().equals("gems") || (bone.getParent() != null && bone.getParent().getName().equals("gems"))) {
            //NOTE: if the bone have a parent, the recursion will get here with the neutral color, making the color getter useless
            DyeColor color1 = getCurrentItemStack().getOrDefault(DataComponents.BASE_COLOR, DyeColor.PURPLE);
            color = FastColor.ABGR32.color(200, color1.getTextColor());
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        } else {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        }
    }

    @Override
    public RenderType getRenderType(SpellHorn animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
