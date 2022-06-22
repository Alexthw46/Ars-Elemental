package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import com.hollingsworth.arsnouveau.api.item.AbstractSummonCharm;
import com.hollingsworth.arsnouveau.common.block.tile.SummoningTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FirenandoCharm extends AbstractSummonCharm {

    public FirenandoCharm(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOnBlock(UseOnContext context, Level world, BlockPos pos) {
        FirenandoEntity firenando = new FirenandoEntity(world);
        Vec3 vec = context.getClickLocation();
        firenando.setPos(vec.x, vec.y, vec.z);
        firenando.setHome(pos);
        firenando.setOwner(context.getPlayer());
        world.addFreshEntity(firenando);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOnSummonTile(UseOnContext context, Level world, SummoningTile tile, BlockPos pos) {
        return useOnBlock(context, world, pos);
    }

}
