package alexthw.ars_elemental.glyphs;

import alexthw.ars_elemental.entity.EntityArcProjectile;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MethodArcProj extends AbstractCastMethod {

    public static MethodArcProj INSTANCE = new MethodArcProj();

    public MethodArcProj(){
        super("arc_projectile", "Arc Projectile");
    }

    public void summonProjectiles(World world, PlayerEntity shooter, List<AbstractAugment> augments, SpellResolver resolver){

        ArrayList<EntityArcProjectile> projectiles = new ArrayList<>();
        EntityArcProjectile projectileSpell = new EntityArcProjectile(world, resolver);
        projectiles.add(projectileSpell);
        int numSplits = getBuffCount(augments,AugmentSplit.class);

        for(int i =1; i < numSplits + 1; i++){
            Direction offset =shooter.getDirection().getClockWise();
            if(i%2==0) offset = offset.getOpposite();
            BlockPos projPos = shooter.blockPosition().relative(offset, i);
            projPos = projPos.offset(0, 1.5, 0);
            EntityArcProjectile spell = new EntityArcProjectile(world, resolver);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }

        float velocity = 0.8F + getBuffCount(augments,AugmentAccelerate.class)/ 10.0F;

        for(EntityArcProjectile proj : projectiles) {
            proj.setPos(proj.getX(), proj.getY()+ 0.25, proj.getZ());
            proj.shoot(shooter, shooter.yRot, shooter.yRot, 0.0F, velocity, 0.3f);
            world.addFreshEntity(proj);
        }
    }

    @Override
    public void onCast(ItemStack itemStack, LivingEntity shooter, World world, List<AbstractAugment> list, SpellContext spellContext, SpellResolver resolver) {
        summonProjectiles(world, (PlayerEntity) shooter, list, resolver);
        resolver.expendMana(shooter);
    }

    @Override
    public void onCastOnBlock(ItemUseContext context, List<AbstractAugment> list, SpellContext spellStats, SpellResolver resolver) {
        World world = context.getLevel();
        PlayerEntity shooter = context.getPlayer();
        summonProjectiles(world, shooter, list, resolver);
        resolver.expendMana(shooter);
    }


    //ignore
    @Override
    public void onCastOnBlock(BlockRayTraceResult blockRayTraceResult, LivingEntity livingEntity, List<AbstractAugment> list, SpellContext spellContext, SpellResolver spellResolver) {

    }

    @Override
    public void onCastOnEntity(ItemStack itemStack, LivingEntity livingEntity, Entity entity, Hand hand, List<AbstractAugment> list, SpellContext spellContext, SpellResolver spellResolver) {

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
        return 10;
    }


    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentPierce.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE);
    }

}
