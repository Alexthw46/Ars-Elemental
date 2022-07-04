package alexthw.ars_elemental.common.items;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;

public class LesserElementalFocus extends ElementalFocus {
    public LesserElementalFocus(Properties properties, SpellSchool element) {
        super(properties, element);
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (SpellSchools.ELEMENTAL.isPartOfSchool(spellPart)) {
            if (element.isPartOfSchool(spellPart)) {
                builder.addAmplification(getBoostMultiplier() / 2);
            } else {
                builder.addAmplification(getMalusMultiplier());
            }
        }
        return builder;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
    }

}
