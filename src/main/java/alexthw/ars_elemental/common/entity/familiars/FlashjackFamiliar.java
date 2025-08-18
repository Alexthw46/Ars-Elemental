package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.familiar.FlyingFamiliarEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FlashjackFamiliar extends FlyingFamiliarEntity {
    public FlashjackFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public FlashjackFamiliar(Level world) {
        super(ModEntities.FLASHJACK_FAMILIAR.get(), world);
    }

    @Override
    public @Nullable ResourceLocation getTexture() {
        return prefix("textures/entity/flashjack.png");
    }
}
