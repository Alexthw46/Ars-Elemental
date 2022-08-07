package alexthw.ars_elemental.network;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.client.particle.ColoredDynamicTypeData;
import com.hollingsworth.arsnouveau.client.particle.ModParticles;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class RayEffectPacket {

    public Vec3 from;
    public Vec3 to;
    public ParticleColor color;

    public RayEffectPacket(Vec3 from, Vec3 to, ParticleColor colors) {
        this.from = from;
        this.to = to;
        this.color = colors;
    }

    public static void encode(RayEffectPacket msg, FriendlyByteBuf buf) {
        encodePos(buf, msg.from);
        encodePos(buf, msg.to);
        buf.writeInt(msg.color.getColor());
    }

    public static void encodePos(@Nonnull FriendlyByteBuf buf, @Nonnull Vec3 item) {
        buf.writeDouble(item.x);
        buf.writeDouble(item.y);
        buf.writeDouble(item.z);
    }

    public static RayEffectPacket decode(FriendlyByteBuf buf) {
        Vec3 from = decodeVector3d(buf);
        Vec3 to = decodeVector3d(buf);
        int colors = buf.readInt();
        return new RayEffectPacket(from, to, ParticleColor.fromInt(colors));
    }

    public static Vec3 decodeVector3d(@Nonnull FriendlyByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3(x, y, z);
    }


    public static void send(@Nonnull Level level, @Nonnull ParticleColor spellColor, @Nonnull Vec3 fromPoint, @Nonnull Vec3 hitPoint) {
        Vec3 midpoint = fromPoint.add(hitPoint).scale(0.5);
        double radius = 64.0 + fromPoint.distanceTo(midpoint);
        double radiusSqr = radius * radius;

        if (level instanceof ServerLevel serverLevel) {
            RayEffectPacket fx = new RayEffectPacket(fromPoint, hitPoint, spellColor);
            serverLevel.getPlayers(p -> p.distanceToSqr(midpoint) <= radiusSqr)
                    .forEach(p -> NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), fx));
        }
    }

    public static void whenThisPacketIsReceived(RayEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().enqueueWork(() -> {

                Level level = ArsNouveau.proxy.getClientWorld();
                Player player = ArsNouveau.proxy.getPlayer();

                double distance = msg.from.distanceTo(msg.to);
                double start = 0.0, increment = 1.0 / 16.0;
                if (player.position().distanceToSqr(msg.from) < 4.0 && msg.to.subtract(msg.from).normalize().dot(player.getViewVector(1f)) > Mth.SQRT_OF_TWO / 2) {
                    start = Math.min(2.0, distance / 2.0);
                    increment = 1.0 / 8.0;
                }
                for (double d = start; d < distance; d += increment) {
                    double fractionalDistance = d / distance;
                    double speedCoefficient = Mth.lerp(fractionalDistance, 0.2, 0.001);
                    level.addParticle(
                            new ColoredDynamicTypeData(ModParticles.SPARKLE_TYPE.get(), msg.color, 0.5F, 10),
                            Mth.lerp(fractionalDistance, msg.from.x, msg.to.x),
                            Mth.lerp(fractionalDistance, msg.from.y, msg.to.y) + 0.5,
                            Mth.lerp(fractionalDistance, msg.from.z, msg.to.z),
                            (level.random.nextFloat() - 0.5) * speedCoefficient,
                            (level.random.nextFloat() - 0.5) * speedCoefficient,
                            (level.random.nextFloat() - 0.5) * speedCoefficient);
                }


            });
        }
        ctx.get().setPacketHandled(true);
    }

}

