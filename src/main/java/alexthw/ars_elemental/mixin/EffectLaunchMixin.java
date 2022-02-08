package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.items.ElementalFocus;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectLaunch;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(EffectLaunch.class)
public class EffectLaunchMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;

        if (rayTraceResult.getEntity() instanceof LivingEntity living && (ElementalFocus.hasFocus(world, shooter) == ModRegistry.AIR_FOCUS.get()) && (spellStats.hasBuff(AugmentExtendTime.INSTANCE) || spellStats.hasBuff(AugmentDurationDown.INSTANCE))){
            living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, (int) (50 * (1 + spellStats.getDurationMultiplier())), (int) spellStats.getAmpMultiplier()/2) );
        }

    }

    @Inject(method = "getCompatibleAugments", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void getCompatibleAugments(CallbackInfoReturnable<Set<AbstractAugment>> cir) {
        cir.setReturnValue(augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE));
    }

    protected Set<AbstractAugment> augmentSetOf(AbstractAugment... augments) {
        return Set.of(augments);
    }
}