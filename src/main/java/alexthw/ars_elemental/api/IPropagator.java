package alexthw.ars_elemental.api;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public interface IPropagator{

    AbstractAugment DUMMY = new AbstractAugment("dummy", "Dummy") {
        @Override
        public int getDefaultManaCost() {
            return 0;
        }
    };

    default void copyResolver(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        SpellContext newContext = spellContext.makeChildContext();
        newContext.getSpell().recipe.add(0, DUMMY);
        spellContext.setCanceled(true);
        SpellResolver newResolver = resolver.getNewResolver(newContext);
        propagate(world, rayTraceResult, shooter, stats, newResolver);
    }

    void propagate(Level world, HitResult hitResult, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

}
