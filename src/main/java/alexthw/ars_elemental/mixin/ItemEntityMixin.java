package alexthw.ars_elemental.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import shadowed.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(value = ItemEntity.class, priority = 1846) //random higher value to avoid conflict
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @ModifyReturnValue(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public boolean protectSpellbook(boolean original) {
        CompoundTag tag = getItem().getTag();
        if (tag != null && tag.contains("ae_netherite")) return original || tag.getBoolean("ae_netherite");
        return original;
    }

}
