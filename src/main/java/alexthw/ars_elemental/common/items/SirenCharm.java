package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import com.hollingsworth.arsnouveau.common.entity.Starbuncle;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SirenCharm extends Item {

    public SirenCharm(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Called when this item is used when targetting a Block
     */
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide)
            return InteractionResult.SUCCESS;
        Level world = context.getLevel();
        MermaidEntity mermaid = new MermaidEntity(world,true);
        Vec3 vec = context.getClickLocation();
        mermaid.setPos(vec.x, vec.y, vec.z);
        world.addFreshEntity(mermaid);
        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
