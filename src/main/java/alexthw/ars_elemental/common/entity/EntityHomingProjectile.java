package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class EntityHomingProjectile extends EntityProjectileSpell {

    LivingEntity owner;
    LivingEntity target;

    public EntityHomingProjectile(Level worldIn, LivingEntity shooter) {
        super(ModRegistry.HOMING_PROJECTILE.get(), worldIn, shooter);
        this.owner = shooter;
    }

    public EntityHomingProjectile(Level world, SpellResolver resolver) {
        super(ModRegistry.HOMING_PROJECTILE.get(), world, resolver);
    }

    public EntityHomingProjectile(Level world, LivingEntity shooter, SpellResolver resolver) {
        this(world,resolver);
        this.owner = shooter;
    }

    public EntityHomingProjectile(EntityType<EntityHomingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tickNextPosition() {
        if (!this.isRemoved()) {

            if ((target != null) && (!target.isAlive() || (target.distanceToSqr(this) > 50))) target = null;

            if (target == null && tickCount % 5 == 0) {

                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(4),
                        (e) -> e != owner && e.isAlive());

                //update target or keep going
                if (entities.isEmpty() && target == null) {
                    super.tickNextPosition();
                } else if (!entities.isEmpty()) {
                    target = entities.stream().filter(e -> e.distanceToSqr(this) < 50).min(Comparator.comparingDouble(e -> e.distanceToSqr(this))).orElse(target);
                }
            }

            if (target != null){
                homeTo(target.blockPosition());
            }else{
                super.tickNextPosition();
            }

        }
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
            double targetY = dest.getY()+0.5;
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
        return ModRegistry.HOMING_PROJECTILE.get();
    }

    @Override
    public boolean save(CompoundTag pCompound) {
        pCompound.putUUID("owner", owner.getUUID());
        return super.save(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.owner = getCommandSenderWorld().getPlayerByUUID(tag.getUUID("owner"));
    }
}