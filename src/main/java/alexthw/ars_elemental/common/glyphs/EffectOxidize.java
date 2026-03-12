package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentRandomize;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EffectOxidize extends ElementalAbstractEffect implements IPotionEffect {

    public static final EffectOxidize INSTANCE = new EffectOxidize("oxidize", "Oxidize");

    public EffectOxidize(String tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Oxidizes the target's armor, temporarily reducing its armor value. If used on blocks it might speed up the oxidation process.";
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        // Aoe Block logic with oxidation
        BlockPos pos = rayTraceResult.getBlockPos();
        BlockState state;
        double aoeBuff = spellStats.getAoeMultiplier();
        int pierceBuff = spellStats.getBuffCount(AugmentPierce.INSTANCE);
        List<BlockPos> posList = SpellUtil.calcAOEBlocks(shooter, pos, rayTraceResult, aoeBuff, pierceBuff);
        for (BlockPos pos1 : posList) {
            if (world.isOutsideBuildHeight(pos1) || world.random.nextFloat() < spellStats.getBuffCount(AugmentRandomize.INSTANCE) * 0.25F) {
                continue;
            }
            state = world.getBlockState(pos1);

            if (!BlockUtil.destroyRespectsClaim(getPlayer(shooter, (ServerLevel) world), world, pos1)) {
                continue;
            }

            //Try oxidize
            if (state.getBlock() instanceof ChangeOverTimeBlock<?> toOxidize && toOxidize.getNext(state).isPresent()) {
                if (world.getRandom().nextFloat() < 0.1)
                    world.setBlockAndUpdate(pos, toOxidize.getNext(state).get());
            }

        }
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        // Apply status
        if (rayTraceResult.getEntity() instanceof LivingEntity livingEntity) {
            this.applyConfigPotion(livingEntity, ModPotions.RUST, spellStats);
        }
    }

    @Override
    protected @NotNull Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_WATER, SpellSchools.ELEMENTAL_AIR);
    }

    @Override
    protected int getDefaultManaCost() {
        return 80;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE, AugmentPierce.INSTANCE, AugmentAOE.INSTANCE);
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        addBlockAoeAugmentDescriptions(map);
        addPotionAugmentDescriptions(map);
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDefaultPotionConfig(builder);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 30 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 8 : EXTEND_TIME.get();
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }
}
