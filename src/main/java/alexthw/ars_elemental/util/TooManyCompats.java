package alexthw.ars_elemental.util;

import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AbstractEffectFilter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class TooManyCompats {
    public static boolean checkIgnoreFilters(Entity e, Set<AbstractEffectFilter> filters) {
        boolean flag = true;
        if (filters == null) return true;
        for (AbstractEffectFilter spellPart : filters) {
            flag &= spellPart.matches(new EntityHitResult(e));
        }
        return !flag;
    }

    public static Set<AbstractEffectFilter> getFilters(List<AbstractSpellPart> recipe, int index) {
        Set<AbstractEffectFilter> list = new HashSet<>();
        for (AbstractSpellPart glyph : recipe.subList(index, recipe.size())) {
            if (glyph instanceof AbstractCastMethod) continue;
            if (glyph instanceof AbstractEffectFilter filter) {
                list.add(filter);
            } else if (glyph instanceof AbstractEffect) break;
        }
        return list;
    }

    public static Predicate<Entity> getFilterPredicate(Spell spell) {
        Set<AbstractEffectFilter> set = getFilters(spell.recipe, 0);
        return (entity -> !checkIgnoreFilters(entity, set));
    }

}
