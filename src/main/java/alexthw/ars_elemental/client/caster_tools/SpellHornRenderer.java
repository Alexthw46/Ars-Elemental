package alexthw.ars_elemental.client.caster_tools;

import alexthw.ars_elemental.common.items.caster_tools.SpellHorn;
import com.hollingsworth.arsnouveau.client.renderer.item.FixedGeoItemRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class SpellHornRenderer extends FixedGeoItemRenderer<SpellHorn> {

    public SpellHornRenderer() {
        super(new SpellHornModel());
    }

    @Override
    public RenderType getRenderType(Object animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(textureLocation);
    }

}
