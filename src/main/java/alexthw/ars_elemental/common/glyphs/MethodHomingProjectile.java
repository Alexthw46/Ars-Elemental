package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.common.items.ISchoolItem;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.util.TooManyCompats;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.lib.GlyphLib;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AbstractEffectFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class MethodHomingProjectile extends AbstractCastMethod {

    public static MethodHomingProjectile INSTANCE = new MethodHomingProjectile();

    public MethodHomingProjectile() {
        super("homing_projectile", "Homing Projectile");
    }

    public void summonProjectiles(Level world, LivingEntity shooter, SpellStats stats, SpellResolver resolver, List<Predicate<LivingEntity>> ignore) {

        ArrayList<EntityHomingProjectile> projectiles = new ArrayList<>();
        projectiles.add(new EntityHomingProjectile(world, shooter, resolver));
        int numSplits = stats.getBuffCount(AugmentSplit.INSTANCE);

        if (numSplits > 0) {
            stats.setDamageModifier(stats.getDamageModifier() * 0.75D);
        }

        splits(world, shooter, resolver, projectiles, numSplits);

        float velocity = 0.5F + stats.getBuffCount(AugmentAccelerate.INSTANCE) / 10.0F;

        for (EntityHomingProjectile proj : projectiles) {
            proj.setIgnored(ignore);
            proj.shoot(shooter, shooter.getYRot(), shooter.getYRot(), 0.0F, velocity, 0.8f);
            world.addFreshEntity(proj);
        }
    }

    private void splits(Level world, LivingEntity shooter, SpellResolver resolver, ArrayList<EntityHomingProjectile> projectiles, int numSplits) {
        for (int i = 1; i < numSplits + 1; i++) {
            Direction offset = shooter.getDirection().getClockWise();
            if (i % 2 == 0) offset = offset.getOpposite();
            BlockPos projPos = shooter.blockPosition().relative(offset, i / 2);
            projPos = projPos.offset(0, 1.5, 0);
            EntityHomingProjectile spell = new EntityHomingProjectile(world, shooter, resolver);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }
    }

    @Override
    public void onCast(ItemStack stack, LivingEntity shooter, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {

        List<Predicate<LivingEntity>> ignore = basicIgnores(shooter, !spellStats.hasBuff(AugmentSensitive.INSTANCE));

        if (CompatUtils.tooManyGlyphsLoaded()) {
            Set<AbstractEffectFilter> filters = TooManyCompats.getFilters(resolver.spell.recipe);
            ignore.add(entity -> TooManyCompats.checkFilters(entity, filters));
        }

        if (shooter instanceof Player) {
            ignore.add(entity -> entity instanceof ISummon summon && shooter.getUUID().equals(summon.getOwnerID()));

            resolver.expendMana(shooter);
        } else if (shooter instanceof ISummon summon && summon.getOwnerID() != null) {
            ignore.add(entity -> entity instanceof ISummon summon2 && summon.getOwnerID().equals(summon2.getOwnerID()));
        } else if (shooter instanceof EntityMageBase mage) {
            ignore.add(entity -> entity instanceof EntityMageBase mage2 && mage.school == mage2.school);
            ignore.add(entity -> entity instanceof Player mage3 && mage.school == ISchoolItem.hasFocus(world,mage3));
        }

        summonProjectiles(world, shooter, spellStats, resolver, ignore);

    }

    /**
     * Cast by players
     */
    @Override
    public void onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Level world = context.getLevel();
        Player shooter = context.getPlayer();
        onCast(null, shooter, world, spellStats, spellContext, resolver);
        resolver.expendMana(shooter);
    }

    /**
     * Cast by others.
     */
    @Override
    public void onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
    }

    @Override
    public void onCastOnEntity(ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        onCast(stack, caster, caster.level, spellStats, spellContext, resolver);
    }

    @Override
    public boolean wouldCastSuccessfully(@Nullable ItemStack stack, LivingEntity playerEntity, Level world, SpellStats spellStats, SpellResolver resolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(UseOnContext context, SpellStats spellStats, SpellResolver resolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellResolver resolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnEntitySuccessfully(@Nullable ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellResolver resolver) {
        return false;
    }

    @Override
    public int getDefaultManaCost() {
        return 40;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.THREE;
    }

    @Override
    protected Map<String, Integer> getDefaultAugmentLimits() {
        Map<String, Integer> map = super.getDefaultAugmentLimits();
        map.put(GlyphLib.AugmentPierceID, 1);
        return map;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentPierce.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE, AugmentSensitive.INSTANCE);
    }

    public static List<Predicate<LivingEntity>> basicIgnores(LivingEntity shooter, Boolean targetPlayers){
        List<Predicate<LivingEntity>> ignore = new ArrayList<>();

        ignore.add((entity -> !entity.isAlive()));
        ignore.add((entity -> entity == shooter));
        ignore.add(entity -> entity instanceof FamiliarEntity);
        if (targetPlayers) {
            ignore.add(entity -> entity instanceof Player);
        }
        return ignore;
    }

}
