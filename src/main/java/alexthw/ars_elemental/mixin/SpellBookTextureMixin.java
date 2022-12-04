package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.item.SpellBookRenderer;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SpellBookRenderer.class)
public class SpellBookTextureMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/hollingsworth/arsnouveau/client/renderer/item/SpellBookRenderer;getTextureLocation(Lcom/hollingsworth/arsnouveau/common/items/SpellBook;)Lnet/minecraft/resources/ResourceLocation;"), remap = false)
    public ResourceLocation getTextureLocation(SpellBookRenderer instance, SpellBook o) {
        CompoundTag book = ((GeoRenderAccessor) instance).getCurrentItemStack().getTag();
        if (ConfigHandler.Client.NetheriteTexture.get() && book != null && book.contains("ae_netherite")) {
            String base = "textures/items/sbn/spellbook_";
            String color = !book.contains("color") ? "purple" : DyeColor.byId(book.getInt("color")).getName();
            return new ResourceLocation(ArsNouveau.MODID, base + color + ".png");
        } else return instance.getTextureLocation(o);
    }

}
