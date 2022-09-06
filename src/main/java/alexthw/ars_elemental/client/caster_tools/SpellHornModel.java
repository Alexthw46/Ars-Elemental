package alexthw.ars_elemental.client.caster_tools;

import alexthw.ars_elemental.common.items.caster_tools.SpellHorn;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SpellHornModel extends AnimatedGeoModel<SpellHorn> {

    ResourceLocation MODEL =  prefix("geo/spell_horn.geo.json");
    ResourceLocation TEXTURE = prefix("textures/item/spell_horn.png");
    ResourceLocation ANIM = prefix("animations/item/spell_horn.animation.json");

    @Override
    public ResourceLocation getModelResource(SpellHorn object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SpellHorn object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SpellHorn animatable) {
        return ANIM;
    }

}
