package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.entity.spells.EntityMagnetSpell;
import alexthw.ars_elemental.common.items.ISchoolItem;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectPull;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

@Mixin(EffectPull.class)
public class EffectPullMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;

        if (rayTraceResult.getEntity() instanceof LivingEntity living && (ISchoolItem.hasFocus(world, shooter) == ELEMENTAL_EARTH) && (spellStats.hasBuff(AugmentExtendTime.INSTANCE) || spellStats.hasBuff(AugmentDurationDown.INSTANCE))) {
            EntityMagnetSpell magnet = new EntityMagnetSpell(world);
            magnet.setPos(living.getPosition(1));
            magnet.setAoe((int) spellStats.getAmpMultiplier());
            magnet.setOwner(shooter);
            magnet.extendedTime = spellStats.getDurationMultiplier();
            world.addFreshEntity(magnet);
            Spell spell = spellContext.getSpell();
            spell.setCost((int) (spell.getCastingCost() * 1.5F));
            ci.cancel();
        }
    }

}
