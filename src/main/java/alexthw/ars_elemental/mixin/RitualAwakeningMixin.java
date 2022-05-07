package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.ritual.RitualAwakening;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(RitualAwakening.class)
public abstract class RitualAwakeningMixin {

    @Shadow(remap = false)
    EntityType<WealdWalker> entity;
    @Shadow(remap = false)
    BlockPos foundPos;

    @Shadow(remap = false)
    public abstract void destroyTree(Level world, Set<BlockPos> set);

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/hollingsworth/arsnouveau/common/ritual/RitualAwakening;findTargets(Lnet/minecraft/world/level/Level;)V"), remap = false)
    public void findFlashing(RitualAwakening instance, Level level) {
        if (instance.getPos() == null) return;
        for (BlockPos p : BlockPos.betweenClosed(instance.getPos().east(3).south(3).below(1), instance.getPos().west(3).north(3).above(1))) {
            Set<BlockPos> flashing = SpellUtil.DFSBlockstates(level, p, 350, (b) -> b.getBlock() == ModItems.FLASHING_ARCHWOOD_LOG.get() || b.getBlock() == ModItems.FLASHING_LEAVES.get());
            if (flashing.size() >= 50) {
                entity = ModEntities.FLASHING_WEALD_WALKER.get();
                foundPos = p;
                destroyTree(level, flashing);
                return;
            }
        }
        if (entity == null) instance.findTargets(level);
    }


}
