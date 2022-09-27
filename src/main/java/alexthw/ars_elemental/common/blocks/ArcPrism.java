package alexthw.ars_elemental.common.blocks;

import alexthw.ars_elemental.common.entity.spells.EntityCurvedProjectile;
import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import com.hollingsworth.arsnouveau.common.block.SpellPrismBlock;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDecelerate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ArcPrism extends SpellPrismBlock {

    public ArcPrism(Properties properties) {
        super(properties);
    }

    @Override
    public void onHit(ServerLevel world, BlockPos pos, EntityProjectileSpell spell) {
        if (spell instanceof EntityCurvedProjectile) {
            super.onHit(world, pos, spell);
            return;
        }
        EntityCurvedProjectile newProjectile = new EntityCurvedProjectile(world, spell.spellResolver);

        Position iposition = getDispensePosition(new BlockSourceImpl(world, pos));
        Direction direction = world.getBlockState(pos).getValue(DispenserBlock.FACING);

        newProjectile.setPos(iposition.x(), iposition.y(), iposition.z());
        newProjectile.prismRedirect++;
        if(spell.prismRedirect >= 3){
            ANCriteriaTriggers.rewardNearbyPlayers(ANCriteriaTriggers.PRISMATIC, world, pos, 10);
        }
        if (spell.spellResolver == null) {
            spell.remove(Entity.RemovalReason.DISCARDED);
            return;
        }
        float acceleration = (spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentAccelerate.INSTANCE) - spell.spellResolver.spell.getBuffsAtIndex(0, null, AugmentDecelerate.INSTANCE) * 0.5F);
        float velocity = Math.max(0.1f, 0.5f + 0.1f * Math.min(2, acceleration));

        newProjectile.shoot(direction.getStepX(), (direction.getStepY()), direction.getStepZ(), velocity, 0);
        world.addFreshEntity(newProjectile);
        spell.discard();

        for (Direction d : Direction.values()) {
            BlockPos adjacentPos = pos.relative(d);
            if (world.getBlockState(adjacentPos).getBlock() instanceof ObserverBlock) {
                BlockState observer = world.getBlockState(adjacentPos);
                if (adjacentPos.relative(observer.getValue(FACING)).equals(pos)) { // Make sure the observer is facing us.
                    world.scheduleTick(pos.relative(d), world.getBlockState(pos.relative(d)).getBlock(), 2);
                }
            }
        }
    }
}
