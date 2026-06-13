package alexthw.ars_elemental.common.glyphs.filters;

import alexthw.ars_elemental.common.glyphs.ElementalAbstractFilter;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class InsectFilter extends ElementalAbstractFilter {

    public static ElementalAbstractFilter INSTANCE = new InsectFilter("insect", "Insect");
    public static ElementalAbstractFilter NOT_INSTANCE = new InsectFilter("not_insect", "Not Insect").inverted();

    InsectFilter(String name, String description) {
        super(name, description);
    }

    @Override
    public String getBookDescription() {
        return "Stops the spell from resolving " + (inverted ? "unless " : "if ") + "target an arthropod creature";
    }

    /**
     * Whether the filter should allow the entity hit
     *
     * @param target EntityHitResult
     */
    @Override
    public boolean shouldResolveOnEntity(EntityHitResult target, Level level, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return target.getEntity() instanceof LivingEntity living && (living.getType().is(EntityTypeTags.ARTHROPOD) || living.getType().is(ModRegistry.INSECT));
    }

}
