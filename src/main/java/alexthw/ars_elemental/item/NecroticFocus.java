package alexthw.ars_elemental.item;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.entity.AllyVhexEntity;
import alexthw.ars_elemental.entity.SummonDirewolf;
import alexthw.ars_elemental.entity.SummonSkeleHorse;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.event.SummonEvent;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import com.hollingsworth.arsnouveau.common.entity.EntityDummy;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.FORGE, modid = ArsElemental.MODID)
public class NecroticFocus extends Item implements ISpellModifierItem {
    public NecroticFocus(Properties properties) {
        super(properties);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void summonedEvent(SummonEvent event) {
        if (!event.world.isClientSide && containsThis(event.world, event.summon.getOwner((ServerWorld) event.world))) {
            if (event.summon instanceof SummonHorse) {
                SummonHorse oldHorse = (SummonHorse) event.summon;
                SummonSkeleHorse newHorse = replaceHorse(event.world, oldHorse);
                if (newHorse != null) {
                    oldHorse.getEntity().remove();
                    event.summon = newHorse;
                    event.world.addFreshEntity(newHorse);
                }
            } else if (event.summon instanceof SummonWolf) {
                SummonWolf oldWolf = (SummonWolf) event.summon;
                SummonDirewolf newWolf = replaceWolf(event.world, oldWolf);

                if (newWolf != null) {
                    oldWolf.getEntity().remove();
                    event.summon = newWolf;
                    event.world.addFreshEntity(newWolf);
                }
            } else if (event.summon instanceof EntityAllyVex) {
                EntityAllyVex oldVex = (EntityAllyVex) event.summon;
                AllyVhexEntity newVex = replaceVex(event.world, oldVex);

                if (newVex != null) {
                    oldVex.getEntity().remove();
                    event.summon = newVex;
                    event.world.addFreshEntity(newVex);
                }
            }
        }
    }

    private static AllyVhexEntity replaceVex(World world, EntityAllyVex oldVex) {
        PlayerEntity owner = (PlayerEntity) oldVex.getOwner((ServerWorld) world);
        if (owner == null) return null;
        AllyVhexEntity vexEntity = new AllyVhexEntity(world, owner);
        vexEntity.moveTo(oldVex.blockPosition(), 0.0F, 0.0F);
        vexEntity.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(oldVex.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
        vexEntity.setOwner(owner);
        vexEntity.setOwnerID(owner.getUUID());
        vexEntity.setBoundOrigin(oldVex.getBoundOrigin());
        vexEntity.setLimitedLife(oldVex.serializeNBT().getInt("LifeTicks"));
        return vexEntity;
    }

    private static SummonDirewolf replaceWolf(World world, SummonWolf oldWolf) {
        SummonDirewolf direwolf = new SummonDirewolf(world);
        direwolf.setTicksLeft(oldWolf.getTicksLeft());
        Vector3d hit = oldWolf.position();
        direwolf.setPos(hit.x(), hit.y(), hit.z());
        if (oldWolf.getOwnerID() == null) return null;
        PlayerEntity shooter = world.getPlayerByUUID(oldWolf.getOwnerID());
        if (shooter == null) return null;
        direwolf.setTarget(shooter.getLastHurtMob());
        direwolf.setAggressive(true);
        direwolf.setTame(true);
        direwolf.tame(shooter);
        direwolf.setOwnerID(shooter.getUUID());
        return direwolf;
    }

    public static SummonSkeleHorse replaceHorse(World world, SummonHorse oldHorse) {
        SummonSkeleHorse newHorse = new SummonSkeleHorse(ModRegistry.SKELEHORSE_SUMMON.get(), world);
        if (oldHorse.getOwnerID() == null) return null;
        PlayerEntity player = world.getPlayerByUUID(oldHorse.getOwnerID());
        if (player == null) return null;
        BlockPos position = oldHorse.blockPosition();
        newHorse.setPos(position.getX(), position.getY(), position.getZ());
        newHorse.ticksLeft = oldHorse.getTicksLeft();
        newHorse.tameWithName(player);
        newHorse.getHorseInventory().setItem(0, new ItemStack(Items.SADDLE));
        newHorse.setOwnerID(player.getUUID());
        newHorse.setDropChance(EquipmentSlotType.CHEST, 0.0F);

        return newHorse;
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void summonDeathEvent(SummonEvent.Death event) {
        if (!event.world.isClientSide) {
            if (event.summon.getOwner((ServerWorld) event.world) instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.summon.getOwner((ServerWorld) event.world);
                if (player != null && containsThis(event.world, player)) {
                    DamageSource source = event.source;
                    if (source != null && source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
                        LivingEntity killer = (LivingEntity) source.getEntity();
                        killer.addEffect(new EffectInstance(Effects.WITHER, 100));
                    }
                }
            }
        }
    }

    public static boolean containsThis(World world, Entity entity) {
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            IItemHandlerModifiable items = CuriosUtil.getAllWornItems((LivingEntity) entity).orElse(null);
            if (items != null) {
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
        if (!event.getWorld().isClientSide && event.getEntity() instanceof PlayerEntity && containsThis(event.getWorld(), event.getEntityLiving()) && event.spell.getCastMethod() != null && sympatheticMethods.contains(event.spell.getCastMethod())) {

            PlayerEntity player = (PlayerEntity) event.getEntity();
            for (LivingEntity i : event.getWorld().getLoadedEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(event.getEntityLiving().blockPosition())).inflate(30.0D), (l) -> l instanceof ISummon && !(l instanceof EntityDummy))) {
                ISummon summon = (ISummon) i;
                Entity owner = summon.getOwner((ServerWorld) event.getWorld());
                if (player.equals(owner)) {
                    EntitySpellResolver spellResolver = new EntitySpellResolver((new SpellContext(event.spell, i)).withColors(event.context.colors));
                    spellResolver.onCast(ItemStack.EMPTY, i, i.level);
                    summon.setTicksLeft(summon.getTicksLeft()/2);
                }
            }
        }

    }

    public static List<AbstractCastMethod> sympatheticMethods = new ArrayList<>();

    static {
        sympatheticMethods.add(MethodTouch.INSTANCE);
        sympatheticMethods.add(MethodProjectile.INSTANCE);
    }
}
