package alexthw.ars_elemental.common.glyphs;

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
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell newSpell = spellContext.getRemainingSpell();
        SpellContext newContext = new SpellContext(newSpell, shooter).withColors(spellContext.colors).withCastingTile(spellContext.castingTile).withType(spellContext.getType());
        SpellResolver newResolver = resolver.getNewResolver(newContext);
        propagate(world, rayTraceResult.getLocation(), shooter, stats, newResolver);

    }

    void propagate(Level world, Vec3 pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

}
