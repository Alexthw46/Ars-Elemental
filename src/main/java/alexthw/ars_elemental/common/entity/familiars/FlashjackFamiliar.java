package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.common.entity.familiar.FlyingFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.common.entity.FlashjackEntity.*;

public class FlashjackFamiliar extends FlyingFamiliarEntity {
    public FlashjackFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public FlashjackFamiliar(Level world) {
        super(ModEntities.FLASHJACK_FAMILIAR.get(), world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ParrotWanderGoal(this, 1.0F));
    }

    public boolean isActive() {
        return this.getTarget() != null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "idle_controller", 5, event -> onGround() ? event.setAndContinue(inactive) : event.setAndContinue(idle)));
        data.add(new AnimationController<>(this, "action_controller", 5, event -> {
            // Play the attack animation if the entity has a target, try to not loop it
            if (isActive() && !event.isCurrentAnimation(attack)) {
                return event.setAndContinue(attack);
            }
            // If the entity is on the ground, stop the animation
            if (event.getAnimatable().onGround())
                return PlayState.STOP;
            // If the entity is in the attack animation, continue it until the end
            if (event.isCurrentAnimation(attack) && event.animationTick < 100) {
                return PlayState.CONTINUE;
            }
            // Defaults to flapping animation if in the air and not attacking
            return event.setAndContinue(flapping);
        }));
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!player.level.isClientSide && player.equals(getOwner())) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() == ModItems.FLASHING_POD.get().asItem()) {
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 6000, 0, false, false));
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, false, false));
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public @Nullable ResourceLocation getTexture() {
        return prefix("textures/entity/flashjack.png");
    }

    static class ParrotWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public ParrotWanderGoal(PathfinderMob p_186224_, double p_186225_) {
            super(p_186224_, p_186225_);
        }

        @javax.annotation.Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos.MutableBlockPos mutablePosUP = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutablePosDOWN = new BlockPos.MutableBlockPos();

            for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - (double) 3.0F), Mth.floor(this.mob.getY() - (double) 6.0F), Mth.floor(this.mob.getZ() - (double) 3.0F), Mth.floor(this.mob.getX() + (double) 3.0F), Mth.floor(this.mob.getY() + (double) 6.0F), Mth.floor(this.mob.getZ() + (double) 3.0F))) {
                if (!blockpos.equals(pos)) {
                    BlockState blockstate = this.mob.level().getBlockState(mutablePosDOWN.setWithOffset(pos, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level().isEmptyBlock(pos) && this.mob.level().isEmptyBlock(mutablePosUP.setWithOffset(pos, Direction.UP))) {
                        return Vec3.atBottomCenterOf(pos);
                    }
                }
            }

            return null;
        }
    }
}
