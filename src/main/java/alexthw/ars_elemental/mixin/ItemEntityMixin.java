package alexthw.ars_elemental.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public boolean protectSpellbook(ItemEntity instance, DamageSource damageSource) {
        CompoundTag tag = instance.getItem().getTag();
        if (tag != null && tag.contains("ae_netherite")) return tag.getBoolean("ae_netherite");
        return instance.isInvulnerableTo(damageSource);
    }

}
