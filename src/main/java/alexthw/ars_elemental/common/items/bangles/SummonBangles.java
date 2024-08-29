package alexthw.ars_elemental.common.items.bangles;

import alexthw.ars_elemental.api.item.ISchoolBangle;
import alexthw.ars_elemental.common.items.ElementalCurio;
import alexthw.ars_elemental.registry.ModRegistry;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class SummonBangles extends ElementalCurio implements ISchoolBangle {

    public SummonBangles(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation uuid, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
        map.put(ModRegistry.SUMMON_POWER, new AttributeModifier(uuid,  2.d, AttributeModifier.Operation.ADD_VALUE));
        return map;
    }

    @Override
    public SpellSchool getSchool() {
        return SpellSchools.CONJURATION;
    }

}
