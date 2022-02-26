package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.AllyVhexEntity;
import alexthw.ars_elemental.common.entity.IUndeadSummon;
import alexthw.ars_elemental.common.entity.SummonDirewolf;
import alexthw.ars_elemental.common.entity.SummonSkeleHorse;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.event.SummonEvent;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import com.hollingsworth.arsnouveau.common.entity.SummonHorse;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static alexthw.ars_elemental.ArsNouveauRegistry.NECROMANCY;


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
        if (!event.world.isClientSide && hasFocus(event.world, event.shooter)) {
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
    public static void summonDeathEvent(SummonEvent.Death event) {
        if (!event.world.isClientSide) {
            ServerLevel world = (ServerLevel) event.world;
            if (event.summon.getOwner(world) instanceof Player player && !(event.summon instanceof IUndeadSummon)) {
                if (hasFocus(event.world, player)) {
                    LivingEntity toRaise = null;
                    if (event.summon instanceof SummonWolf wolf) {
                        toRaise = new SummonDirewolf(world, player, wolf);
                    }else if (event.summon instanceof EntityAllyVex vex){
                        toRaise = new AllyVhexEntity(world, vex, player);
                    }
                    if (toRaise instanceof IUndeadSummon undead){
                        if (!event.wasExpiration) event.summon.setTicksLeft(0);
                        undead.setOwnerID(event.summon.getOwnerID());
                        undead.setTicksLeft(400);
                        event.world.addFreshEntity(toRaise);
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
                return IntStream.range(0, items.getSlots()).anyMatch(i -> items.getStackInSlot(i).getItem() instanceof NecroticFocus);
            }
        }
        return false;
    }

    @Override
    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        builder.addDamageModifier(1.0f);
        if (NECROMANCY.isPartOfSchool(spellPart)) builder.addAmplification(2.0f);
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

    //TODO change this
    @SubscribeEvent
    public static void castSpell(SpellCastEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof Player player && hasFocus(event.getWorld(), event.getEntityLiving()) && event.spell.getCastMethod() != null && sympatheticMethods.contains(event.spell.getCastMethod())) {

            for (LivingEntity i : event.getWorld().getEntitiesOfClass(LivingEntity.class, (new AABB(event.getEntityLiving().blockPosition())).inflate(30.0D), (l) -> l instanceof IUndeadSummon)) {
                ISummon summon = (ISummon) i;
                Entity owner = summon.getOwner((ServerLevel) event.getWorld());
                if (player.equals(owner)) {
                    EntitySpellResolver spellResolver = new EntitySpellResolver((new SpellContext(event.spell, i)).withColors(event.context.colors));
                    spellResolver.onCast(ItemStack.EMPTY, i, i.level);
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
