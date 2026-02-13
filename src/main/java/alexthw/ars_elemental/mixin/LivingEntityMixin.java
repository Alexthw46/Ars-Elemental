package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.registry.ModPotions;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @WrapOperation(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"
            )
    )
    private float wrapFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity, Operation<Float> original) {
        float baseFriction = original.call(state, level, pos, entity);
        if (hasEffect(ModPotions.ICE_SLIDE)) {
            return Math.clamp(baseFriction + 0.38f, baseFriction, 1.075f);
        }
        return baseFriction;
    }
}
