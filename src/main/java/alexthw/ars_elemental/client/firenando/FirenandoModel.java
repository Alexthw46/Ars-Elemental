package alexthw.ars_elemental.client.firenando;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.processor.IBone;
import software.bernie.ars_nouveau.geckolib3.model.AnimatedGeoModel;
import software.bernie.ars_nouveau.geckolib3.model.provider.data.EntityModelData;

import static alexthw.ars_elemental.ArsElemental.prefix;

@SuppressWarnings("unchecked")
public class FirenandoModel<M extends LivingEntity & IAnimatable> extends AnimatedGeoModel<M> {

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
    public void setCustomAnimations(M entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        if (customPredicate == null || entity instanceof FirenandoEntity fe && !fe.isActive()) return;
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
    }

}
