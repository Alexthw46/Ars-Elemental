package alexthw.ars_elemental.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityCarryMEI extends MobEffectInstance {

    protected LivingEntity owner;
    protected LivingEntity target;

    public EntityCarryMEI(Holder<MobEffect> effect, int duration, int amp, LivingEntity owner, LivingEntity afflicted){
        this(effect, duration, amp, false, true, owner, afflicted);
    }

    public EntityCarryMEI(Holder<MobEffect> effect, int duration, int amp, boolean ambient, boolean show, LivingEntity owner, @Nullable LivingEntity afflicted) {
        super(effect, duration, amp, show, ambient, show);
        this.owner = owner;
        this.target = afflicted;
    }

    //Override to update the owner and target if they have changed
    @Override
    public boolean update(@NotNull MobEffectInstance pOther) {

        if (pOther instanceof EntityCarryMEI toUpdate){

            if (this.owner != toUpdate.getOwner()){
                owner = toUpdate.getOwner();
            }

            if (this.target != toUpdate.getTarget()){
                target = toUpdate.getTarget();
            }

        }

        return super.update(pOther);
    }

    public LivingEntity getOwner() {
        return owner;
    }

    public LivingEntity getTarget() {
        return target;
    }
}
