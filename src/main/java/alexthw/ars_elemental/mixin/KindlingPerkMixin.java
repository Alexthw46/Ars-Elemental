package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.perk.PerkInstance;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.perk.IgnitePerk;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IgnitePerk.class)
public class KindlingPerkMixin {

    @Inject(method = "onPreResolve", at = @At("HEAD"), remap = false)
    private void onPreResolve(HitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver, AbstractEffect effect, PerkInstance perkInstance, CallbackInfo ci) {
        if (effect instanceof IDamageEffect damageEffect && rayTraceResult instanceof EntityHitResult entityHitResult && perkInstance.getSlot() == PerkSlot.THREE) {
            if (damageEffect.canDamage(shooter, spellStats, spellContext, resolver, entityHitResult.getEntity()) && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addEffect(new MobEffectInstance(ModPotions.MAGIC_FIRE.get(), 20), livingEntity);
            }
        }
    }

}
