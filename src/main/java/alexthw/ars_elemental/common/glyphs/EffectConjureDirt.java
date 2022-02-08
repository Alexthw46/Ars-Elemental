package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class EffectConjureDirt extends AbstractEffect {

    public static EffectConjureDirt INSTANCE = new EffectConjureDirt();

    private EffectConjureDirt() {
        super("conjure_dirt", "Conjure Dirt");
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        BlockState toPlace = spellStats.hasBuff(AugmentAmplify.INSTANCE) ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.DIRT.defaultBlockState();
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
    public String getBookDescription() {
        return "Places dirt at a location. Can place more blocks if augmented with AoE or Pierce";
    }

    @Override
    public Item getCraftingReagent() {
        return Items.DIRT;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.ONE;
    }

    @Nonnull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION,SpellSchools.ELEMENTAL_EARTH);
    }
}
