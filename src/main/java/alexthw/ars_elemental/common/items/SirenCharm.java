package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.entity.MermaidEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class SirenCharm extends Item {

    public SirenCharm(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Called when this item is used when targeting a Block
     */
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide)
            return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        if (world.getBlockState(pos).getBlock() == Blocks.PRISMARINE) {
            world.setBlockAndUpdate(pos, ModRegistry.MERMAID_ROCK.get().defaultBlockState());
            context.getItemInHand().shrink(1);
        }else {
            MermaidEntity mermaid = new MermaidEntity(world, false);
            Vec3 vec = context.getClickLocation();
            mermaid.setPos(vec.x, vec.y, vec.z);
            world.addFreshEntity(mermaid);
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
