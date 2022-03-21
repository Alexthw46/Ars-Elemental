package alexthw.ars_elemental;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.item.IPetalApothecary;

public class BotaniaCompat {

    public static boolean tryFillApothecary(BlockPos blockPos, World world) {
        TileEntity apothecary = world.getBlockEntity(blockPos);
        if (apothecary instanceof IPetalApothecary){
            ((IPetalApothecary)apothecary).setFluid(IPetalApothecary.State.WATER);
            return true;
        }
        return false;
    }

}
