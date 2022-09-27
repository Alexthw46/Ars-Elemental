package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ArsNouveauRegistry;
import alexthw.ars_elemental.api.IUndeadSummon;
import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.common.entity.summon.*;
import alexthw.ars_elemental.common.items.foci.NecroticFocus;
import com.hollingsworth.arsnouveau.api.event.SummonEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ArsElemental.MODID)
public class SummonEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void summonedEvent(SummonEvent event) {

        SpellSchool focus = ISchoolFocus.hasFocus(event.world, event.shooter);

        if (!event.world.isClientSide && focus != null) {

            if (focus == ArsNouveauRegistry.NECROMANCY) {
                if (event.summon.getLivingEntity() != null) {
                    event.summon.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 1));
                    event.summon.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 1));
                }
            }

            if (event.summon instanceof SummonHorse oldHorse && event.shooter instanceof ServerPlayer summoner) {
                switch (focus.getId()) {
                    case "water" -> {
                        SummonDolphin newHorse = new SummonDolphin(oldHorse, summoner);
                        if (newHorse.getOwnerID() != null) {
                            oldHorse.remove(Entity.RemovalReason.DISCARDED);
                            event.summon = newHorse;
                            event.world.addFreshEntity(newHorse);
                            CriteriaTriggers.SUMMONED_ENTITY.trigger(summoner, newHorse);
                        }
                    }
                    case "fire" -> {
                        SummonStrider newHorse = new SummonStrider(oldHorse, summoner);
                        if (newHorse.getOwnerID() != null) {
                            oldHorse.remove(Entity.RemovalReason.DISCARDED);
                            event.summon = newHorse;
                            event.world.addFreshEntity(newHorse);
                            CriteriaTriggers.SUMMONED_ENTITY.trigger(summoner, newHorse);
                        }
                    }
                    case "necromancy" -> {
                        SummonSkeleHorse newHorse = new SummonSkeleHorse(oldHorse, summoner);
                        if (newHorse.getOwnerID() != null) {
                            oldHorse.remove(Entity.RemovalReason.DISCARDED);
                            event.summon = newHorse;
                            event.world.addFreshEntity(newHorse);
                            CriteriaTriggers.SUMMONED_ENTITY.trigger(summoner, newHorse);
                        }
                    }
                    default -> {
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void reRaiseSummon(SummonEvent.Death event) {
        if (!event.world.isClientSide) {
            ServerLevel world = (ServerLevel) event.world;
            if (event.summon.getOwner(world) instanceof Player player && !(event.summon instanceof IUndeadSummon)) {
                if (NecroticFocus.hasFocus(event.world, player)) {
                    LivingEntity toRaise = null;
                    if (event.summon instanceof SummonWolf wolf) {
                        toRaise = new SummonDirewolf(world, player, wolf);
                    } else if (event.summon instanceof EntityAllyVex vex) {
                        toRaise = new AllyVhexEntity(world, vex, player);
                    } else if (event.summon instanceof SummonSkeleton skelly) {
                        toRaise = new SummonUndead(world, skelly, player);
                    }
                    if (toRaise instanceof IUndeadSummon undead) {
                        undead.inherit(event.summon);
                        event.world.addFreshEntity(toRaise);
                        NecroticFocus.spawnDeathPoof(world, toRaise.blockPosition());
                    }
                }
            }
        }
    }
}
