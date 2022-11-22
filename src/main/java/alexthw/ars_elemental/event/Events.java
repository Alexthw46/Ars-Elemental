package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.item.IElementalArmor;
import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.registry.ModPotions;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static alexthw.ars_elemental.ConfigHandler.COMMON;
import static alexthw.ars_elemental.common.enchantments.SoulboundEnchantment.*;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class Events {

    @SubscribeEvent
    public static void focusDiscount(SpellCastEvent event) {
        if (!event.getWorld().isClientSide) {
            double finalDiscount = 0;
            SpellSchool focus = ISchoolFocus.hasFocus(event.getWorld(), event.getEntity());
            if (focus != null) {
                if (event.spell.recipe.stream().anyMatch(focus::isPartOfSchool))
                    finalDiscount += COMMON.FocusDiscount.get();
            }
            for (ItemStack stack : event.getEntity().getArmorSlots()) {
                if (stack.getItem() instanceof IElementalArmor armor)
                    finalDiscount += armor.getDiscount(event.spell.recipe);
            }
            event.spell.addDiscount((int) (event.spell.getDiscountedCost() * finalDiscount));

        }
    }

    @SubscribeEvent
    public static void DeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && player.hasEffect(ModPotions.HYMN_OF_ORDER.get())) {
            MobEffectInstance effect = player.getEffect(ModPotions.HYMN_OF_ORDER.get());
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

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player) {
            if ((player instanceof FakePlayer) || player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            Collection<ItemEntity> drops = event.getDrops();
            if (drops == null) return;
            List<ItemEntity> keeps = new ArrayList<>();
            for (ItemEntity item : drops) {
                ItemStack stack = item.getItem();
                if (!stack.isEmpty() && stack.getEnchantmentLevel(ModRegistry.SOULBOUND.get()) > 0) {
                    keeps.add(item);
                }
            }

            if (keeps.size() > 0) {
                drops.removeAll(keeps);

                CompoundTag cmp = new CompoundTag();
                cmp.putInt(TAG_SOULBOUND_DROP_COUNT, keeps.size());

                int i = 0;
                for (ItemEntity keep : keeps) {
                    ItemStack stack = keep.getItem();
                    CompoundTag cmp1 = stack.save(new CompoundTag());
                    cmp.put(TAG_SOULBOUND_PREFIX + i, cmp1);
                    i++;
                }

                CompoundTag data = player.getPersistentData();
                if (!data.contains(Player.PERSISTED_NBT_TAG)) {
                    data.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
                }

                CompoundTag persist = data.getCompound(Player.PERSISTED_NBT_TAG);
                persist.put(TAG_SOULBOUND, cmp);
            }
        }

    }


    @SubscribeEvent
    public static void onPlayerRespawnHW(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        CompoundTag data = event.getEntity().getPersistentData();
        if (data.contains(Player.PERSISTED_NBT_TAG)) {
            CompoundTag persist = data.getCompound(Player.PERSISTED_NBT_TAG);
            if (persist.contains("magic_locked") && persist.contains("magic_lock_duration")) {
                event.getEntity().addEffect(new MobEffectInstance(ModPotions.HYMN_OF_ORDER.get(), persist.getInt("magic_lock_duration")));
                persist.remove("magic_locked");
                persist.remove("magic_lock_duration");
            }

            CompoundTag soulTag = persist.getCompound(TAG_SOULBOUND);

            int count = soulTag.getInt(TAG_SOULBOUND_DROP_COUNT);
            List<ItemStack> recovered = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                CompoundTag toRecover = soulTag.getCompound(TAG_SOULBOUND_PREFIX + i);
                ItemStack stack = ItemStack.of(toRecover);
                if (!stack.isEmpty()) {
                    recovered.add(stack.copy());
                }
            }

            for (ItemStack stack : recovered) {
                if (stack.getItem() instanceof ArmorItem armor && event.getEntity().getItemBySlot(armor.getSlot()).isEmpty()) {
                    event.getEntity().setItemSlot(armor.getSlot(), stack);
                } else if (stack.getItem() instanceof ShieldItem && event.getEntity().getOffhandItem().isEmpty()) {
                    event.getEntity().setItemInHand(InteractionHand.OFF_HAND, stack);
                } else {
                    ItemHandlerHelper.giveItemToPlayer(event.getEntity(), stack);
                }
            }

            persist.remove(TAG_SOULBOUND);

        }
    }

    @SubscribeEvent
    public static void soulboundCurio(DropRulesEvent event) {
        event.addOverride((i) -> (i.getEnchantmentLevel(ModRegistry.SOULBOUND.get()) > 0), ICurio.DropRule.ALWAYS_KEEP);
    }

}