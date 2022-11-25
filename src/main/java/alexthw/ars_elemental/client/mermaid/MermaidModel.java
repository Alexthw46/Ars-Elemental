package alexthw.ars_elemental.client.mermaid;

import net.minecraft.resources.ResourceLocation;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.processor.IBone;
import software.bernie.ars_nouveau.geckolib3.model.AnimatedGeoModel;
import software.bernie.ars_nouveau.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MermaidModel<T extends IAnimatable> extends AnimatedGeoModel<T> {

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


    @SuppressWarnings("unchecked")
    @Override
    public void setCustomAnimations(T entity, int uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        if (customPredicate == null) return;
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * 0.002f);
        head.setRotationY(extraData.netHeadYaw * 0.005f);
    }

}
