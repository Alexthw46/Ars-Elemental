package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.common.components.ElementProtectionFlag;
import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemEntity.class, priority = 1846) //random higher value to avoid conflict
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public boolean protectSpellbook(ItemEntity instance, DamageSource damageSource) {
        ElementProtectionFlag tag = getItem().get(ModRegistry.P4E);
        if (tag != null && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && tag.flag())
            return true;
        return instance.isInvulnerableTo(damageSource);
    }

}
