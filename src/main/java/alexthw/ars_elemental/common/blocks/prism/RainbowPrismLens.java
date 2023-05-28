package alexthw.ars_elemental.common.blocks.prism;

import com.hollingsworth.arsnouveau.client.particle.RainbowParticleColor;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.hollingsworth.arsnouveau.common.entity.ColoredProjectile.PARTICLE_TAG;

public class RainbowPrismLens extends AbstractPrismLens {

    public RainbowPrismLens(Properties pProperties) {
        super(pProperties, "rgb");
    }

    @Override
    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        // change the color of the spell to a random color
        var tag = spell.getParticleColor().serialize();
        tag.putString("type", RainbowParticleColor.ID.toString());
        spell.getEntityData().set(PARTICLE_TAG, tag);
        super.shoot(world, pos, spell, angle);
    }

    @Override
    public boolean canConvert(EntityProjectileSpell spell, Level level, BlockPos pos) {
        return !spell.isRainbow();
    }

}
