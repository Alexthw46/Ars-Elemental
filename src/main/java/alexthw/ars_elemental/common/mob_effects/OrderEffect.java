package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.event.MaxManaCalcEvent;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.ArrayList;
import java.util.List;

public class OrderEffect extends MobEffect {

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    public OrderEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::punish);
        MinecraftForge.EVENT_BUS.addListener(this::block);

    }

    public void block(SpellCastEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModPotions.HYMN_OF_ORDER.get())) event.setCanceled(true);
        }
    }

    public void punish(MaxManaCalcEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasEffect(ModPotions.HYMN_OF_ORDER.get())) event.setMax(1);
        }
    }

}
