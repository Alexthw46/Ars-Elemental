package alexthw.ars_elemental.client.flashjack;

import alexthw.ars_elemental.common.entity.familiars.FlashjackFamiliar;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class FlashJackFamiliarRenderer<M extends FlashjackFamiliar> extends FlashJackRenderer<M> {
    public FlashJackFamiliarRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
