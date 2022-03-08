package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class EntityHomingProjectile extends EntityProjectileSpell {

    LivingEntity owner;

    public EntityHomingProjectile(Level worldIn, double x, double y, double z) {
        super(ModRegistry.HOMING_PROJECTILE.get(), worldIn, x, y, z);
    }

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


    @Override
    public void tickNextPosition() {
        if (!this.isRemoved()) {

            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(4),
                    (e) -> e != owner && e.isAlive());

            if (entities.isEmpty()) {
                super.tickNextPosition();
            }
            else{
                LivingEntity nearest = entities.stream().min(Comparator.comparingDouble(e -> e.distanceToSqr(this))).get();
                System.out.println(nearest);
                Vec3 diff = nearest.position().add(0, nearest.getBbHeight() / 2, 0).subtract(position());
                double speed = getDeltaMovement().length();
                Vec3 newmotion = getDeltaMovement().add(diff.normalize().scale(speed)).scale(0.5);
                if (newmotion.length() == 0) newmotion = newmotion.add(0.01, 0, 0); // avoid divide by zero
                setDeltaMovement(newmotion.scale(speed / newmotion.length()));
            }
        }
    }

}
