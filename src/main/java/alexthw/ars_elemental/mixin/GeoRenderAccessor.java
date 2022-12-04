package alexthw.ars_elemental.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.ars_nouveau.geckolib3.renderers.geo.GeoItemRenderer;

@Mixin(value = GeoItemRenderer.class)
public interface GeoRenderAccessor {

    @Accessor(remap = false)
    ItemStack getCurrentItemStack();

}
