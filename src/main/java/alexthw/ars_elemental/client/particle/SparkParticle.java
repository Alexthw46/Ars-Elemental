package alexthw.ars_elemental.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SparkParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    private SparkParticle(ClientLevel levelIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, SpriteSet spriteSet) {
        super(levelIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        lifetime = 8;

        this.spriteSet = spriteSet;
        pickSprite(spriteSet);
        xd = xSpeedIn;
        yd = ySpeedIn;
        zd = zSpeedIn;
        scale(0.75F);
        oRoll = roll = random.nextFloat() * 2 * (float) Math.PI;
    }

    @Override
    public void tick() {
        super.tick();
        if ((age & 1) == 0) {
            pickSprite(spriteSet);
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float pTicks) {
        return 0x00F000F0;
    }

    @Nonnull
    public static ParticleProvider<SimpleParticleType> factory(SpriteSet spriteSet) {
        return (data, level, x, y, z, dx, dy, dz) -> new SparkParticle(level, x, y, z, dx, dy, dz, spriteSet);
    }

}