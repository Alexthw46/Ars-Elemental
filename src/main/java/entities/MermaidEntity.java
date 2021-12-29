package entities;

import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MermaidEntity extends WaterAnimal implements IAnimatable, IDispellable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public MermaidEntity(EntityType<? extends WaterAnimal> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this,1,40));
        this.applyEntityAI();
        //target selectors
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    private void applyEntityAI() {
        //target - no attacks
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        //no target
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level p_21480_) {
        return new MermaidPathNavigator(this, p_21480_);
    }

    @Override
    protected void handleAirSupply(int p_30344_) {
    }

    @Override
    public boolean onDispel(@Nullable LivingEntity livingEntity) {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "idle", 0, this::idle));
        data.addAnimationController(new AnimationController<>(this,"actions",10,this::actions));
    }


    private <T extends IAnimatable> PlayState idle(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        return PlayState.CONTINUE;
    }

    private <T extends IAnimatable> PlayState actions(AnimationEvent<T> event){
        if (this.isInWater() && event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("act",false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public static AttributeSupplier createAttributes(){
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,10)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .build();
    }

    public void travel(@NotNull Vec3 p_149181_) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), p_149181_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(p_149181_);
        }

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private static class MermaidPathNavigator extends WaterBoundPathNavigation {
        public MermaidPathNavigator(MermaidEntity mermaidEntity, Level p_21480_) {
            super(mermaidEntity,p_21480_);
        }

        protected boolean canUpdatePath() {
            return true;
        }

        protected @NotNull PathFinder createPathFinder(int p_149222_) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
            return new PathFinder(this.nodeEvaluator, p_149222_);
        }

        public boolean isStableDestination(BlockPos p_149224_) {
            return !this.level.getBlockState(p_149224_.below()).isAir();
        }
    }
}
