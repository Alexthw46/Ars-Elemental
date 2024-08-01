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
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ElementalTurretRenderer extends GeoBlockRenderer<ElementalSpellTurretTile> {

    public static GeoModel<ElementalSpellTurretTile> modelFire = new TurretModel<>("fire");
    public static GeoModel<ElementalSpellTurretTile> modelWater = new TurretModel<>("water");
    public static GeoModel<ElementalSpellTurretTile> modelAir = new TurretModel<>("air");
    public static GeoModel<ElementalSpellTurretTile> modelEarth = new TurretModel<>("earth");
    public static GeoModel<ElementalSpellTurretTile> modelShaper = new TurretModel<>("manipulation");

    public ElementalTurretRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(modelFire);
    }


    @Override
    public void actuallyRender(PoseStack poseStack, ElementalSpellTurretTile animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        Direction direction = animatable.getBlockState().getValue(BasicSpellTurret.FACING);
        if (direction == Direction.UP) {
            poseStack.translate(0.0, 0.5, -0.5);
        } else if (direction == Direction.DOWN) {
            poseStack.translate(0.0, 0.5, 0.5);
        }

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    public static GenericItemBlockRenderer getISTER(String element) {
        GeoModel<?> model = switch (element) {
            case "fire" -> modelFire;
            case "water" -> modelWater;
            case "air" -> modelAir;
            case "earth" -> modelEarth;
            default -> modelShaper;
        };
        return new GenericItemBlockRenderer(model);
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalSpellTurretTile instance) {
        return ResourceLocation.fromNamespaceAndPath(ArsElemental.MODID, "textures/block/" + instance.getSchool().getId() + "_turret.png");
    }

    public static class TurretModel<T extends ElementalSpellTurretTile> extends GeoModel<T> {

        final String element;

        public TurretModel(String element) {
            this.element = element;
        }

        @Override
        public ResourceLocation getModelResource(T t) {
            return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "geo/basic_spell_turret.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(T t) {
            return ResourceLocation.fromNamespaceAndPath(ArsElemental.MODID, "textures/block/" + element + "_turret.png");
        }

        @Override
        public ResourceLocation getAnimationResource(T t) {
            return ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "animations/basic_spell_turret_animations.json");
        }
    }

}
