package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.item.ElementalFocus;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectIgnite.class)
public class EffectIgniteMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false)
    public void onResolveEntity(EntityRayTraceResult rayTraceResult, World world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (rayTraceResult.getEntity() instanceof LivingEntity && ElementalFocus.hasFocus(world, shooter) == ModRegistry.FIRE_FOCUS.get()){
            LivingEntity living = (LivingEntity) rayTraceResult.getEntity();
            living.addEffect(new EffectInstance(ModRegistry.HELLFIRE.get(), 200, (int) spellStats.getAmpMultiplier()/2));
            living.invulnerableTime = 0;
        }
    }
}
