package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class FlashLightning extends LightningBolt {
    public FlashLightning(EntityType<? extends LightningBolt> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public FlashLightning(Level pLevel) {
        super(ModEntities.FLASH_LIGHTNING.get(), pLevel);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.FLASH_LIGHTNING.get();
    }

    public void tick() {
        this.baseTick();
        if (this.life == 2) {
            if (this.level.isClientSide()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, .6F, 0.8F + this.random.nextFloat() * 0.2F, false);
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, .5F, 0.5F + this.random.nextFloat() * 0.2F, false);
            } else {
                this.powerLightningRod();
                clearCopperOnLightningStrike(this.level, this.getStrikePosition());
                this.gameEvent(GameEvent.LIGHTNING_STRIKE);
            }
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.discard();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
            }
        }

        if (this.life >= 0) {
            if (!(this.level instanceof ServerLevel)) {
                this.level.setSkyFlashTime(2);
            }
        }

    }

}
