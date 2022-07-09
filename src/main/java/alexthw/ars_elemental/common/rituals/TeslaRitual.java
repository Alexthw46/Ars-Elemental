package alexthw.ars_elemental.common.rituals;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.LightningEntity;
import com.hollingsworth.arsnouveau.common.entity.ModEntities;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class TeslaRitual extends AbstractRitual {
    @Override
    protected void tick() {
        if (getWorld() instanceof ServerLevel level && level.getGameTime() % 100 == 0 && this.tile != null) {
            Predicate<? super LivingEntity> test = this.didConsumeItem(ItemsRegistry.AIR_ESSENCE) ? (living) -> true : (living) -> !(living instanceof Player);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(tile.getBlockPos()).inflate(5, 3, 5), test);
            for (LivingEntity entity : entities) {
                Vec3 pos = entity.position();
                LightningEntity lightningBoltEntity = new LightningEntity(ModEntities.LIGHTNING_ENTITY.get(), level);
                lightningBoltEntity.setPos(pos.x(), pos.y(), pos.z());
                lightningBoltEntity.setCause(null);
                level.addFreshEntity(lightningBoltEntity);
                setNeedsSource(true);
            }
        }
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        return stack.getItem() == ItemsRegistry.AIR_ESSENCE.get();
    }

    @Override
    public ParticleColor getCenterColor() {
        return new ParticleColor(
                100 + rand.nextInt(155),
                50 + rand.nextInt(200),
                rand.nextInt(25));
    }

    @Override
    public int getSourceCost() {
        return 1000;
    }

    @Override
    public String getLangName() {
        return "Tesla";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return prefix(ID);
    }

    public static String ID = "ritual_tesla_coil";

}
