package alexthw.ars_elemental.client.mermaid;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MermaidModel<T extends GeoEntity> extends GeoModel<T> {

    ResourceLocation MODEL = prefix("geo/mermaid.geo.json");
    ResourceLocation TEXTURE = prefix("textures/entity/mermaid.png");
    ResourceLocation ANIM = prefix("animations/mermaid.animation.json");

    @Override
    public ResourceLocation getModelResource(T object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return TEXTURE;
    }

    /**
     * This resource location needs to point to a json file of your animation file,
     * i.e. "geckolib:animations/frog_animation.json"
     *
     * @param animatable ignore
     * @return the animation file location
     */
    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIM;
    }


    @Override
    public void setCustomAnimations(T entity, long uniqueID, @Nullable AnimationState<T> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        if (customPredicate == null) return;
        GeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData)customPredicate.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(extraData.headPitch() * 0.002f);
        head.setRotY(extraData.netHeadYaw() * 0.005f);
    }

}
