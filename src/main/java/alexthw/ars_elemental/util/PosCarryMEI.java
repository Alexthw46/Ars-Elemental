package alexthw.ars_elemental.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

public class PosCarryMEI extends MobEffectInstance {

    public BlockPos getOrigin() {
        return origin;
    }

    BlockPos origin;

    public PosCarryMEI(Holder<MobEffect> pEffect, int duration, int amp, boolean ambient, boolean show, BlockPos origin) {
        super(pEffect, duration, amp, ambient, show);
        this.origin = origin;
    }

    //update the origin when updating the effect
    @Override
    public boolean update(@NotNull MobEffectInstance pOther) {

        if (pOther instanceof PosCarryMEI other) {
            this.origin = other.getOrigin();
        }

        return super.update(pOther);
    }
}
