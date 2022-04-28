package alexthw.ars_elemental;

import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AbstractEffectFilter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FiltersCompat {

    public static boolean checkFilters(LivingEntity e, Set<AbstractEffectFilter> filters) {
        boolean flag = true;
        if (filters == null) return false;
        for (AbstractEffectFilter spellPart : filters){
            flag &= spellPart.matches(new EntityRayTraceResult(e));
        }
        return flag;
    }

    public static Set<AbstractEffectFilter> getFilters(List<AbstractSpellPart> recipe) {
        Set<AbstractEffectFilter> list = new HashSet<>();
        for (AbstractSpellPart glyph : recipe){
            if (glyph instanceof AbstractCastMethod) continue;
            if (glyph instanceof AbstractEffectFilter){
                list.add((AbstractEffectFilter) glyph);
            }else if (glyph instanceof AbstractEffect) break;
        }
        return list;
    }

}
