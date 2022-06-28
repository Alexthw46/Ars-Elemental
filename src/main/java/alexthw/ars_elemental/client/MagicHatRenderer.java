package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.items.armor.ElementalHat;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MagicHatRenderer extends GeoArmorRenderer<ElementalHat> {

    public MagicHatRenderer() {
        super(new MagicHatModel());
        this.headBone = "bipedHead";
    }

    static class MagicHatModel extends AnimatedGeoModel<ElementalHat> {

        @Override
        public ResourceLocation getModelResource(ElementalHat object) {
            String school = object == null ? "fire" : object.getSchool().getId();
            return new ResourceLocation("ars_arsenal", "geo/" + school + "_hat.geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(ElementalHat object) {
            String school = object == null ? "fire" : object.getSchool().getId();
            return new ResourceLocation("ars_arsenal", "textures/item/" + school + "_hat.png");
        }

        @Override
        public ResourceLocation getAnimationResource(ElementalHat animatable) {
            return null;
        }
    }

}
