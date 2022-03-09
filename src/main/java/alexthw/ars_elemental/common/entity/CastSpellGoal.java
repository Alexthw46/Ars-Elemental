package alexthw.ars_elemental.common.entity;

import com.hollingsworth.arsnouveau.common.entity.goal.wealdwalker.CastGoal;

import java.util.function.Supplier;

public class CastSpellGoal extends CastGoal<FirenandoEntity> {

    FirenandoEntity firenando;

    public CastSpellGoal(FirenandoEntity entity, double speed, int attackInterval, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks){
        super(entity, speed, attackInterval, attackRange, canUse, animId, delayTicks);
        this.firenando = entity;
    }

    @Override
    public void start() {
        super.start();
        firenando.getEntityData().set(FirenandoEntity.SHOOTING, true);
    }

    @Override
    public void stop() {
        super.stop();
        firenando.getEntityData().set(FirenandoEntity.SHOOTING, false);
    }
}
