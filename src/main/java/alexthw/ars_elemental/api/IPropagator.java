package alexthw.ars_elemental.api;

import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IPropagator {

    default void copyResolver(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        // Cancel the current spell and get the remaining glyphs
        if (spellContext.getCurrentIndex() == 1) return;
        spellContext.setCanceled(true);
        spellContext.setCurrentIndex(spellContext.getCurrentIndex() - 1);
        Spell newSpell = spellContext.getRemainingSpell();
        if (newSpell.isEmpty()) return;
        // Create a new context and resolver with the remaining glyphs
        SpellContext newContext = spellContext.clone().withSpell(newSpell);
        SpellResolver newResolver = resolver.getNewResolver(newContext);
        // Propagate the new spell
        propagate(world, rayTraceResult.getLocation(), shooter, stats, newResolver, spellContext);

    }

    void propagate(Level world, Vec3 pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver, SpellContext spellContext);

}
