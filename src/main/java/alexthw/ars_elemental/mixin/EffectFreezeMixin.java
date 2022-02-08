package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.items.ElementalFocus;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectFreeze.class)
public class EffectFreezeMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;
        if (rayTraceResult.getEntity() instanceof LivingEntity living) {
            if (ElementalFocus.hasFocus(world, shooter) == ModRegistry.WATER_FOCUS.get()) {
                living.setIsInPowderSnow(true);
                living.setTicksFrozen((int) (living.getTicksFrozen() + 60 * spellStats.getAmpMultiplier()));
                living.invulnerableTime = 0;
            }
            if (living.hasEffect(ModRegistry.HELLFIRE.get())){
                living.removeEffect(ModRegistry.HELLFIRE.get());
            }
        }
    }

}
