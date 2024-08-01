package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.renderer.item.SpellBookRenderer;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Mixin(value = SpellBookRenderer.class)
public class SpellBookTextureMixin extends GeoItemRenderer<SpellBook> {

    public SpellBookTextureMixin(GeoModel<SpellBook> model) {
        super(model);
    }

    @Inject(method = "getTextureLocation(Lcom/hollingsworth/arsnouveau/common/items/SpellBook;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), remap = false, cancellable = true)
    public void getTextureLocation(SpellBook o, CallbackInfoReturnable<ResourceLocation> cir) {
        var book = this.getCurrentItemStack().get(ModRegistry.P4E);
        if (ConfigHandler.Client.NetheriteTexture.get() && book != null && book.flag()) {
            var bookColor = this.getCurrentItemStack().get(DataComponents.BASE_COLOR);
            String base = "textures/item/sbn/spellbook_";
            String color = bookColor == null ? "purple" : bookColor.getName();
            var newTex = ResourceLocation.fromNamespaceAndPath(ArsNouveau.MODID, base + color + ".png");
            cir.setReturnValue(newTex);
        }
    }

}
