package alexthw.ars_elemental.client.armor;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.item.IElementalArmor;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class ElementalArmorModel<T extends IElementalArmor & GeoItem> extends GeoModel<T> {

    public ResourceLocation modelLocation;
    public ResourceLocation textLoc;
    public ResourceLocation animationLoc;

    public ElementalArmorModel(String name) {
        this.modelLocation = new ResourceLocation(ArsElemental.MODID, "geo/" + name + ".geo.json");
        this.textLoc = new ResourceLocation(ArsElemental.MODID, "textures/armor/" + name + ".png");
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        return modelLocation;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return textLoc;
    }

    public GeoModel<T> withEmptyAnim() {
        this.animationLoc = new ResourceLocation(ArsNouveau.MODID, "animations/empty.json");
        return this;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animationLoc;
    }

}
