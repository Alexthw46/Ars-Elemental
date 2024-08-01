package alexthw.ars_elemental.util;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import vazkii.botania.api.block.PetalApothecary;
//
//public class BotaniaCompat {
//
//    // This is a helper method to check if a block is a Petal Apothecary and if it is empty, fill it with water.
//    public static boolean tryFillApothecary(BlockPos pos, Level world) {
//        if (world.getBlockEntity(pos) instanceof PetalApothecary apothecary) {
//            if (apothecary.getFluid() == PetalApothecary.State.EMPTY) {
//                apothecary.setFluid(PetalApothecary.State.WATER);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean isApothecary(BlockPos pos, Level world) {
//        return world.getBlockEntity(pos) instanceof PetalApothecary;
//    }
//
//
//}
