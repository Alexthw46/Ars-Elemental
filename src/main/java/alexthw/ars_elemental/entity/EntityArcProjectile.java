package alexthw.ars_elemental.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityArcProjectile extends EntityProjectileSpell {


    public EntityArcProjectile(EntityType<? extends EntityProjectileSpell> entityType, World world) {
        super(entityType, world);
        setNoGravity(false);
    }

    public EntityArcProjectile(World world, SpellResolver resolver) {
        super(world,resolver);
    }

    @Override
    public EntityType<?> getType() {
        return ModRegistry.ARC_PROJECTILE.get();
    }

    @Override
    public void tick() {
        age++;

        if(this.age > 1200 || (!level.isClientSide && spellResolver == null)){
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
        Vector3d motion = getDeltaMovement();
        setDeltaMovement(motion.x * 0.96, (motion.y > 0 ? motion.y * 0.96 : motion.y) - 0.03f, motion.z * 0.96);
        Vector3d pos = position();
        setPos(pos.x + motion.x, pos.y + motion.y, pos.z + motion.z);
    }

    @Override
    protected void attemptRemoval() {
        this.pierceLeft--;
        if ( this.pierceLeft < 0 ){
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }else{
            Vector3d vel = getDeltaMovement();
            setDeltaMovement(vel.x(), -0.9 * vel.y(), vel.z());
        }
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(rotationYawIn * ((float)Math.PI / 180F)) * MathHelper.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, 0, inaccuracy);
        Vector3d vec3d = entityThrower.getLookAngle();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3d.x, vec3d.y, vec3d.z).scale(velocity));
    }

}
