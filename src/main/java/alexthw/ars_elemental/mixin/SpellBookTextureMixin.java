package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.item.SpellBookRenderer;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SpellBookRenderer.class)
public class SpellBookTextureMixin {

    ItemStack currentStack = ItemStack.EMPTY;

    @Inject(at = @At("HEAD"), method = "render", remap = false)
    public void setCurrentItemStack(Item animatable, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, ItemStack itemStack, ItemTransforms.TransformType transformType, CallbackInfo ci) {
        currentStack = itemStack;
    }

    @Inject(method = "getTextureLocation(Lcom/hollingsworth/arsnouveau/common/items/SpellBook;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), remap = false, cancellable = true)
    public void getTextureLocation(SpellBook o, CallbackInfoReturnable<ResourceLocation> cir) {
        CompoundTag book = currentStack.getTag();
        if (ConfigHandler.Client.NetheriteTexture.get() && book != null && book.contains("ae_netherite")) {
            String base = "textures/items/sbn/spellbook_";
            String color = !book.contains("color") ? "purple" : DyeColor.byId(book.getInt("color")).getName();
            var newTex = new ResourceLocation(ArsNouveau.MODID, base + color + ".png");
            cir.setReturnValue(newTex);
        }
    }

}
