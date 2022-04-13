package alexthw.ars_elemental.client.mages;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MageRenderer extends HumanoidMobRenderer<Mob, MageModel<Mob>> {

    private static final ResourceLocation testTexture = prefix("textures/entity/test_mage.png");

    public MageRenderer(EntityRendererProvider.Context context) {
        super(context, createModel(context.getModelSet()), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new MageModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new MageModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
    }

    private static MageModel<Mob> createModel(EntityModelSet modelSet) {
        return new MageModel<>(modelSet.bakeLayer(ModelLayers.PLAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(Mob pEntity) {
        return testTexture;
    }
}
