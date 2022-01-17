package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.items.ElementalFocus;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EffectIgnite.class)
public class EffectIgniteMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (rayTraceResult.getEntity() instanceof LivingEntity living && ElementalFocus.hasFocus(world, shooter) == ModRegistry.FIRE_FOCUS.get() && shooter!=living){
            living.addEffect(new MobEffectInstance(ModRegistry.HELLFIRE.get(), 200, (int) spellStats.getAmpMultiplier()/2));
            living.invulnerableTime = 0;
        }
    }
}
