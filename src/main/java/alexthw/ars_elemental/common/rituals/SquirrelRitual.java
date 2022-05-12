package alexthw.ars_elemental.common.rituals;

import alexthw.ars_elemental.ConfigHandler.Common;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.entity.Starbuncle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SquirrelRitual extends AbstractRitual {

    @Override
    protected void tick() {
        int modifier = didConsumeItem(Items.GOLD_BLOCK) ? 2 : 1;
        if (getWorld() instanceof ServerLevel level && level.getGameTime() % Common.SQUIRREL_REFRESH_RATE.get() == 0 && this.tile != null) {
            List<Starbuncle> entities = level.getEntitiesOfClass(Starbuncle.class, new AABB(tile.getBlockPos()).inflate(10 * modifier));
            for (Starbuncle entity : entities) {
                if (entity != null) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1, false, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 2400, 1, false, false));
                }
                setNeedsMana(true);
            }
        }
    }

    @Override
    public boolean canConsumeItem(ItemStack stack) {
        return getConsumedItems().isEmpty() && stack.getItem() == Items.GOLD_BLOCK;
    }

    @Override
    public int getManaCost() {
        return 150;
    }

    @Override
    public ParticleColor getCenterColor() {
        return new ParticleColor(
                150 + rand.nextInt(105),
                50 + rand.nextInt(100),
                rand.nextInt(25));
    }

    @Override
    public String getID() {
        return ID;
    }

    public static String ID = "ae_squirrels";

}
