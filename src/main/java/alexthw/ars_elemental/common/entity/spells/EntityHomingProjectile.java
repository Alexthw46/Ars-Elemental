package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class EntityHomingProjectile extends EntityProjectileSpell {

    List<Predicate<LivingEntity>> ignore;
    LivingEntity target;

    public void setIgnored(List<Predicate<LivingEntity>> ignore) {
        this.ignore = ignore;
    }
    public List<Predicate<LivingEntity>> getIgnored(){return this.ignore;}

    @Override
    public int getExpirationTime() {
        return 20 * 30;
    }

    public EntityHomingProjectile(Level world, SpellResolver resolver) {
        super(ModEntities.HOMING_PROJECTILE.get(), world, resolver);
    }

    public EntityHomingProjectile(EntityType<EntityHomingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * ((float) Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float) Math.PI / 180F));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float) Math.PI / 180F));
        float f2 = Mth.cos(rotationYawIn * ((float) Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float) Math.PI / 180F));
        this.shoot(f, f1, f2, velocity, inaccuracy);
    }

    @Override
    protected void attemptRemoval() {
        super.attemptRemoval();
        if (this.pierceLeft >= 0) {
            this.age += 300;
        }
    }

    @Override
    public void tickNextPosition() {
        if (!this.isRemoved()) {

            if ((target != null) && (!target.isAlive() || (target.distanceToSqr(this) > 50))) target = null;

            if (target == null && tickCount % 5 == 0) {


                List<LivingEntity> entities;
                if (getOwner() instanceof Player) {
                    entities = level.getEntitiesOfClass(LivingEntity.class,
                            this.getBoundingBox().inflate(4), (LivingEntity e) -> shouldTarget(e) && glowCheck(e));
                } else {
                    entities = level.getEntitiesOfClass(LivingEntity.class,
                            this.getBoundingBox().inflate(4), this::shouldTarget);
                }
                //update target or keep going
                if (entities.isEmpty() && target == null) {
                    super.tickNextPosition();
                } else if (!entities.isEmpty()) {
                    target = entities.stream().filter(e -> e.distanceToSqr(this) < 50).min(Comparator.comparingDouble(e -> e.distanceToSqr(this))).orElse(target);
                }
            }

            if (target != null) {
                homeTo(target.blockPosition());
            } else {
                super.tickNextPosition();
            }

        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        boolean b = super.canHitEntity(entity);
        if (entity instanceof LivingEntity) b &= shouldTarget((LivingEntity) entity);
        return b;
    }

    private boolean shouldTarget(LivingEntity e) {
        if (ignore == null) return false;
        for (Predicate<LivingEntity> p : getIgnored()) {
            if (p.test(e)) {
                return false;
            }
        }
        return true;
    }

    private void homeTo(BlockPos dest) {

        double posX = getX();
        double posY = getY();
        double posZ = getZ();
        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (dest.getX() != 0 || dest.getY() != 0 || dest.getZ() != 0){
            double targetX = dest.getX()+0.5;
            double targetY = dest.getY()+0.75;
            double targetZ = dest.getZ()+0.5;
            Vec3 targetVector = new Vec3(targetX-posX,targetY-posY,targetZ-posZ);
            double length = targetVector.length();
            targetVector = targetVector.scale(0.3/length);
            double weight  = 0;
            if (length <= 3){
                weight = (3.0 - length) * 0.3;
            }

            motionX = (0.9-weight)*motionX+(0.1+weight)*targetVector.x;
            motionY = (0.9-weight)*motionY+(0.1+weight)*targetVector.y;
            motionZ = (0.9-weight)*motionZ+(0.1+weight)*targetVector.z;
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        this.setPos(posX, posY, posZ);

        this.setDeltaMovement(motionX, motionY, motionZ);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.HOMING_PROJECTILE.get();
    }

    @Override
    protected void onHit(HitResult result) {

        if (!level.isClientSide && result instanceof BlockHitResult blockRaytraceResult && !this.isRemoved() && !hitList.contains(((BlockHitResult) result).getBlockPos())) {

            BlockState state = level.getBlockState(blockRaytraceResult.getBlockPos());
            if (state.getBlock() == Blocks.NETHER_PORTAL || state.getBlock() == Blocks.END_PORTAL) return;
        }

        super.onHit(result);
    }

    public static boolean glowCheck(LivingEntity e) {
        if (ConfigHandler.Common.HOMING_GLOWING.get()) {
            return e.hasEffect(MobEffects.GLOWING);
        }
        return true;
    }
}