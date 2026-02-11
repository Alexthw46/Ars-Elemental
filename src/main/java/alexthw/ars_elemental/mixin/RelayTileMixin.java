package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.api.source.AbstractSourceMachine;
import com.hollingsworth.arsnouveau.common.block.tile.RelayTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RelayTile.class)
public abstract class RelayTileMixin extends AbstractSourceMachine {

    public RelayTileMixin(BlockEntityType<?> manaTile, BlockPos pos, BlockState state) {
        super(manaTile, pos, state);
    }

    @Inject(method = "getTooltip", at = @At("TAIL"), remap = false)
    public void ars_elemental$getTooltip(List<Component> tooltip, CallbackInfo ci) {
        tooltip.add(Component.literal("Source Buffer " + this.getSource() + '/' + this.getMaxSource()));
        tooltip.add(Component.literal("Transfer Rate " + this.getTransferRate() + " Source/s"));
    }

}
