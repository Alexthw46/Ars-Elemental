package alexthw.ars_elemental.client;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.blocks.ElementalSpellTurretTile;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.item.GenericItemBlockRenderer;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class ElementalTurretRenderer<TL extends ElementalSpellTurretTile> extends GeoBlockRenderer<TL> {

    public static AnimatedGeoModel modelFire = new TurretModel<>("fire");
    public static AnimatedGeoModel modelWater = new TurretModel<>("water");
    public static AnimatedGeoModel modelAir = new TurretModel<>("air");
    public static AnimatedGeoModel modelEarth = new TurretModel<>("earth");

    public ElementalTurretRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, modelFire);
    }

    @Override
    public void render(GeoModel model, TL animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.pushPose();
        Direction direction = animatable.getBlockState().getValue(BasicSpellTurret.FACING);
        if (direction == Direction.UP) {
            matrixStackIn.translate(0, -0.5, -0.5);
        } else if (direction == Direction.DOWN) {
            matrixStackIn.translate(0, -0.5, 0.5);
        }
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);

        matrixStackIn.popPose();
    }

    public static GenericItemBlockRenderer getISTER(String element) {
        AnimatedGeoModel<?> model = switch (element) {
            case "fire" -> modelFire;
            case "water" -> modelWater;
            case "air" -> modelAir;
            default -> modelEarth;
        };
        return new GenericItemBlockRenderer(model);
    }

    @Override
    public ResourceLocation getTextureLocation(TL instance) {
        return new ResourceLocation(ArsElemental.MODID, "textures/block/" + instance.getSchool().getId() + "_turret.png");
    }

    public static class TurretModel<T extends ElementalSpellTurretTile> extends AnimatedGeoModel<T> {

        final String element;

        public TurretModel(String element) {
            this.element = element;
        }

        @Override
        public ResourceLocation getModelLocation(T t) {
            return new ResourceLocation(ArsNouveau.MODID, "geo/basic_spell_turret.geo.json");
        }

        @Override
        public ResourceLocation getTextureLocation(T t) {
            return new ResourceLocation(ArsElemental.MODID, "textures/block/" + element + "_turret.png");
        }

        @Override
        public ResourceLocation getAnimationFileLocation(T t) {
            return new ResourceLocation(ArsNouveau.MODID, "animations/basic_spell_turret_animations.json");
        }
    }

}
