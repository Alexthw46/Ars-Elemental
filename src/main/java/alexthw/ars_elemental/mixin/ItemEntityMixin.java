package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.common.components.ElementProtectionFlag;
import alexthw.ars_elemental.registry.ModRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Shadow public abstract void setUnlimitedLifetime();

    @WrapOperation(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public boolean protectSpellbook(ItemEntity instance, DamageSource damageSource, Operation<Boolean> original) {
        ElementProtectionFlag tag = getItem().get(ModRegistry.P4E);
        if (tag != null && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && tag.flag())
            return true;
        return original.call(instance, damageSource);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "RETURN"))
    public void unlimitedLifetimeIfProtected(EntityType entityType, Level level, CallbackInfo ci) {
        ElementProtectionFlag tag = getItem().get(ModRegistry.P4E);
        if (tag != null && tag.flag()) {
            this.setUnlimitedLifetime();
        }
    }
}
