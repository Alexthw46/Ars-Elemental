package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class MermaidAi {
    private static final float SPEED_MULTIPLIER_ON_LAND = 0.15F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.6F;

    //TODO Needs Sensors and Memory

    public static Brain<?> makeBrain(Brain<MermaidEntity> p_149291_) {
        initCoreActivity(p_149291_);
        initIdleActivity(p_149291_);
        p_149291_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_149291_.setDefaultActivity(Activity.IDLE);
        p_149291_.useDefaultActivity();
        return p_149291_;
    }

    private static void initCoreActivity(Brain<MermaidEntity> p_149307_) {
        p_149307_.addActivity(Activity.CORE, 0, ImmutableList.of(
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()));
    }

    private static void initIdleActivity(Brain<MermaidEntity> p_149309_) {
        p_149309_.addActivity(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
                        //Pair.of(2, new FollowTemptation(MermaidAi::getSpeedModifier)),
                        Pair.of(3, new StartAttacking<>(MermaidAi::findNearestValidAttackTarget)),
                        Pair.of(3, new TryFindWater(6, SPEED_MULTIPLIER_ON_LAND)),
                        Pair.of(4, new GateBehavior<>(
                                        ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                                        ImmutableSet.of(),
                                        GateBehavior.OrderPolicy.ORDERED,
                                        GateBehavior.RunningPolicy.TRY_ALL,
                                        ImmutableList.of(
                                                Pair.of(new RandomSwim(SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER), 2),
                                                Pair.of(new RandomStroll(0.15F, false), 2),
                                                Pair.of(new SetWalkTargetFromLookTarget(MermaidAi::canSetWalkTargetFromLookTarget, MermaidAi::getSpeedModifier, 3), 3),
                                                Pair.of(new RunIf<>(Entity::isInWaterOrBubble, new DoNothing(30, 60)), 5),
                                                Pair.of(new RunIf<>(Entity::isOnGround, new DoNothing(200, 400)), 5)
                                        )
                                )
                        )
                )
        );
    }

    private static boolean canSetWalkTargetFromLookTarget(LivingEntity p_182381_) {
        Level level = p_182381_.level;
        Optional<PositionTracker> optional = p_182381_.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
        if (optional.isPresent()) {
            BlockPos blockpos = optional.get().currentBlockPosition();
            return level.isWaterAt(blockpos) == p_182381_.isInWaterOrBubble();
        } else {
            return false;
        }
    }

    public static void updateActivity(MermaidEntity p_149293_) {
        Brain<MermaidEntity> brain = p_149293_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse(null) != Activity.FIGHT) {
            brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
        }
    }

    private static float getSpeedModifierChasing(LivingEntity p_149289_) {
        return p_149289_.isInWaterOrBubble() ? SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER : SPEED_MULTIPLIER_ON_LAND;
    }

    private static float getSpeedModifier(LivingEntity p_149301_) {
        return p_149301_.isInWaterOrBubble() ? SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER : SPEED_MULTIPLIER_ON_LAND;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(MermaidEntity p_149299_) {
        return p_149299_.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }
}
