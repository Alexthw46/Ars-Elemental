package alexthw.ars_elemental.common.entity.spikes;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.entity.ColoredProjectile;
import com.hollingsworth.arsnouveau.common.entity.EnchantedFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import org.jetbrains.annotations.NotNull;

public class EnchantedDripstoneEntity extends EnchantedFallingBlock {

    public EnchantedDripstoneEntity(EntityType<? extends ColoredProjectile> entityType, Level level) {
        super(entityType, level);
        dropItem = false;
    }

    public EnchantedDripstoneEntity(Level world, BlockPos pos, SpellResolver resolver, SpellStats spellStats) {
        super(world, pos, Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN), resolver);
        dropItem = false;
        this.context = resolver.spellContext;
        this.spellStats = spellStats;
        this.setXRot(0);
        this.setYRot(0);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.THROWN_SPIKE.get();
    }

}
