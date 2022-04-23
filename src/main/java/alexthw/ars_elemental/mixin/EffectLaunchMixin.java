package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.items.ISchoolItem;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
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

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_AIR;

@Mixin(EffectLaunch.class)
public class EffectLaunchMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;

        if (rayTraceResult.getEntity() instanceof LivingEntity living && (spellStats.hasBuff(AugmentExtendTime.INSTANCE) || spellStats.hasBuff(AugmentDurationDown.INSTANCE)) && (ISchoolItem.hasFocus(world, shooter) == ELEMENTAL_AIR) ) {
            living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, (int) (50 * (1 + spellStats.getDurationMultiplier())), (int) spellStats.getAmpMultiplier() / 2));
        }

    }

}