package alexthw.ars_elemental.client.firenando;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FirenandoModel extends AnimatedGeoModel<IAnimatable> {

    @Override
    public ResourceLocation getModelLocation(IAnimatable object) {
        return prefix("geo/fire_golem.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(IAnimatable object) {
        return prefix("textures/entity/fire_golem.png");
    }

    /**
     * This resource location needs to point to a json file of your animation file,
     * i.e. "geckolib:animations/frog_animation.json"
     *
     * @param animatable ignore
     * @return the animation file location
     */
    @Override
    public ResourceLocation getAnimationFileLocation(IAnimatable animatable) {
        return prefix("animations/fire_golem.animation.json");
    }

    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, AnimationEvent customPredicate) {
            super.setLivingAnimations(entity, uniqueID, customPredicate);
            if (customPredicate == null) return;
            IBone head = this.getAnimationProcessor().getBone("head");
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
    }
}
