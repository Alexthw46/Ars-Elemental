package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.common.entity.familiar.FlyingFamiliarEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
    }

    public FlashjackFamiliar(Level world) {
        super(ModEntities.FLASHJACK_FAMILIAR.get(), world);
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
}
