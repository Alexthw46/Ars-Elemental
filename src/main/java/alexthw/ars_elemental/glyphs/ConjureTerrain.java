package alexthw.ars_elemental.glyphs;

import com.hollingsworth.arsnouveau.GlyphLib;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSmelt;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class ConjureTerrain extends AbstractEffect {

    public static ConjureTerrain INSTANCE = new ConjureTerrain();

    public ConjureTerrain() {
        super("conjure_terrain", "Conjure Dirt");
    }

    @Override
    public void onResolveBlock(BlockRayTraceResult rayTraceResult, World world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        BlockState toPlace = spellStats.hasBuff(AugmentAmplify.INSTANCE) ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.DIRT.defaultBlockState();

        for (int i = spellContext.getCurrentIndex(); i < spellContext.getSpell().getSpellSize(); i++) {
            AbstractSpellPart next = spellContext.nextSpell();
                if (next instanceof AbstractAugment) continue;
                if (next instanceof AbstractEffect) {
                    if (next == EffectCrush.INSTANCE) {
                        toPlace = spellStats.hasBuff(AugmentAmplify.INSTANCE) ? Blocks.SANDSTONE.defaultBlockState() : Blocks.SAND.defaultBlockState();
                        spellContext.setCanceled(true);
                    } else if (next == EffectSmelt.INSTANCE && spellStats.hasBuff(AugmentAmplify.INSTANCE)) {
                        toPlace = Blocks.STONE.defaultBlockState();
                        Spell spell = spellContext.getSpell();
                        spell.setCost((int) (spell.getCastingCost() * 1.5F));
                        spellContext.setCanceled(true);
                    }
                }
                break;
        }
        GlyphEffectUtil.placeBlocks(rayTraceResult, world, shooter, spellStats, toPlace);
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentAOE.INSTANCE, AugmentPierce.INSTANCE);
    }

    @Override
    protected Map<String, Integer> getDefaultAugmentLimits() {
        Map<String, Integer> map = super.getDefaultAugmentLimits();
        map.put(GlyphLib.AugmentAmplifyID, 1);
        return map;
    }

    @Override
    public String getBookDescription() {
        return "Places terrain block at a location. Can place more blocks if augmented with AoE or Pierce";
    }

    @Override
    public Set<SpellSchool> getSchools() {
        return this.setOf(SpellSchools.ELEMENTAL_EARTH,SpellSchools.CONJURATION);
    }

    public Tier getTier() {
        return Tier.ONE;
    }

    public Item getCraftingReagent() {
        return Items.DIRT;
    }

}