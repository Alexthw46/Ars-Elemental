package alexthw.ars_elemental.client.armor;

import alexthw.ars_elemental.ArsElemental;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElementalArmorModel<T extends AnimatedMagicArmor> extends GeoModel<T> {

    public ResourceLocation modelLocation;
    public ResourceLocation textLoc;
    public ResourceLocation animationLoc;

    public ElementalArmorModel(String name) {
        this.modelLocation = ResourceLocation.fromNamespaceAndPath(ArsElemental.MODID, "geo/" + name + ".geo.json");
        this.textLoc = ResourceLocation.fromNamespaceAndPath(ArsElemental.MODID, "textures/armor/" + name + ".png");
        this.animationLoc = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "animations/empty.json");
    }

    @Override
    public ResourceLocation getModelResource(T object) {
        return modelLocation;
    }

    @Override
    public ResourceLocation getTextureResource(T object) {
        return textLoc;
    }

    @Deprecated(forRemoval = true)
    public GeoModel<T> withEmptyAnim() {
        this.animationLoc = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, "animations/empty.json");
        return this;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animationLoc;
    }

}
