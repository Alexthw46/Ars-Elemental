package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCurvedProjectile extends EntityProjectileSpell {


    public EntityCurvedProjectile(EntityType<? extends EntityProjectileSpell> entityType, Level world) {
        super(entityType, world);
        isNoGravity = false;
    }

    public EntityCurvedProjectile(Level world, SpellResolver resolver) {
        super(ModRegistry.CURVED_PROJECTILE.get(), world, resolver);
        isNoGravity = false;
    }

    @Override
    public EntityType<?> getType() {
        return ModRegistry.CURVED_PROJECTILE.get();
    }

    @Override
    public void tickNextPosition() {
        Vec3 motion = getDeltaMovement();
        setDeltaMovement(motion.x * 0.96, (motion.y > 0 ? motion.y * 0.96 : motion.y) - 0.03f, motion.z * 0.96);
        Vec3 pos = position();
        setPos(pos.x + motion.x, pos.y + motion.y, pos.z + motion.z);
    }

    @Override
    protected void attemptRemoval() {
        this.pierceLeft--;
        if ( this.pierceLeft < 0 ){
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove(RemovalReason.DISCARDED);
        }else{
            Vec3 vel = getDeltaMovement();
            setDeltaMovement(vel.x(), -vel.y(), vel.z());
       }
    }




}
