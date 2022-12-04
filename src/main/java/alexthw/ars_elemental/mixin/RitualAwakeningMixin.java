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
import shadowed.llamalad7.mixinextras.injector.WrapWithCondition;

import java.util.Set;

@Mixin(RitualAwakening.class)
public abstract class RitualAwakeningMixin {

    @Shadow(remap = false)
    EntityType<WealdWalker> entity;
    @Shadow(remap = false)
    BlockPos foundPos;

    @Shadow(remap = false)
    public abstract void destroyTree(Level world, Set<BlockPos> set);

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/hollingsworth/arsnouveau/common/ritual/RitualAwakening;findTargets(Lnet/minecraft/world/level/Level;)V"), remap = false)
    public boolean findFlashing(RitualAwakening instance, Level level) {
        if (instance.getPos() == null) return false;
        for (BlockPos p : BlockPos.withinManhattan(instance.getPos(), 3, 1, 3)) {
            Set<BlockPos> flashing = SpellUtil.DFSBlockstates(level, p, 350, (b) -> b.getBlock() == ModItems.FLASHING_ARCHWOOD_LOG.get() || b.getBlock() == ModItems.FLASHING_LEAVES.get());
            if (flashing.size() >= 50) {
                entity = ModEntities.FLASHING_WEALD_WALKER.get();
                foundPos = p;
                destroyTree(level, flashing);
                return false;
            }
        }
        return (entity == null);
    }

}
