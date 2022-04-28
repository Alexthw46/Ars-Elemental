package alexthw.ars_elemental.glyphs;

import alexthw.ars_elemental.FiltersCompat;
import alexthw.ars_elemental.entity.EntityHomingProjectile;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AbstractEffectFilter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class MethodHomingProj extends AbstractCastMethod {

    public static MethodHomingProj INSTANCE = new MethodHomingProj();

    public MethodHomingProj(){
        super("homing_projectile", "Homing Projectile");
    }

    public void summonProjectiles(World world, LivingEntity shooter, List<Predicate<LivingEntity>> ignore, List<AbstractAugment> augments,SpellResolver resolver){

        ArrayList<EntityHomingProjectile> projectiles = new ArrayList<>();
        EntityHomingProjectile projectileSpell = new EntityHomingProjectile(world, resolver, ignore);
        projectiles.add(projectileSpell);
        int numSplits = getBuffCount(augments,AugmentSplit.class);

        for(int i =1; i < numSplits + 1; i++){
            Direction offset =shooter.getDirection().getClockWise();
            if(i%2==0) offset = offset.getOpposite();
            BlockPos projPos = shooter.blockPosition().relative(offset, i/2);
            projPos = projPos.offset(0, 1.5, 0);
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver, ignore);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }

        float velocity = 0.5F + getBuffCount(augments,AugmentAccelerate.class)/ 10.0F;

        for(EntityHomingProjectile proj : projectiles) {
            proj.shoot(shooter, shooter.yRot, shooter.yRot, 0.0F, velocity, 0.8f);
            world.addFreshEntity(proj);
        }
    }

    @Override
    public void onCast(ItemStack itemStack, LivingEntity shooter, World world, List<AbstractAugment> list, SpellContext spellContext, SpellResolver resolver) {
        List<Predicate<LivingEntity>> ignore = new ArrayList<>();
        ignore.add(livingEntity -> !livingEntity.isAlive());
        ignore.add(livingEntity -> livingEntity == shooter);
        ignore.add(shooter::isAlliedTo);
        if (!list.contains(AugmentSensitive.INSTANCE))
            ignore.add(livingEntity -> livingEntity instanceof PlayerEntity);
        ignore.add(livingEntity -> livingEntity instanceof FamiliarEntity);
        if (ModList.get().isLoaded("toomanyglyphs")){
            Set<AbstractEffectFilter> filters = FiltersCompat.getFilters(spellContext.getSpell().recipe);
            if (!filters.isEmpty()) {
                ignore.add(livingEntity -> !FiltersCompat.checkFilters(livingEntity, filters));
            }
        }
        if (shooter instanceof PlayerEntity) {
            ignore.add(livingEntity -> livingEntity instanceof ISummon && shooter.getUUID().equals(((ISummon)livingEntity).getOwnerID()));
            resolver.expendMana(shooter);
        }else if (shooter instanceof ISummon && ((ISummon) shooter).getOwnerID() != null) {
            ignore.add(livingEntity -> livingEntity.getUUID().equals(((ISummon) shooter).getOwnerID()));
            ignore.add(livingEntity -> livingEntity instanceof ISummon && ((ISummon) shooter).getOwnerID().equals(((ISummon)livingEntity).getOwnerID()));
        }
        summonProjectiles(world, shooter, ignore, list, resolver);

    }

    @Override
    public void onCastOnBlock(ItemUseContext context, List<AbstractAugment> list, SpellContext spellContext, SpellResolver resolver) {
        World world = context.getLevel();
        PlayerEntity shooter = context.getPlayer();
        onCast(ItemStack.EMPTY, shooter, world, list, spellContext, resolver);
        resolver.expendMana(shooter);
    }


    //bookwyrm
    @Override
    public void onCastOnBlock(BlockRayTraceResult blockRayTraceResult, LivingEntity livingEntity, List<AbstractAugment> list, SpellContext spellContext, SpellResolver spellResolver) {

    }

    @Override
    public void onCastOnEntity(ItemStack itemStack, LivingEntity livingEntity, Entity entity, Hand hand, List<AbstractAugment> list, SpellContext spellContext, SpellResolver spellResolver) {
        onCast(itemStack, livingEntity, livingEntity.level, list, spellContext, spellResolver);
    }

    @Override
    public boolean wouldCastSuccessfully(ItemStack itemStack, LivingEntity livingEntity, World world, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(ItemUseContext itemUseContext, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(BlockRayTraceResult blockRayTraceResult, LivingEntity livingEntity, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public boolean wouldCastOnEntitySuccessfully(ItemStack itemStack, LivingEntity livingEntity, Entity entity, Hand hand, List<AbstractAugment> list, SpellResolver spellResolver) {
        return false;
    }

    @Override
    public int getManaCost() {
        return 40;
    }

    public Tier getTier() {
        return Tier.THREE;
    }


    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentPierce.INSTANCE, AugmentSensitive.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE);
    }

}
