package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.EntityHomingProjectile;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSplit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
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
import java.util.Set;

public class MethodHomingProjectile extends AbstractCastMethod {

    public static MethodHomingProjectile INSTANCE = new MethodHomingProjectile();

    public MethodHomingProjectile() {
        super("homing_projectile", "Curved Projectile");
    }

    public void summonProjectiles(Level world, LivingEntity shooter, SpellStats stats, SpellResolver resolver){
        ArrayList<EntityHomingProjectile> projectiles = new ArrayList<>();
        EntityHomingProjectile projectileSpell = new EntityHomingProjectile(world, resolver);
        projectiles.add(projectileSpell);
        int numSplits = stats.getBuffCount(AugmentSplit.INSTANCE);

        for(int i =1; i < numSplits + 1; i++){
            Direction offset = shooter.getDirection().getClockWise();
            if(i%2==0) offset = offset.getOpposite();
            // Alternate sides
            BlockPos projPos = shooter.blockPosition().relative(offset, i);
            projPos = projPos.offset(0, 1.5, 0);
            EntityHomingProjectile spell = new EntityHomingProjectile(world, resolver);
            spell.setPos(projPos.getX(), projPos.getY(), projPos.getZ());
            projectiles.add(spell);
        }

        float velocity = 1.0f + stats.getBuffCount(AugmentAccelerate.INSTANCE);

        for(EntityHomingProjectile proj : projectiles) {
            proj.shoot(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, velocity * 0.7F, 0.8f);
            world.addFreshEntity(proj);
        }
    }


    // Summons the projectiles directly above the block, facing downwards.
    public void summonProjectiles(Level world, BlockPos pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver){

    }

    @Override
    public void onCast(ItemStack stack, LivingEntity shooter, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {
        summonProjectiles(world, shooter, spellStats, resolver);
        resolver.expendMana(shooter);
    }

    @Override
    public void onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        Level world = context.getLevel();
        Player shooter = context.getPlayer();
        summonProjectiles(world, shooter, spellStats, resolver);
        resolver.expendMana(shooter);
    }

    /**
     * Cast by entities.
     */
    @Override
    public void onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        caster.lookAt(EntityAnchorArgument.Anchor.EYES, blockRayTraceResult.getLocation().add(0, 0, 0));
        summonProjectiles(caster.getCommandSenderWorld(), blockRayTraceResult.getBlockPos(), caster, spellStats, resolver);
        resolver.expendMana(caster);
    }

    @Override
    public void onCastOnEntity(ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        summonProjectiles(caster.getCommandSenderWorld(), caster, spellStats, resolver);
        resolver.expendMana(caster);
    }

    @Override
    public boolean wouldCastSuccessfully(@Nullable ItemStack stack, LivingEntity playerEntity, Level world, SpellStats spellStats, SpellResolver resolver) {
        return true;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(UseOnContext context, SpellStats spellStats, SpellResolver resolver) {
        return true;
    }

    @Override
    public boolean wouldCastOnBlockSuccessfully(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellResolver resolver) {
        return true;
    }

    @Override
    public boolean wouldCastOnEntitySuccessfully(@Nullable ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellResolver resolver) {
        return true;
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentPierce.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE, AugmentSensitive.INSTANCE);
    }
}
