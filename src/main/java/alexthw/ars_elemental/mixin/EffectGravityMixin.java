package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.items.ISchoolItem;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectGravity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static alexthw.ars_elemental.common.entity.spells.EntityMagnetSpell.createMagnet;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

@Mixin(EffectGravity.class)
public abstract class EffectGravityMixin {

    @Shadow(remap = false) public abstract int getDefaultManaCost();

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;

        if (rayTraceResult.getEntity() instanceof LivingEntity && (ISchoolItem.hasFocus(world, shooter) == ELEMENTAL_EARTH) && spellStats.hasBuff(AugmentSensitive.INSTANCE)) {
            createMagnet(world, shooter, spellStats, spellContext, rayTraceResult.getLocation());
            spellContext.getSpell().setCost(spellContext.getSpell().getCastingCost() + getDefaultManaCost() * 2);
            ci.cancel();
        }
    }

    @Inject(method = "onResolveBlock", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {

        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;

        if (spellStats.hasBuff(AugmentSensitive.INSTANCE) && ISchoolItem.hasFocus(world, shooter) == ELEMENTAL_EARTH) {
            createMagnet(world, shooter, spellStats, spellContext, rayTraceResult.getLocation());
            spellContext.getSpell().setCost(spellContext.getSpell().getCastingCost() + getDefaultManaCost() * 2);
            ci.cancel();
        }

    }

}
