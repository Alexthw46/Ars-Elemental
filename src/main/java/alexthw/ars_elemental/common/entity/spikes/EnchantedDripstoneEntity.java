package alexthw.ars_elemental.common.entity.spikes;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.util.CompatUtils;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.entity.EnchantedFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import org.jetbrains.annotations.NotNull;

public class EnchantedDripstoneEntity extends EnchantedFallingBlock {

    boolean icy;

    public EnchantedDripstoneEntity(EntityType<? extends EnchantedFallingBlock> entityType, Level level, boolean ice) {
        super(entityType, level);
        dropItem = false;
        this.icy = ice;
    }

    public EnchantedDripstoneEntity(Level world, BlockPos pos, SpellResolver resolver, SpellStats spellStats) {
        super(world, pos, Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN), resolver);
        dropItem = false;
        this.context = resolver.spellContext;
        this.spellStats = spellStats;
        this.icy = CompatUtils.waterCheck(resolver);
        this.setXRot(0);
        this.setYRot(0);
    }

    public EnchantedDripstoneEntity(EntityType<EnchantedDripstoneEntity> enchantedDripstoneEntityEntityType, Level level) {
        this(enchantedDripstoneEntityEntityType, level, false);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return icy ? ModEntities.THROWN_ICE_SPIKE.get() : ModEntities.THROWN_SPIKE.get();
    }

    public @NotNull DamageSource getDamageSource(Entity owner, Entity entity) {
        DamageSource damagesource;
        if (owner == null) {
            damagesource = level.damageSources().fallingStalactite(this);
        } else {
            damagesource = level.damageSources().fallingStalactite(owner);
            if (owner instanceof LivingEntity livingOwner) {
                livingOwner.setLastHurtMob(entity);
            }
        }
        return damagesource;
    }

}
