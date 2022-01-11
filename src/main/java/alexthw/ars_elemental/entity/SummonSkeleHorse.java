package alexthw.ars_elemental.entity;

import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SummonSkeleHorse extends SummonHorse {
    public SummonSkeleHorse(EntityType<? extends HorseEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }


    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return this.isEyeInFluid(FluidTags.WATER) ? SoundEvents.SKELETON_HORSE_AMBIENT_WATER : SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        super.getHurtSound(pDamageSource);
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (this.onGround) {
            if (!this.isVehicle()) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }

            ++this.gallopSoundCounter;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
            }

            if (this.gallopSoundCounter <= 5) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
        }

        return SoundEvents.SKELETON_HORSE_SWIM;
    }

    @Override
    protected void playSwimSound(float pVolume) {
        if (this.onGround) {
            super.playSwimSound(0.3F);
        } else {
            super.playSwimSound(Math.min(0.1F, pVolume * 25.0F));
        }

    }

    @Override
    protected void playJumpSound() {
        if (this.isInWater()) {
            this.playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
        } else {
            super.playJumpSound();
        }

    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

}
