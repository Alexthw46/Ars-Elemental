package alexthw.ars_elemental.common.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Supplier;

public class CastGoal<T extends Mob> extends Goal {

    protected final T mob;

    protected final double speedModifier;
    protected final float attackRadiusSqr;
    protected int seeTime;

    int animId;
    boolean hasAnimated;
    int animatedTicks;
    int delayTicks;
    boolean done;

    Supplier<Boolean> canUse;

    public CastGoal(T entity, double speed, int attackInterval, float attackRange, Supplier<Boolean> canUse, int animId, int delayTicks) {
        this.mob = entity;
        this.speedModifier = speed;
        this.attackRadiusSqr = attackRange * attackRange;
        this.canUse = canUse;
        this.animId = animId;
        this.delayTicks = delayTicks;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return this.canUse.get() && this.mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && !this.done;
    }

    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        int attackTime = -1;
        animatedTicks = 0;
        done = false;
        hasAnimated = false;
    }


}
