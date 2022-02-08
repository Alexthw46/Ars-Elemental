package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.entity.AllyVhexEntity;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.event.SummonEvent;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import com.hollingsworth.arsnouveau.common.entity.EntityDummy;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import alexthw.ars_elemental.common.entity.SummonDirewolf;
import alexthw.ars_elemental.common.entity.SummonSkeleHorse;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, modid = ArsElemental.MODID)
public class NecroticFocus extends Item implements ISpellModifierItem, ICurioItem {
    public NecroticFocus(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void summonedEvent(SummonEvent event) {
        if (!event.world.isClientSide && hasFocus(event.world, event.summon.getOwner((ServerLevel) event.world))) {
            if (event.summon instanceof SummonHorse oldHorse) {
                SummonSkeleHorse newHorse = replaceHorse(event.world, oldHorse);
                if (newHorse != null) {
                    oldHorse.remove(Entity.RemovalReason.DISCARDED);
                    event.summon = newHorse;
                    event.world.addFreshEntity(newHorse);
                }
            } else if (event.summon instanceof SummonWolf oldWolf) {
                SummonDirewolf newWolf = replaceWolf(event.world, oldWolf);

                if (newWolf != null) {
                    oldWolf.remove(Entity.RemovalReason.DISCARDED);
                    event.summon = newWolf;
                    event.world.addFreshEntity(newWolf);
                }
            } else if (event.summon instanceof EntityAllyVex oldVex) {
                AllyVhexEntity newVex = replaceVex(event.world, oldVex);

                if (newVex != null) {
                    oldVex.getSelfEntity().remove(Entity.RemovalReason.DISCARDED);
                    event.summon = newVex;
                    event.world.addFreshEntity(newVex);
                }
            }
        }
    }

    private static AllyVhexEntity replaceVex(Level level, EntityAllyVex oldVex) {
        Player owner = (Player) oldVex.getOwner((ServerLevel) level);
        if (owner == null) return null;
        AllyVhexEntity vexEntity = new AllyVhexEntity(level, owner);
        vexEntity.moveTo(oldVex.blockPosition(), 0.0F, 0.0F);
        vexEntity.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(oldVex.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        vexEntity.setOwner(owner);
        vexEntity.setOwnerID(owner.getUUID());
        vexEntity.setBoundOrigin(oldVex.getBoundOrigin());
        vexEntity.setLimitedLife(oldVex.serializeNBT().getInt("LifeTicks"));
        return vexEntity;
    }

    private static SummonDirewolf replaceWolf(Level level, SummonWolf oldWolf) {
        SummonDirewolf direwolf = new SummonDirewolf(level);
        direwolf.setTicksLeft(oldWolf.getTicksLeft());
        Vec3 hit = oldWolf.position();
        direwolf.setPos(hit.x(), hit.y(), hit.z());
        if (oldWolf.getOwnerID() == null) return null;
        Player shooter = level.getPlayerByUUID(oldWolf.getOwnerID());
        if (shooter == null) return null;
        direwolf.setTarget(shooter.getLastHurtMob());
        direwolf.setAggressive(true);
        direwolf.setTame(true);
        direwolf.tame(shooter);
        direwolf.setOwnerID(shooter.getUUID());
        return direwolf;
    }

    public static SummonSkeleHorse replaceHorse(Level level, SummonHorse oldHorse) {
        SummonSkeleHorse newHorse = new SummonSkeleHorse(ModRegistry.SKELEHORSE_SUMMON.get(), level);
        if (oldHorse.getOwnerID() == null) return null;
        Player player = level.getPlayerByUUID(oldHorse.getOwnerID());
        if (player == null) return null;
        BlockPos position = oldHorse.blockPosition();
        newHorse.setPos(position.getX(), position.getY(), position.getZ());
        newHorse.ticksLeft = oldHorse.getTicksLeft();
        newHorse.tameWithName(player);
        newHorse.getHorseInventory().setItem(0, new ItemStack(Items.SADDLE));
        newHorse.setOwnerID(player.getUUID());
        newHorse.setDropChance(EquipmentSlot.CHEST, 0.0F);

        return newHorse;
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void summonDeathEvent(SummonEvent.Death event) {
        if (!event.world.isClientSide) {
            if (event.summon.getOwner((ServerLevel) event.world) instanceof Player player) {
                if (hasFocus(event.world, player)) {
                    DamageSource source = event.source;
                    if (source != null && source.getEntity() != null && source.getEntity() instanceof LivingEntity killer) {
                        killer.addEffect(new MobEffectInstance(MobEffects.WITHER, 100));
                    }
                }
            }
        }
    }

    public static boolean hasFocus(Level level, Entity entity) {
        if (!level.isClientSide && entity instanceof Player) {
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems((LivingEntity)entity).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                for (int i = 0; i < items.getSlots(); ++i) {
                    Item item = items.getStackInSlot(i).getItem();
                    if (item instanceof NecroticFocus) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void castSpell(SpellCastEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof Player player && hasFocus(event.getWorld(), event.getEntityLiving()) && event.spell.getCastMethod() != null && sympatheticMethods.contains(event.spell.getCastMethod())) {

            for (LivingEntity i : event.getWorld().getEntitiesOfClass(LivingEntity.class, (new AABB(event.getEntityLiving().blockPosition())).inflate(30.0D), (l) -> l instanceof ISummon && !(l instanceof EntityDummy))) {
                ISummon summon = (ISummon) i;
                Entity owner = summon.getOwner((ServerLevel) event.getWorld());
                if (player.equals(owner)) {
                    EntitySpellResolver spellResolver = new EntitySpellResolver((new SpellContext(event.spell, i)).withColors(event.context.colors));
                    spellResolver.onCast(ItemStack.EMPTY, i, i.level);
                    summon.setTicksLeft(summon.getTicksLeft()/2);
                }
            }
        }

    }

    @Override
    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        builder.addDamageModifier(1.0f);
        return builder;
    }

    public static List<AbstractCastMethod> sympatheticMethods = new ArrayList<>();

    static {
        sympatheticMethods.add(MethodTouch.INSTANCE);
        sympatheticMethods.add(MethodProjectile.INSTANCE);
    }
}
