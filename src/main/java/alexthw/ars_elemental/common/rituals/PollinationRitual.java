package alexthw.ars_elemental.common.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class PollinationRitual extends AbstractRitual {


    public static String ID = "ritual_pollination";
    int backoff = 0;

    @Override
    public String getLangName() {
        return "Pollination";
    }

    @Override
    public String getLangDescription() {
        return "Speeds up the gathering of nectar of the nearby roaming bees, doesn't speed up the honey production inside the beehives.";
    }

    @Override
    protected void tick() {
        if (getWorld() instanceof ServerLevel level && level.getGameTime() % getBackoff() == 0 && this.tile != null) {
            // Get all entities in a 15 block radius, excluding players and bosses.
            List<? extends Bee> entities = level.getEntitiesOfClass(Bee.class, new AABB(tile.getBlockPos()).inflate(15));
            boolean flag = false;
            for (Bee bee : entities) {
                // Apply the repulsion effect to the bee.
                if (bee != null && bee.beePollinateGoal.isPollinating()) {
                    flag = flag || !bee.hasNectar();
                    bee.beePollinateGoal.successfulPollinatingTicks += 100;
                    if (flag) setNeedsSource(true);
                }
            }
            // If no entities were found, increase the backoff timer.
            if (entities.isEmpty() && !flag) {
                setBackoff(100);
            } else {
                // Otherwise, reset the backoff timer.
                setBackoff(0);
            }
        }
    }

    private int getBackoff() {
        return 60 + backoff;
    }

    private void setBackoff(int i) {
        this.backoff = i;
    }

    @Override
    public void write(HolderLookup.Provider provider, CompoundTag tag) {
        super.write(provider, tag);
        tag.putInt("backoff", this.backoff);
    }

    @Override
    public void read(HolderLookup.Provider provider, CompoundTag tag) {
        super.read(provider, tag);
        backoff = tag.getInt("backoff");
    }

    @Override
    public ResourceLocation getRegistryName() {
        return prefix(ID);
    }

    @Override
    public int getSourceCost() {
        return 10;
    }

}
