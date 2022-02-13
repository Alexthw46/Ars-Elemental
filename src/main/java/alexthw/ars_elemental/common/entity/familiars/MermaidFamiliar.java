package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.entity.familiar.ISpellCastListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class MermaidFamiliar extends FamiliarEntity implements ISpellCastListener {
    public MermaidFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public void onModifier(SpellModifierEvent event) {
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster) && SpellSchools.ELEMENTAL_WATER.isPartOfSchool(event.spellPart)) {
            event.builder.addDamageModifier(2.0D);
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.isInWater()) {
            if (this.level.getGameTime() % 60L == 0L && this.getOwner() != null) {
                this.getOwner().addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
                this.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
            }
        }
    }

    @Override
    public PlayState walkPredicate(AnimationEvent event) {
        if (event.isMoving()) {
           //event.getController().setAnimation(new AnimationBuilder().addAnimation("run"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    public @NotNull EntityType<?> getType() {
        return ModRegistry.SIREN_ENTITY.get();
    }

}
