package alexthw.ars_elemental.client.firenando;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

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
     * @param animatable
     * @return the animation file location
     */
    @Override
    public ResourceLocation getAnimationFileLocation(IAnimatable animatable) {
        return prefix("animations/fire_golem.animations.json");
    }

    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }
}
