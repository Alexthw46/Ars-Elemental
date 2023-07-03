package alexthw.ars_elemental.client.mermaid;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import alexthw.ars_elemental.common.entity.familiars.MermaidFamiliar;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MermaidRenderer<T extends LivingEntity & GeoEntity> extends GeoEntityRenderer<T> {

    public MermaidRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MermaidModel<>());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T instance) {
        if (instance instanceof MermaidEntity var) return var.getTexture(var);
        if (instance instanceof MermaidFamiliar var) return var.getTexture(var);
        return super.getTextureLocation(instance);
    }


    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(texture);
    }

}
