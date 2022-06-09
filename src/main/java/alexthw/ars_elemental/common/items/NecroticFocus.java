package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.summon.*;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.event.SummonEvent;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import com.hollingsworth.arsnouveau.common.entity.SummonSkeleton;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectSummonUndead;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;

import static alexthw.ars_elemental.ArsNouveauRegistry.NECROMANCY;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, modid = ArsElemental.MODID)
public class NecroticFocus extends ElementalCurio implements ISchoolFocus {

    public NecroticFocus(Item.Properties properties) {
        super(properties);
    }

    @Override
    public SpellSchool getSchool() {
        return NECROMANCY;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void summonedEvent(SummonEvent event) {
        if (!event.world.isClientSide && hasFocus(event.world, event.shooter)) {
            if (event.summon.getLivingEntity() != null) {
                event.summon.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 500, 1));
                event.summon.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 1));
            }

            if (event.summon instanceof SummonHorse oldHorse && event.shooter instanceof Player summoner) {
                SummonSkeleHorse newHorse = new SummonSkeleHorse(oldHorse, summoner);
                if (newHorse.getOwnerID() != null) {
                    oldHorse.remove(Entity.RemovalReason.DISCARDED);
                    event.summon = newHorse;
                    event.world.addFreshEntity(newHorse);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void reRaiseSummon(SummonEvent.Death event) {
        if (!event.world.isClientSide) {
            ServerLevel world = (ServerLevel) event.world;
            if (event.summon.getOwner(world) instanceof Player player && !(event.summon instanceof IUndeadSummon)) {
                if (hasFocus(event.world, player)) {
                    LivingEntity toRaise = null;
                    if (event.summon instanceof SummonWolf wolf) {
                        toRaise = new SummonDirewolf(world, player, wolf);
                    } else if (event.summon instanceof EntityAllyVex vex) {
                        toRaise = new AllyVhexEntity(world, vex, player);
                    } else if (event.summon instanceof SummonSkeleton skel) {
                        toRaise = new SummonUndead(world, skel, player);
                    }
                    if (toRaise instanceof IUndeadSummon undead) {
                        undead.inherit(event.summon);
                        event.world.addFreshEntity(toRaise);
                        spawnDeathPoof(world, toRaise.blockPosition());
                    }
                }
            }
        }
    }

    public static void spawnDeathPoof(ServerLevel world, BlockPos pos){
        for(int i =0; i < 10; i++){
            double d0 = pos.getX() + 0.5;
            double d1 = pos.getY() + 1.2;
            double d2 = pos.getZ() + 0.5 ;
            world.sendParticles(ParticleTypes.ANGRY_VILLAGER, d0, d1, d2, 2,(world.random.nextFloat() - 0.5)/3, (world.random.nextFloat() - 0.5)/3, (world.random.nextFloat() - 0.5)/3, 0.1f);
        }
    }

    public static boolean hasFocus(Level level, Entity entity) {
        if (!level.isClientSide && entity instanceof Player) {
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems((LivingEntity)entity).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                return IntStream.range(0, items.getSlots()).anyMatch(i -> items.getStackInSlot(i).getItem() instanceof NecroticFocus);
            }
        }
        return false;
    }

    @Override
    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        builder.addDamageModifier(1.0f);
        if (NECROMANCY.isPartOfSchool(spellPart)) {
            builder.addDurationModifier(2.0f);
            if (spellPart == EffectHeal.INSTANCE || spellPart == EffectSummonUndead.INSTANCE) {
                builder.addAmplification(2.0f);
            }
        }
        return builder;
    }

    @SubscribeEvent
    public static void lifeSteal(LivingDeathEvent event){
        if (event.getSource().getEntity() instanceof IUndeadSummon risen && risen.getOwnerID() != null){
            Player player = event.getEntity().level.getPlayerByUUID(risen.getOwnerID());
            if (player != null) {
                player.heal(2.0F);
            }
        }
    }

    @SubscribeEvent
    public static void castSpell(SpellCastEvent event) {
        if (event.getWorld() instanceof ServerLevel world && event.getEntity() instanceof Player player && hasFocus(event.getWorld(), event.getEntityLiving()) && event.spell.getCastMethod() == MethodHomingProjectile.INSTANCE) {
            for (Mob i : world.getEntitiesOfClass(Mob.class, (new AABB(event.getEntityLiving().blockPosition())).inflate(30.0D), (l) -> l instanceof IUndeadSummon)) {
                Entity owner = ((ISummon) i).getOwner(world);
                if (player.equals(owner)) {
                    LivingEntity target = i.getTarget();
                    if (target == null) target = player.getLastHurtMob();
                    if (target != null && target.isAlive()) {
                        i.getLookControl().setLookAt(target);
                    } else {
                        i.getLookControl().setLookAt(player.getViewVector(1));
                    }
                    EntitySpellResolver spellResolver = new EntitySpellResolver((new SpellContext(event.spell, i)).withColors(event.context.colors));
                    spellResolver.onCast(ItemStack.EMPTY, i, i.level);
                }
            }
        }
    }

}
