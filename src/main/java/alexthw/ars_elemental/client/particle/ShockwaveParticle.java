package alexthw.ars_elemental.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class ShockwaveParticle extends HugeExplosionParticle {

    protected ShockwaveParticle(ClientLevel level, double x, double y, double z, double quadSizeMultiplier, SpriteSet sprites, boolean smaller) {
        super(level, x, y, z, quadSizeMultiplier, sprites);
        this.lifetime = 16;
        this.quadSize = smaller ? 1.5F : 2.5F;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {

        Quaternionf q = new Quaternionf();

        // vanilla camera setup
        this.getFacingCameraMode().setRotation(q, renderInfo, partialTicks);

        // now rotate quad onto horizontal plane
        q.rotateX((float) Math.toRadians(90));

        if (this.roll != 0.0F) {
            q.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
        }

        this.renderRotatedQuad(buffer, renderInfo, q, partialTicks);
    }

    @Override
    public int getLightColor(float partialTick) {
        float f = ((float) this.age + partialTick) / (float) this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    protected void renderRotatedQuad(@NotNull VertexConsumer buffer,
                                     @NotNull Quaternionf quaternion,
                                     float x, float y, float z,
                                     float partialTicks) {

        float size = this.getQuadSize(partialTicks);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);

        // normal face
        this.renderVertex(buffer, quaternion, x, y, z, 1, -1, size, u1, v1, light);
        this.renderVertex(buffer, quaternion, x, y, z, 1, 1, size, u1, v0, light);
        this.renderVertex(buffer, quaternion, x, y, z, -1, 1, size, u0, v0, light);
        this.renderVertex(buffer, quaternion, x, y, z, -1, -1, size, u0, v1, light);

        // reversed face (double sided)
        this.renderVertex(buffer, quaternion, x, y, z, -1, -1, size, u0, v1, light);
        this.renderVertex(buffer, quaternion, x, y, z, -1, 1, size, u0, v0, light);
        this.renderVertex(buffer, quaternion, x, y, z, 1, 1, size, u1, v0, light);
        this.renderVertex(buffer, quaternion, x, y, z, 1, -1, size, u1, v1, light);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                @NotNull SimpleParticleType type,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new ShockwaveParticle(level, x, y, z, xSpeed, this.sprites, false);
        }
    }

    public static class ProviderSmall implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public ProviderSmall(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(
                @NotNull SimpleParticleType type,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new ShockwaveParticle(level, x, y, z, xSpeed, this.sprites, true);
        }
    }


}
