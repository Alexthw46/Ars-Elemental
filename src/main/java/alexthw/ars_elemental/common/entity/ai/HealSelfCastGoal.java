package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;

/**
 * Convenience self-cast goal that always casts a heal spell when below a fraction of max health.
 */
public class HealSelfCastGoal<T extends EntityMageBase> extends SelfCastGoal<T> {

    private static final Spell spell = new Spell(MethodSelf.INSTANCE, EffectHeal.INSTANCE, AugmentAmplify.INSTANCE);

    /**
     * @param entity         The mage entity
     * @param healthFraction Fraction of max health below which to trigger (e.g. 0.33 for one third)
     * @param animId         Animation id to play
     * @param delayTicks     Delay between animation and cast
     */
    public HealSelfCastGoal(T entity, double healthFraction, int animId, int delayTicks) {
        super(entity,
                () -> spell,
                // condition: cooldown expired and health below threshold
                () -> (entity.selfCastCooldown <= 0 && entity.getHealth() <= entity.getMaxHealth() * healthFraction),
                animId,
                delayTicks
        );
    }

}

