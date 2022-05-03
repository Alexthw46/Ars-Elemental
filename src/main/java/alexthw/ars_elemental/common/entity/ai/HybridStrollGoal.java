package alexthw.ars_elemental.common.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

public class HybridStrollGoal extends RandomStrollGoal {
    public HybridStrollGoal(PathfinderMob pMob, double pSpeedModifier, int interval) {
        super(pMob, pSpeedModifier, interval);
    }

    protected Vec3 getPosition() {
        return mob.isInWater() ? BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7) : super.getPosition();
    }

}
