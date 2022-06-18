package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSmelt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class EffectConjureDirt extends AbstractEffect {

    public static EffectConjureDirt INSTANCE = new EffectConjureDirt();

    private EffectConjureDirt() {
        super("conjure_dirt", "Conjure Dirt");
    }

    @Override
    public boolean isRenderAsIcon() {
        return false;
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        BlockState toPlace = Blocks.DIRT.defaultBlockState();
        if (spellStats.hasBuff(AugmentAmplify.INSTANCE)) toPlace = Blocks.COBBLESTONE.defaultBlockState();
        if (spellContext.hasNextPart()) {
            while (spellContext.hasNextPart()) {
                AbstractSpellPart next = spellContext.nextPart();

                if (next instanceof AbstractEffect) {
                    if (next == EffectCrush.INSTANCE) {
                        toPlace = spellStats.hasBuff(AugmentAmplify.INSTANCE) ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SAND.defaultBlockState();
                    } else if (next == EffectSmelt.INSTANCE && spellStats.hasBuff(AugmentAmplify.INSTANCE)) {
                        toPlace = Blocks.STONE.defaultBlockState();
                        Spell spell = spellContext.getSpell();
                        spell.setCost((int) (spell.getCastingCost() * 1.5F));
                    } else {
                        spellContext.setCurrentIndex(spellContext.getCurrentIndex() - 1);
                    }
                    break;
                }
            }
        }
        GlyphEffectUtil.placeBlocks(rayTraceResult, world, shooter, spellStats, toPlace);
    }

    @Override
    public int getDefaultManaCost() {
        return 20;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAOE.INSTANCE, AugmentPierce.INSTANCE, AugmentAmplify.INSTANCE);
    }

    @Override
    protected Map<String, Integer> getDefaultAugmentLimits() {
        Map<String, Integer> map = super.getDefaultAugmentLimits();
        map.put(GlyphLib.AugmentAmplifyID, 1);
        return map;
    }

    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION, SpellSchools.ELEMENTAL_EARTH);
    }

    @Override
    public String getBookDescription() {
        return "Places terrain block at a location. Can place more blocks if augmented with AoE or Pierce";
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

}
