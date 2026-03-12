package alexthw.ars_elemental.common.glyphs;

import com.alexthw.sauce.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentRandomize;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectConjureWater;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSmelt;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class EffectConjureTerrain extends ElementalAbstractEffect {

    final static public TagKey<Block> CONJUREABLE_DIRT = BlockTags.create(prefix("conjureable/dirt"));
    final static public TagKey<Block> CONJUREABLE_COBBLE = BlockTags.create(prefix("conjureable/cobble"));
    final static public TagKey<Block> CONJUREABLE_STONE = BlockTags.create(prefix("conjureable/stone"));
    final static public TagKey<Block> CONJUREABLE_SAND = BlockTags.create(prefix("conjureable/sand"));

    public static EffectConjureTerrain INSTANCE = new EffectConjureTerrain();

    private EffectConjureTerrain() {
        super("conjure_terrain", "Conjure Terrain");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        // Get the number of Amplify Augments in the spell and use that to determine the block to place
        // Do not use amplification spellstats to avoid unintended effects
        int amps = spellStats.getBuffCount(AugmentAmplify.INSTANCE);
        Registry<Block> registry = shooter.registryAccess().registryOrThrow(Registries.BLOCK);
        HolderSet.Named<Block> terrain = registry.getTag(CONJUREABLE_DIRT).orElseThrow();
        HolderSet.Named<Block> cobbles = registry.getTag(CONJUREABLE_COBBLE).orElseThrow();
        HolderSet.Named<Block> stones = registry.getTag(CONJUREABLE_STONE).orElseThrow();
        HolderSet.Named<Block> sands = registry.getTag(CONJUREABLE_SAND).orElseThrow();

        Block toPlace = switch (amps) {
            case 1 ->
                    spellStats.isRandomized() ? cobbles.getRandomElement(shooter.getRandom()).orElse(Blocks.COBBLESTONE.builtInRegistryHolder()).value() : Blocks.COBBLESTONE;
            case 2 -> Blocks.COBBLED_DEEPSLATE;
            default ->
                    spellStats.isRandomized() ? terrain.getRandomElement(shooter.getRandom()).orElse(Blocks.DIRT.builtInRegistryHolder()).value() : Blocks.DIRT;
        };

        if (spellContext.hasNextPart()) {
            while (spellContext.hasNextPart()) {
                AbstractSpellPart next = spellContext.nextPart();
                // Skip augments, look for next effect
                if (next instanceof AbstractEffect) {
                    // If the spell contains a Conjure Water effect, place mud instead, and if it contains a Crush effect, place sand instead
                    if (next == EffectConjureWater.INSTANCE && amps == 0) {
                        toPlace = Blocks.MUD;
                    } else if (next == EffectCrush.INSTANCE) {
                        // If the spell contains a Crush effect with an Amplify augment, place sandstone instead
                        toPlace = amps > 0 ? (spellStats.isRandomized() && world.random.nextBoolean() ? Blocks.RED_SANDSTONE : Blocks.SANDSTONE) : spellStats.isRandomized() ? sands.getRandomElement(shooter.getRandom()).orElse(Blocks.SAND.builtInRegistryHolder()).value() : Blocks.SAND;
                    } else if (next == EffectSmelt.INSTANCE) {
                        // Directly cook the cobblestone
                        switch (amps) {
                            case 0 -> {
                                // No effect
                            }
                            case 1 -> {
                                if (spellStats.isRandomized()) {
                                    toPlace = stones.getRandomElement(shooter.getRandom()).orElse(Blocks.STONE.builtInRegistryHolder()).value();
                                }
                            }
                            // Amps > 1
                            default -> toPlace = Blocks.DEEPSLATE;
                        }
                    } else {
                        spellContext.setCurrentIndex(spellContext.getCurrentIndex() - 1);
                    }
                    break;
                }
            }
        }
        GlyphEffectUtil.placeBlocks(rayTraceResult, world, shooter, spellStats, spellContext, resolver, toPlace.defaultBlockState());
    }

    @Override
    public int getDefaultManaCost() {
        return 20;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAOE.INSTANCE, AugmentPierce.INSTANCE, AugmentAmplify.INSTANCE, AugmentRandomize.INSTANCE);
    }

    @Override
    protected void addDefaultAugmentLimits(Map<ResourceLocation, Integer> defaults) {
        defaults.put(AugmentAmplify.INSTANCE.getRegistryName(), 2);
        defaults.put(AugmentRandomize.INSTANCE.getRegistryName(), 1);
    }

    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.CONJURATION, SpellSchools.ELEMENTAL_EARTH);
    }

    @Override
    public String getBookDescription() {
        return "Places Dirt or other terrain blocks. Can be augmented with AoE and Pierce to place more blocks, one Amplify to place cobblestone, two for cobbled deepslate, or one of the following combinations: Followed by ConjureWater, it will place Mud. If augmented with Amplify(s) and followed by Smelt, it will place Stone or Deepslate. If followed by Crush it will place Sand or, if amplified, Sandstone.";
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        addBlockAoeAugmentDescriptions(map);
        map.put(AugmentAmplify.INSTANCE, "Changes Dirt to Cobblestone to Cobbled Deepslate, or Sand to Sandstone.");
        map.put(AugmentRandomize.INSTANCE, "Uses a variant of the terrain block, ex. Red Sand instead of sand or Andesite in place of stone.");
    }

}
