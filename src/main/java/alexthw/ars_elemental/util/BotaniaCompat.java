package alexthw.ars_elemental.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import vazkii.botania.api.block.IPetalApothecary;

public class BotaniaCompat {

    public static boolean tryFillApothecary(BlockPos pos, Level world){
        if (world.getBlockEntity(pos) instanceof IPetalApothecary apothecary){
            apothecary.setFluid(IPetalApothecary.State.WATER);
            return true;
        }
        return false;
    }


}
