package alexthw.ars_elemental.common.mob_effects;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.event.MaxManaCalcEvent;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;

public class OrderEffect extends MobEffect {

//    @Override
//    public List<ItemStack> getCurativeItems() {
//        return new ArrayList<>();
//    }

    public OrderEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::punish);
        NeoForge.EVENT_BUS.addListener(this::block);
    }

    public void block(SpellCastEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModPotions.HYMN_OF_ORDER))
            event.setCanceled(true);
    }

    public void punish(MaxManaCalcEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModPotions.HYMN_OF_ORDER))
            event.setReserve(1.0F);
    }

}
