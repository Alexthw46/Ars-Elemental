package alexthw.ars_elemental.api;

import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public interface IPropagator {

    default void copyResolver(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        spellContext.setCanceled(true);
        //keep the propagator glyph to ensure augments are applied
        spellContext.setCurrentIndex(spellContext.getCurrentIndex() - 1);
        Spell newSpell = spellContext.getRemainingSpell();
        if (newSpell.isEmpty()) return;
        SpellContext newContext = spellContext.clone().withSpell(newSpell);
        SpellResolver newResolver = new SpellResolver(newContext) {
            //remove the leftover propagator before resolving
            @Override
            protected void resolveAllEffects(Level world) {
                if (!this.spellContext.getSpell().recipe.isEmpty())
                    this.spellContext.getSpell().recipe.remove(0);
                super.resolveAllEffects(world);
            }

            @Override
            public boolean hasFocus(ItemStack stack) {
                return resolver.hasFocus(stack);
            }
        };
        propagate(world, rayTraceResult, shooter, stats, newResolver);
    }

    void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

}
