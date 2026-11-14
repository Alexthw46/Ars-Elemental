package alexthw.ars_elemental.client.phalanx;

import alexthw.ars_elemental.ArsNouveauRegistry;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.ModelProperty;
import com.hollingsworth.arsnouveau.api.particle.timelines.ProjectileTimeline;
import com.hollingsworth.arsnouveau.api.registry.ParticlePropertyRegistry;
import com.hollingsworth.arsnouveau.client.renderer.entity.StyledProjectileModel;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CPStyledProjectileModel extends StyledProjectileModel {

    public static final Map<ModelProperty.Model, ResourceLocation> ModelToAnim = new HashMap<>();

    @Override
    public ResourceLocation getAnimationResource(EntityProjectileSpell animatable) {
        ModelProperty.Model resource = getModelProp(animatable).selectedResource;
        var animation = ModelToAnim.getOrDefault(resource, null);
        return animation != null ? animation : ANIMATIONS;
    }

    public ModelProperty getModelProp(EntityProjectileSpell projectileSpell) {
        ProjectileTimeline timeline = projectileSpell.resolver().spell.particleTimeline().get(ArsNouveauRegistry.PHALANX_TIMELINE.get());
        return timeline.trailEffect.motion().propertyMap.get(ParticlePropertyRegistry.MODEL_PROPERTY.get());
    }

    public ResourceLocation getModelRes(EntityProjectileSpell projectileSpell) {
        ProjectileTimeline timeline = projectileSpell.resolver().spell.particleTimeline().get(ArsNouveauRegistry.PHALANX_TIMELINE.get());
        ModelProperty modelProperty = timeline.trailEffect.motion().propertyMap.get(ParticlePropertyRegistry.MODEL_PROPERTY.get());
        return modelProperty.selectedResource.resourceLocation();
    }
}
