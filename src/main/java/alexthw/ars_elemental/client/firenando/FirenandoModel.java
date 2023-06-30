package alexthw.ars_elemental.client.firenando;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.ars_nouveau.geckolib.animatable.GeoEntity;
import software.bernie.ars_nouveau.geckolib.constant.DataTickets;
import software.bernie.ars_nouveau.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.ars_nouveau.geckolib.core.animation.AnimationState;
import software.bernie.ars_nouveau.geckolib.model.GeoModel;
import software.bernie.ars_nouveau.geckolib.model.data.EntityModelData;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FirenandoModel<M extends LivingEntity & GeoEntity> extends GeoModel<M> {

    ResourceLocation MODEL = prefix("geo/fire_golem.geo.json");
    ResourceLocation DEF_TEXTURE = prefix("textures/entity/fire_golem.png");
    ResourceLocation ANIMATIONS = prefix("animations/fire_golem.animation.json");

    @Override
    public ResourceLocation getModelResource(M object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(M object) {
        return DEF_TEXTURE;
    }

    /**
     * This resource location needs to point to a json file of your animation file,
     * i.e. "geckolib:animations/frog_animation.json"
     *
     * @param animatable ignore
     * @return the animation file location
     */
    @Override
    public ResourceLocation getAnimationResource(M animatable) {
        return ANIMATIONS;
    }


    @Override
    public void setCustomAnimations(M entity, long uniqueID, AnimationState<M> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        if (customPredicate == null || entity instanceof FirenandoEntity fe && !fe.isActive()) return;
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(extraData.headPitch() * ((float) Math.PI / 330F));
        head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 330F));
    }

}
