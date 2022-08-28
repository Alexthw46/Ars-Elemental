package alexthw.ars_elemental.util;

import com.hollingsworth.arsnouveau.api.spell.*;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AbstractEffectFilter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class    TooManyCompats {
    public static boolean checkIgnoreFilters(Entity e, Set<IFilter> filters) {
        boolean flag = true;
        if (filters == null) return true;
        for (IFilter spellPart : filters) {
            flag &= spellPart.shouldResolveOnEntity(e);
        }
        return !flag;
    }

    public static Set<IFilter> getFilters(List<AbstractSpellPart> recipe, int index) {
        Set<IFilter> list = new HashSet<>();
        for (AbstractSpellPart glyph : recipe.subList(index, recipe.size())) {
            if (glyph instanceof AbstractCastMethod) continue;
            if (glyph instanceof IFilter filter) {
                list.add(filter);
            } else if (glyph instanceof AbstractEffect) break;
        }
        return list;
    }

    public static Predicate<Entity> getFilterPredicate(Spell spell, Predicate<Entity> defaultFilter) {
        Set<IFilter> set = getFilters(spell.recipe, 0);
        if (set.isEmpty()) return defaultFilter;
        return (entity -> !checkIgnoreFilters(entity, set));
    }

}
