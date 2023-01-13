package alexthw.ars_elemental.common.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.List;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class AttractionRitual extends AbstractRitual {

    @Override
    public String getLangName() {
        return "Attraction";
    }

    @Override
    protected void tick() {
        if (getWorld() instanceof ServerLevel level && level.getGameTime() % 10 == 0 && this.tile != null) {
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(tile.getBlockPos()).inflate(8), entity -> !(entity instanceof Player || entity.getType().is(Tags.EntityTypes.BOSSES)));
            for (Entity entity : entities) {
                if (entity != null && getPos() != null) {
                    Vec3 vec3d = new Vec3(this.getPos().getX() - entity.getX(), this.getPos().getY() - entity.getY(), this.getPos().getZ() - entity.getZ());
                    if (vec3d.length() < 1) continue;
                    entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.normalize()).scale(0.3F));
                    entity.hurtMarked = true;
                }
                setNeedsSource(true);
            }
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return prefix(ID);
    }

    @Override
    public int getSourceCost() {
        return 10;
    }

    public static String ID = "ritual_attraction";
}
