package alexthw.ars_elemental.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class PosCarryMEI extends MobEffectInstance {

    public BlockPos getOrigin() {
        return origin;
    }

    BlockPos origin;

    public PosCarryMEI(MobEffect pEffect, int duration, int amp, boolean ambient, boolean show, BlockPos origin) {
        super(pEffect, duration, amp, ambient, show);
        this.origin = origin;
    }

    @Override
    public boolean update(MobEffectInstance pOther) {

        if (pOther instanceof PosCarryMEI other) {
            this.origin = other.getOrigin();
        }

        return super.update(pOther);
    }
}
