package alexthw.ars_elemental.common.items.bangles;

import alexthw.ars_elemental.common.items.ElementalCurio;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class BaseBangle extends ElementalCurio implements ISpellModifierItem {
    public BaseBangle(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (world.getRandom().nextBoolean())
            builder.addDamageModifier(2.0D);
        return builder;
    }
}
