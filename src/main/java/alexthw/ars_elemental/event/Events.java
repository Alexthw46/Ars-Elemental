package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.ConfigHandler.Common;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModRegistry;
import alexthw.ars_elemental.world.WorldEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class Events {

    @SubscribeEvent
    public static void addMobSpawns(BiomeLoadingEvent e) {
        if (e.getCategory() == Biome.BiomeCategory.NETHER || e.getCategory() == Biome.BiomeCategory.THEEND || e.getCategory() == Biome.BiomeCategory.NONE)
            return;
        if (Common.SIREN_WEIGHT.get() > 0) {
            if (e.getCategory() == Biome.BiomeCategory.OCEAN || e.getCategory() == Biome.BiomeCategory.BEACH) {
                if (e.getClimate().temperature >= 0.5)
                    e.getSpawns().addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.SIREN_ENTITY.get(), Common.SIREN_WEIGHT.get(), 1, 2));
            }
        }

        if (Common.MAGES_WEIGHT.get() > 0) {
            e.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.FIRE_MAGE.get(), ConfigHandler.Common.MAGES_WEIGHT.get(), 1, 1));
            e.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.WATER_MAGE.get(), ConfigHandler.Common.MAGES_WEIGHT.get(), 1, 1));
            e.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.AIR_MAGE.get(), ConfigHandler.Common.MAGES_WEIGHT.get(), 1, 1));
            e.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.EARTH_MAGE.get(), ConfigHandler.Common.MAGES_WEIGHT.get(), 1, 1));
        }

        if (Common.TREE_SPAWN_RATE.get() > 0)
            e.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WorldEvents.PLACED_FLASHING_CONFIGURED);

    }

    public static void keepOrder(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModRegistry.HYMN_OF_ORDER.get())) {
            CompoundTag data = player.getPersistentData();
            data.putBoolean("magic_locked", true);
            data.putInt("magic_lock_duration", player.getEffect(ModRegistry.HYMN_OF_ORDER.get()).getDuration());
        }
    }

    public static void onPlayerRespawnHW(PlayerEvent.PlayerRespawnEvent event) {
        CompoundTag data = event.getPlayer().getPersistentData();
        if (data.contains("magic_locked") && data.contains("magic_lock_duration") && data.getBoolean("magic_locked")) {
            event.getPlayer().addEffect(new MobEffectInstance(ModRegistry.HYMN_OF_ORDER.get(), data.getInt("magic_lock_duration")));
            data.remove("magic_locked");
            data.remove("magic_lock_duration");
        }
    }
}