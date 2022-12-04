package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.common.entity.LightningEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import shadowed.llamalad7.mixinextras.injector.WrapWithCondition;

@Mixin(LightningEntity.class)
public class LightningMixin {

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;thunderHit(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LightningBolt;)V"))
    public boolean thunderHit(Entity instance, ServerLevel pLevel, LightningBolt pLightning) {
        return !(instance instanceof ItemEntity);
    }

}
