package alexthw.ars_elemental.common.items.bangles;

import alexthw.ars_elemental.api.item.ISchoolBangle;
import alexthw.ars_elemental.common.items.ElementalCurio;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import top.theillusivec4.curios.api.SlotContext;

public class FireBangles extends ElementalCurio implements ISchoolBangle {

    public FireBangles(Properties properties) {
        super(properties);
    }

    @Override
    public SpellSchool getSchool() {
        return SpellSchools.ELEMENTAL_FIRE;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity != null) {
            Holder<Biome> biome = entity.level().getBiome(slotContext.entity().getOnPos());
            if (biome.value().getBaseTemperature() > 1.8f) {
                Multimap<Holder<Attribute>, AttributeModifier> map = HashMultimap.create();
                map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(id, 0.035f, AttributeModifier.Operation.ADD_VALUE));
                return map;
            }
        }
        return super.getAttributeModifiers(slotContext, id, stack);
    }

}
