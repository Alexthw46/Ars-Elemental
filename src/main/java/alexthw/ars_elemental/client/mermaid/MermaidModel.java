package alexthw.ars_elemental.client.mermaid;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class MermaidModel extends AnimatedGeoModel<IAnimatable> {

    @Override
    public ResourceLocation getModelLocation(IAnimatable object) {
        return new ResourceLocation(ArsElemental.MODID,"geo/mermaid.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(IAnimatable object) {
        return new ResourceLocation(ArsElemental.MODID,"textures/entity/mermaid.png");
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
        return new ResourceLocation(ArsElemental.MODID,"animations/mermaid.animation.json");
    }

    @Override
    public void setLivingAnimations(IAnimatable entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (customPredicate == null) return;
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        //head.setRotationX(extraData.headPitch * ((float) Math.PI / 330F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 330F));
    }

}
