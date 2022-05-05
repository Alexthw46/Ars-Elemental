package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.common.block.tile.WhirlisprigTile;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WhirlisprigTile.class)
public class WhirlispirgTileMixin {

    @Redirect(method = "evaluateGrove", at = @At(value = "INVOKE", target = "Lcom/hollingsworth/arsnouveau/common/block/tile/WhirlisprigTile;getScore(Lnet/minecraft/world/level/block/state/BlockState;)I"), remap = false)
    int getWaterScore(BlockState state) {
        if (state.getMaterial() == Material.WATER_PLANT)
            return 1;
        return WhirlisprigTile.getScore(state);
    }

}
