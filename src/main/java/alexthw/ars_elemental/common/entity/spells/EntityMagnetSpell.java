package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.EntityLingeringSpell;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityMagnetSpell extends EntityLingeringSpell {

    public EntityMagnetSpell(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityMagnetSpell(Level worldIn) {
        super(ModEntities.LINGER_MAGNET.get(), worldIn);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.LINGER_MAGNET.get();
    }

    @Override
    public int getAoe() {
        return super.getAoe()/2;
    }

    @Override
    public void tick() {

        age++;

        if (this.age > getExpirationTime()){
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        if (level.isClientSide() && this.age > getParticleDelay()) {
            playParticles();
        }
        if (!level.isClientSide() && this.age % 5 == 0){
            for(Entity entity : level.getEntities(null, new AABB(this.blockPosition()).inflate(getAoe()))) {
                if(entity.equals(this) || entity instanceof EntityLingeringSpell || entity.equals(getOwner()))
                    continue;
                Vec3 vec3d = new Vec3(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
                entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.normalize()));
                entity.hurtMarked = true;
            }
        }

    }


}
