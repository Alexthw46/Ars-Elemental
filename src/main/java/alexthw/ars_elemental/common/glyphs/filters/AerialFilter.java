package alexthw.ars_elemental.common.glyphs.filters;

import alexthw.ars_elemental.common.glyphs.ElementalAbstractFilter;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class AerialFilter extends ElementalAbstractFilter {

    public static ElementalAbstractFilter INSTANCE = new AerialFilter("aerial", "Aerial");
    public static ElementalAbstractFilter NOT_INSTANCE = new AerialFilter("not_aerial", "Not Aerial").inverted();

    AerialFilter(String name, String description) {
        super(name, description);
    }

    @Override
    public String getBookDescription() {
        return "Stops the spell from resolving " + (inverted ? "unless " : "if ") + "target an aerial creature";
    }

    /**
     * Whether the filter should allow the entity hit
     *
     * @param target EntityHitResult
     */
    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target, Level level, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (target.getEntity() instanceof LivingEntity living) {
            return living.getType().is(ModRegistry.AERIAL);
        }
        return false;
    }

}
