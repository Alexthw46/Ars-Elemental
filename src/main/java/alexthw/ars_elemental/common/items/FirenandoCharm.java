package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FirenandoCharm extends Item {

    public FirenandoCharm(Properties props){
        super(props);
    }

    /**
     * Called when this item is used when targeting a Block
     */
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide)
            return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        FirenandoEntity firenando = new FirenandoEntity(world);
        Vec3 vec = context.getClickLocation();
        firenando.setPos(vec.x, vec.y, vec.z);
        firenando.setHome(pos);
        world.addFreshEntity(firenando);
        context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }


}
