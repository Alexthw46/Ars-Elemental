package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.ColoredProjectile;
import com.hollingsworth.arsnouveau.common.entity.EntityFollowProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.ars_nouveau.geckolib3.core.easing.EasingManager;
import software.bernie.ars_nouveau.geckolib3.core.easing.EasingType;

public class EntityLerpedProjectile extends ColoredProjectile {

    public static final EntityDataAccessor<BlockPos> to = SynchedEntityData.defineId(EntityLerpedProjectile.class, EntityDataSerializers.BLOCK_POS);
    public static final EntityDataAccessor<BlockPos> from = SynchedEntityData.defineId(EntityLerpedProjectile.class, EntityDataSerializers.BLOCK_POS);
    public static final EntityDataAccessor<Float> OFFSET = SynchedEntityData.defineId(EntityLerpedProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DIDOFFSET = SynchedEntityData.defineId(EntityLerpedProjectile.class, EntityDataSerializers.BOOLEAN);

    public int age;
    int maxAge;

    public EntityLerpedProjectile(Level worldIn, BlockPos from, BlockPos to) {
        this(worldIn, new Vec3(from.getX(), from.getY(), from.getZ()), new Vec3(to.getX(), to.getY(), to.getZ()), 255, 25, 180);
    }

    public EntityLerpedProjectile(Level worldIn, BlockPos from, BlockPos to, int r, int g, int b) {
        this(worldIn, new Vec3(from.getX(), from.getY(), from.getZ()), new Vec3(to.getX(), to.getY(), to.getZ()), r, g, b);
    }

    public EntityLerpedProjectile(EntityType<EntityLerpedProjectile> entityAOEProjectileEntityType, Level world) {
        super(entityAOEProjectileEntityType, world);
    }

    public EntityLerpedProjectile(Level worldIn, Vec3 from, Vec3 to, int r, int g, int b) {
        super(ModEntities.LERP_PROJECTILE.get(), worldIn);
        this.entityData.set(EntityFollowProjectile.to, new BlockPos(to));
        this.entityData.set(EntityFollowProjectile.from, new BlockPos(from));
        this.maxAge = (int) Math.floor(from.subtract(to).length() * 5);
        setPos(from.x + 0.5, from.y, from.z + 0.5);
        this.entityData.set(RED, r);
        this.entityData.set(GREEN, g);
        this.entityData.set(BLUE, b);
    }

    public static float lerp(double percentCompleted, double startValue, double endValue, EasingType type) {
        if (percentCompleted >= 1) {
            return (float) endValue;
        }
        percentCompleted = EasingManager.ease(percentCompleted, type, null);
        // current tick / position should be between 0 and 1 and represent the percentage of the lerping that has completed
        return (float) lerpInternal(percentCompleted, startValue,
                endValue);
    }

    public static double lerpInternal(double pct, double start, double end) {
        return start + pct * (end - start);
    }

    /**
     * Calculates a value between 0 and 1, given the precondition that value
     * is between min and max. 0 means value = max, and 1 means value = min.
     */
    public double normalize(double value, double min, double max) {
        return 1.0 - ((value - min) / (max - min));
    }

    boolean wentUp;

    @Override
    public void tick() {
        super.tick();
        this.age++;


        if (age > 400)
            this.remove(RemovalReason.DISCARDED);


        Vec3 vec3d2 = this.getDeltaMovement();
        BlockPos start = entityData.get(from);
        BlockPos end = entityData.get(to);
        if (BlockUtil.distanceFrom(this.blockPosition(), end) < 1 || this.age > 1000 || BlockUtil.distanceFrom(this.blockPosition(), end) > 16) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        double posX = getX();
        double posY;
        double posZ = getZ();


        double time = 1 - normalize(age, 0.0, 80);

        EasingType type = EasingType.EaseOutExpo;

        double startY = start.getY();
        double endY = end.getY() + getDistanceAdjustment(start, end);
        double lerpX = lerp(time, (double) start.getX() + 0.5, (double) end.getX() + 0.5, type);
        double lerpY = lerp(time, lerp(time, startY, endY, type), lerp(time, endY, startY, type), type);
        double lerpZ = lerp(time, (double) start.getZ() + 0.5, (double) end.getZ() + 0.5, type);

        BlockPos adjustedPos = new BlockPos(posX, end.getY(), posZ);
        if (BlockUtil.distanceFrom(adjustedPos, end) <= 0.5) {
            posY = getY() - 0.05;
            this.setPos(lerpX, posY, lerpZ);
        } else {
            this.setPos(lerpX, lerpY, lerpZ);
        }

        if (level.isClientSide && this.age > 1) {
            double deltaX = getX() - xOld;
            double deltaY = getY() - yOld;
            double deltaZ = getZ() - zOld;
            double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 20);
            int counter = 0;

            for (double i = 0; i < dist; i++) {
                double coeff = i / dist;
                counter += level.random.nextInt(3);
                if (counter % (Minecraft.getInstance().options.particles().get().getId() == 0 ? 1 : 2 * Minecraft.getInstance().options.particles().get().getId()) == 0) {
                    level.addParticle(GlowParticleData.createData(
                                    new ParticleColor(this.entityData.get(RED), this.entityData.get(GREEN), this.entityData.get(BLUE))),
                            (float) (xo + deltaX * coeff), (float) (yo + deltaY * coeff), (float) (zo + deltaZ * coeff), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f), 0.0125f * (random.nextFloat() - 0.5f));
                }
            }

        }
    }

    public void setDistanceAdjust(float offset) {
        this.entityData.set(OFFSET, offset);
        this.entityData.set(DIDOFFSET, true);
    }

    private double getDistanceAdjustment(BlockPos start, BlockPos end) {
        if (this.entityData.get(DIDOFFSET))
            return this.entityData.get(OFFSET);
        double distance = BlockUtil.distanceFrom(start, end);
        if (distance <= 1.5)
            return 2.5;

        return 3;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(to, new BlockPos(0, 0, 0));
        this.entityData.define(from, new BlockPos(0, 0, 0));
        this.entityData.define(OFFSET, 0.0f);
        this.entityData.define(DIDOFFSET, false);
    }


    @Override
    public EntityType<?> getType() {
        return ModEntities.LERP_PROJECTILE.get();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
