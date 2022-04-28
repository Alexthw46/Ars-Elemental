package alexthw.ars_elemental.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class EntityHomingProjectile extends EntityProjectileSpell {
    LivingEntity target;
    List<Predicate<LivingEntity>> ignore;

    public EntityHomingProjectile(World world, SpellResolver resolver, List<Predicate<LivingEntity>> ignored) {
        this(world,resolver);
        this.ignore = ignored;
    }

    public EntityHomingProjectile(World world, SpellResolver resolver) {
        super(world, resolver);
    }

    public EntityHomingProjectile(EntityType<EntityHomingProjectile> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public EntityType<?> getType() {
        return ModRegistry.HOMING_PROJECTILE.get();
    }

    @Override
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, 0f, inaccuracy);
        Vector3d vec3d = entityThrower.getLookAngle();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3d.x, vec3d.y, vec3d.z).scale(velocity));
    }

    @Override
    public void tick() {
        age++;

        if(this.age > 600 || (!level.isClientSide && spellResolver == null)){
            this.remove();
            return;
        }


        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();


        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();
        if (this.inGround) {
            this.inGround = false;
            this.setDeltaMovement(this.getDeltaMovement());
        }

        Vector3d vector3d2 = this.position();
        Vector3d vector3d3 = vector3d2.add(this.getDeltaMovement());
        RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(vector3d2, vector3d3, this.numSensitive > 0 ? RayTraceContext.BlockMode.OUTLINE : RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
            vector3d3 = raytraceresult.getLocation();
        }

        EntityRayTraceResult entityraytraceresult = this.findHitEntity(vector3d2, vector3d3);
        if (entityraytraceresult != null) {
            raytraceresult = entityraytraceresult;
        }

        if (raytraceresult instanceof EntityRayTraceResult) {
            Entity entity = ((EntityRayTraceResult)raytraceresult).getEntity();
            Entity entity1 = this.getOwner();
            if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity)entity1).canHarmPlayer((PlayerEntity)entity)) {
                raytraceresult = null;
            }
        }

        if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
            this.hasImpulse = true;
        }

        if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.MISS && raytraceresult instanceof BlockRayTraceResult) {
            BlockRegistry.PORTAL_BLOCK.onProjectileHit(this.level, this.level.getBlockState(new BlockPos(raytraceresult.getLocation())), (BlockRayTraceResult)raytraceresult, this);
        }

        tickNextPosition();

        if (this.level.isClientSide && this.age > 2) {
            for(int i = 0; i < 10; ++i) {
                double deltaX = this.getX() - this.xOld;
                double deltaY = this.getY() - this.yOld;
                double deltaZ = this.getZ() - this.zOld;
                double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 8.0D);

                for(double j = 0.0D; j < dist; ++j) {
                    double coeff = j / dist;
                    this.level.addParticle(GlowParticleData.createData(this.getParticleColor()), (float)(this.xo + deltaX * coeff), (float)(this.yo + deltaY * coeff), (float)(this.zo + deltaZ * coeff), 0.0125F * (this.random.nextFloat() - 0.5F), 0.0125F * (this.random.nextFloat() - 0.5F), 0.0125F * (this.random.nextFloat() - 0.5F));
                }
            }
        }
    }

    public void tickNextPosition() {
        if (this.isAlive()) {

            if ((target != null) && (!target.isAlive() || (target.distanceToSqr(this) > 50))) target = null;

            if (target == null && tickCount % 5 == 0) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(4), this::shouldTarget);

                //update target or keep going
                if (entities.isEmpty() && target == null) {
                    oldTick();
                } else if (!entities.isEmpty()) {
                    target = entities.stream().filter(e -> e.distanceToSqr(this) < 50).min(Comparator.comparingDouble(e -> e.distanceToSqr(this))).orElse(target);
                }
            }

            if (target != null){
                homeTo(target.blockPosition());
            }else{
                oldTick();
            }

        }
    }

    private void oldTick(){
        Vector3d vec3d = this.getDeltaMovement();
        double x = this.getX() + vec3d.x;
        double y = this.getY() + vec3d.y;
        double z = this.getZ() + vec3d.z;
        if (!this.isNoGravity()) {
            Vector3d vec3d1 = this.getDeltaMovement();
            this.setDeltaMovement(vec3d1.x, vec3d1.y, vec3d1.z);
        }

        this.setPos(x, y, z);
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
            Vector3d targetVector = new Vector3d(targetX-posX,targetY-posY,targetZ-posZ);
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
    protected boolean canHitEntity(Entity p_230298_1_) {
        if (p_230298_1_ instanceof LivingEntity) return shouldTarget((LivingEntity) p_230298_1_) && super.canHitEntity(p_230298_1_);
        return super.canHitEntity(p_230298_1_);
    }

    private boolean shouldTarget(LivingEntity e) {
        if (ignore != null && ignore.stream().anyMatch(p -> p.test(e))) {
            return false;
        }
        return e != getOwner() && e.isAlive();
    }

    @Override
    public boolean save(CompoundNBT pCompound) {
        return super.save(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
    }

}
