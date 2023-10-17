package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SummonCamel extends Camel implements ISummon {
    public int ticksLeft;

    public SummonCamel(SummonHorse oldHorse, Player summoner) {
        this(summoner.level);
        BlockPos position = oldHorse.blockPosition();
        setPos(position.getX(), position.getY(), position.getZ());
        ticksLeft = oldHorse.getTicksLeft();
        tameWithName(summoner);
        getHorseInventory().setItem(0, new ItemStack(Items.SADDLE));
        setOwnerID(summoner.getUUID());
        setDropChance(EquipmentSlot.CHEST, 0.0F);
        oldHorse.getActiveEffects().stream().filter(e -> e.getEffect().isBeneficial()).forEach(this::addEffect);

    }

    public SummonCamel(Level level) {
        super(ModEntities.CAMEL_SUMMON.get(), level);
    }


    public SummonCamel(EntityType<? extends Camel> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.CAMEL_SUMMON.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            ticksLeft--;
            if (ticksLeft <= 0) {
                ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
                this.remove(RemovalReason.DISCARDED);
                onSummonDeath(level, null, true);
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        onSummonDeath(level, cause, false);
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack itemstackIn) {
        return false;
    }

    @Override
    protected void dropEquipment() {
    }

    @Override
    public int getExperienceReward() {
        return 0;
    }

    public SimpleContainer getHorseInventory() {
        return this.inventory;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player playerEntity) {
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return false;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.ticksLeft = compound.getInt("left");

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("left", ticksLeft);
        writeOwner(compound);
    }

    @Override
    public int getTicksLeft() {
        return ticksLeft;
    }

    @Override
    public void setTicksLeft(int ticks) {
        this.ticksLeft = ticks;
    }

    public void setOwnerID(UUID uuid) {
        setOwnerUUID(uuid);
    }


}
