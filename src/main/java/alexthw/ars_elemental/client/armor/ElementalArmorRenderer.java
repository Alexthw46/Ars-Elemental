package alexthw.ars_elemental.client.armor;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.item.IElementalArmor;
import com.hollingsworth.arsnouveau.client.renderer.item.ArmorRenderer;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import net.minecraft.resources.ResourceLocation;
import software.bernie.ars_nouveau.geckolib3.model.AnimatedGeoModel;

public class ElementalArmorRenderer<ElementalArmor> extends ArmorRenderer {

    public ElementalArmorRenderer(AnimatedGeoModel<AnimatedMagicArmor> modelProvider) {
        super(modelProvider);
    }

    @Override
    public ResourceLocation getTextureLocation(AnimatedMagicArmor instance) {
        if (instance instanceof IElementalArmor armor) {
            return new ResourceLocation(ArsElemental.MODID, "textures/armor/" + armor.getTier() + "_armor_" + armor.getSchool().getId() + ".png");
        }
        return super.getTextureLocation(instance);
    }

}
