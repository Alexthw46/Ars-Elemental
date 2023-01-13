package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class MethodHomingProjectile extends ElementalAbstractForm {

    public static MethodHomingProjectile INSTANCE = new MethodHomingProjectile();

    public MethodHomingProjectile() {
        super("homing_projectile", "Homing Projectile");
    }

    public void summonProjectiles(Level world, LivingEntity shooter, SpellStats stats, SpellResolver resolver, List<Predicate<LivingEntity>> ignore) {

        int numSplits = stats.getBuffCount(AugmentSplit.INSTANCE);

        List<EntityHomingProjectile> projectiles = new ArrayList<>();
        for (int i = 0; i < 1 + numSplits; i++) {
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
            projectiles.add(spell);
        }
        float velocity = getProjectileSpeed(stats);
        int opposite = -1;
        int counter = 0;
        for (EntityHomingProjectile proj : projectiles) {
            proj.setIgnored(ignore);
            proj.shoot(shooter, shooter.getXRot(), shooter.getYRot() + Math.round(counter / 2.0) * 5 * opposite, 0.0F, velocity, 0.8f);
            opposite = opposite * -1;
            counter++;
            world.addFreshEntity(proj);
        }
    }

    public static float getProjectileSpeed(SpellStats stats) {
        return Math.max(0.2F, 0.5F + stats.getAccMultiplier() / 5.0F);
    }

    public static void splits(Level world, LivingEntity shooter, BlockPos position, SpellResolver resolver, ArrayList<EntityHomingProjectile> projectiles, int numSplits) {
        float sizeRatio = shooter.getEyeHeight() / Player.DEFAULT_EYE_HEIGHT;
        for (int i = 1; i < numSplits + 1; i++) {
            Direction offset = shooter.getDirection().getClockWise();
            if (i % 2 == 0) offset = offset.getOpposite();
            BlockPos projPos = position.relative(offset, i / 2).offset(0, 1.5 * sizeRatio, 0);
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }
    }

    @Override
    public CastResolveType onCast(ItemStack stack, LivingEntity shooter, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {

        List<Predicate<LivingEntity>> ignore = basicIgnores(shooter, spellStats.hasBuff(AugmentSensitive.INSTANCE), resolver.spell);

        if (shooter instanceof Player) {
            ignore.add(entity -> entity instanceof ISummon summon && shooter.getUUID().equals(summon.getOwnerID()));
            ignore.add(entity -> entity instanceof OwnableEntity pet && shooter.equals(pet.getOwner()));
        } else if (shooter instanceof ISummon summon && summon.getOwnerID() != null) {
            ignore.add(entity -> entity instanceof ISummon summon2 && summon.getOwnerID().equals(summon2.getOwnerID()));
            ignore.add(entity -> entity instanceof OwnableEntity pet && summon.getOwnerID().equals(pet.getOwnerUUID()));
        }

        summonProjectiles(world, shooter, spellStats, resolver, ignore);

        return CastResolveType.SUCCESS;
    }

    /**
     * Cast by players
     */
    @Override
    public CastResolveType onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Level world = context.getLevel();
        Player shooter = context.getPlayer();
        onCast(null, shooter, world, spellStats, spellContext, resolver);
        return CastResolveType.SUCCESS;
    }

    /**
     * Cast by others.
     * */
    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        return CastResolveType.FAILURE;
    }

    @Override
    public CastResolveType onCastOnEntity(ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        onCast(stack, caster, caster.level, spellStats, spellContext, resolver);
        return CastResolveType.SUCCESS;
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
    protected Map<ResourceLocation, Integer> getDefaultAugmentLimits(Map<ResourceLocation, Integer> map) {
        map.put(AugmentPierce.INSTANCE.getRegistryName(), 1);
        return map;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentPierce.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE, AugmentDecelerate.INSTANCE, AugmentSensitive.INSTANCE);
    }

    public static List<Predicate<LivingEntity>> basicIgnores(LivingEntity shooter, Boolean targetPlayers, Spell spell) {
        List<Predicate<LivingEntity>> ignore = new ArrayList<>();

        ignore.add((entity -> !entity.isAlive()));
        ignore.add((entity -> entity == shooter));
        ignore.add(entity -> entity instanceof FamiliarEntity);
        ignore.add(shooter::isAlliedTo);
        if (!targetPlayers) {
            ignore.add(entity -> entity instanceof Player);
        }
        Set<IFilter> filters = GlyphEffectUtil.getFilters(spell.recipe, 0);
        if (!filters.isEmpty()){
            ignore.add(entity -> GlyphEffectUtil.checkIgnoreFilters(entity, filters));
        }
        return ignore;
    }

}
