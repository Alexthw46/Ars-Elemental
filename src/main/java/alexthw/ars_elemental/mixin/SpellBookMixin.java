package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.common.items.SpellBook;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SpellBook.class)
public class SpellBookMixin {

    @Inject(at = @At("HEAD"),method = "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V")
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (stack.hasTag() && stack.getTag().contains("ae_netherite"))
            tooltip.add(Component.translatable("tooltip.ars_nouveau.blessed").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)));
    }

}
