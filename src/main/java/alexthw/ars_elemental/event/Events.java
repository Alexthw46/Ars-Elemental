package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class Events {

    @SubscribeEvent
    public static void keepOrder(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModRegistry.HYMN_OF_ORDER.get())) {
            MobEffectInstance effect = player.getEffect(ModRegistry.HYMN_OF_ORDER.get());
            if (effect == null) return;
            CompoundTag data = player.getPersistentData();
            if (!data.contains(Player.PERSISTED_NBT_TAG)) {
                data.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
            }
            CompoundTag persist = data.getCompound(Player.PERSISTED_NBT_TAG);

            persist.putBoolean("magic_locked", true);
            persist.putInt("magic_lock_duration", effect.getDuration());
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawnHW(PlayerEvent.PlayerRespawnEvent event) {
        CompoundTag data = event.getPlayer().getPersistentData();
        if (data.contains(Player.PERSISTED_NBT_TAG)) {
            CompoundTag persist = data.getCompound(Player.PERSISTED_NBT_TAG);
            if (persist.contains("magic_locked") && persist.contains("magic_lock_duration")) {
                event.getPlayer().addEffect(new MobEffectInstance(ModRegistry.HYMN_OF_ORDER.get(), persist.getInt("magic_lock_duration")));
                persist.remove("magic_locked");
                persist.remove("magic_lock_duration");
            }
        }
    }
}