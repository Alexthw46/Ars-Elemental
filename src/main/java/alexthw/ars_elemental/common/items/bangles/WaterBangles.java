package alexthw.ars_elemental.common.items.bangles;

import alexthw.ars_elemental.api.item.ISchoolBangle;
import alexthw.ars_elemental.common.items.ElementalCurio;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForgeMod;
import top.theillusivec4.curios.api.SlotContext;

public class WaterBangles extends ElementalCurio implements ISchoolBangle {

    public WaterBangles(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellSchool getSchool() {
        return SpellSchools.ELEMENTAL_WATER;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(NeoForgeMod.SWIM_SPEED, new AttributeModifier(id, 0.5f, AttributeModifier.Operation.ADD_VALUE));
        if (slotContext.entity() != null && slotContext.entity().isInWaterOrRain())
            map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(id,  0.035f, AttributeModifier.Operation.ADD_VALUE));
        return map;
    }

}
