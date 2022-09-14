package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.api.IUndeadSummon;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonUndead extends SummonSkeleton implements IUndeadSummon {

    public SummonUndead(EntityType<? extends Skeleton> type, Level worldIn) {
        super(type, worldIn);
    }

    public SummonUndead(Level level) {
        this(ModEntities.WSKELETON_SUMMON.get(), level);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.WSKELETON_SUMMON.get();
    }

    public SummonUndead(Level level, SummonSkeleton oldSkeleton, Player summoner) {
        this(level);
        this.setWeapon(oldSkeleton.getMainHandItem());
        setOwner(summoner);
        setLimitedLife(oldSkeleton.getTicksLeft());
        Vec3 hit = oldSkeleton.position();
        setPos(hit.x(), hit.y(), hit.z());
        setTarget(summoner.getLastHurtMob());
        oldSkeleton.getActiveEffects().stream().filter(e -> e.getEffect().isBeneficial()).forEach(this::addEffect);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LifeTicks", getTicksLeft());
    }

}
