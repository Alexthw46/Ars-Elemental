package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.common.entity.LightningEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightningEntity.class)
public class SafeLightningMixin {

        @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;thunderHit(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/entity/effect/LightningBoltEntity;)V"))
        public void thunderHit(Entity instance, ServerWorld pLevel, LightningBoltEntity pLightning){
            if (instance instanceof ItemEntity) return;
            instance.thunderHit(pLevel,pLightning);
        }

}
