package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.entity.familiar.ISpellCastListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class FirenandoFamiliar extends FamiliarEntity implements ISpellCastListener {

    public FirenandoFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public FirenandoFamiliar(Level world) {
        super(ModEntities.FIRENANDO_FAMILIAR.get(), world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle_controller", 0, this::idlePredicate));
    }

    public void onModifier(SpellModifierEvent event) {
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster) && SpellSchools.ELEMENTAL_FIRE.isPartOfSchool(event.spellPart)) {
            event.builder.addDamageModifier(2.0D);
        }
    }

    @Override
    public PlayState walkPredicate(AnimationEvent event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        return PlayState.CONTINUE;
    }

    <T extends IAnimatable> PlayState idlePredicate(AnimationEvent<T> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle.body"));
        return PlayState.CONTINUE;
    }
}
