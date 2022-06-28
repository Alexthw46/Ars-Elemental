package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.ISchoolBangle;
import alexthw.ars_elemental.api.ISchoolFocus;
import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.items.armor.IElementalArmor;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

import static alexthw.ars_elemental.ConfigHandler.COMMON;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_AIR;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class DamageEvents {

    @SubscribeEvent
    public static void bypassRes(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity living) {
            SpellSchool focus = ISchoolFocus.hasFocus(event.getEntity().level, player);
            if (focus != null) {
                switch (focus.getId()) {
                    case "fire" -> {
                        if (event.getSource().isFire() && living.fireImmune()) {
                            event.setCanceled(true);
                            DamageSource newDamage = new EntityDamageSource("hellflare", player).setMagic();
                            if (event.getSource().isBypassArmor()) newDamage.bypassArmor();
                            if (event.getSource().isBypassMagic()) newDamage.bypassMagic();
                            living.hurt(newDamage, event.getAmount());
                        }
                    }
                    case "water" -> {
                        if (event.getSource().getMsgId().equals("drown") && living.getMobType() == MobType.WATER) {
                            event.setCanceled(true);
                            DamageSource newDamage = DamageSource.playerAttack(player).setMagic();
                            if (event.getSource().isBypassArmor()) newDamage.bypassArmor();
                            if (event.getSource().isBypassMagic()) newDamage.bypassMagic();
                            living.hurt(newDamage, event.getAmount());
                        }
                    }
                    case "air" -> {
                        if (living.invulnerableTime > 0 && event.getSource().isFall()) {
                            living.invulnerableTime = 0;
                        }
                    }
                }
            }
        } else if (event.getSource().getEntity() instanceof FirenandoEntity FE) {
            if (event.getEntity().fireImmune() && event.getSource().isFire() && event.getEntity() instanceof Monster) {
                event.setCanceled(true);
                event.getEntityLiving().hurt(DamageSource.mobAttack(FE).setMagic().bypassArmor(), event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public static void banglesSpecials(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity living) {
            SpellSchool bangle = ISchoolBangle.hasBangle(event.getEntity().level, player);
            if (bangle != null) {
                switch (bangle.getId()) {
                    case "fire" -> living.setSecondsOnFire(5);
                    case "water" -> living.setTicksFrozen(living.getTicksFrozen() + 100);
                    case "earth" -> living.addEffect(new MobEffectInstance(ModPotions.SNARE_EFFECT.get(), 60));
                }
            }
        }
        if (event.getEntity() instanceof Player player && (event.getSource() == DamageSource.CACTUS || event.getSource() == DamageSource.SWEET_BERRY_BUSH)) {
            if (ISchoolBangle.hasBangle(event.getEntity().level, player) == ELEMENTAL_EARTH) {
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public static void handleHealing(LivingHealEvent event) {
        if (COMMON.EnableGlyphEmpowering.get() || event.getEntity() instanceof Player player && ISchoolFocus.hasFocus(player.getLevel(), player) == ELEMENTAL_EARTH) {
            event.setAmount(event.getAmount() * 1.5F);
        }
        if (event.getEntityLiving().hasEffect(ModRegistry.HELLFIRE.get())) {
            MobEffectInstance inst = event.getEntityLiving().getEffect(ModRegistry.HELLFIRE.get());
            if (inst == null) return;
            int amplifier = Math.min(9, inst.getAmplifier());
            event.setAmount(event.getAmount() * (10 - amplifier) / 10);
            event.getEntityLiving().invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    public static void damageReduction(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource() == DamageSource.FLY_INTO_WALL && ISchoolFocus.hasFocus(event.getEntity().level, player) == ELEMENTAL_AIR) {
                event.setAmount(event.getAmount() * 0.1f);
                return;
            }
            HashMap<SpellSchool, Integer> bonusMap = new HashMap<>();
            int bonusReduction = 0;
            for (ItemStack stack : player.getArmorSlots()) {
                Item item = stack.getItem();
                if (item instanceof IElementalArmor armor && armor.doAbsorb(event.getSource())) {
                    bonusReduction++;
                    if (bonusMap.containsKey(armor.getSchool())) {
                        bonusMap.put(armor.getSchool(), bonusMap.get(armor.getSchool()) + 1);
                    } else {
                        bonusMap.put(armor.getSchool(), 1);
                    }
                }
            }
            if (bonusMap.getOrDefault(SpellSchools.ELEMENTAL_FIRE, 0) == 4 && event.getSource().isFire()) {
                player.clearFire();
            }
            if (bonusMap.getOrDefault(SpellSchools.ELEMENTAL_WATER, 0) == 4 && event.getSource() == DamageSource.DROWN) {
                player.setAirSupply(player.getMaxAirSupply());
                event.setAmount(event.getAmount() / 2);
            }
            if (bonusMap.getOrDefault(ELEMENTAL_EARTH, 0) == 4 && player.getEyePosition().y() < 60 && player.getFoodData().getFoodLevel() < 1) {
                player.getFoodData().setFoodLevel(10);
            }
            if (bonusMap.getOrDefault(ELEMENTAL_AIR, 0) == 4 && event.getSource().isFall()) {
                event.setAmount(event.getAmount() / 2);
            }

            if (bonusReduction > 0) {
                int finalBonusReduction = bonusReduction;
                CapabilityRegistry.getMana(player).ifPresent(mana -> event.getEntityLiving().addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 200, finalBonusReduction / 2)));
                event.setAmount(event.getAmount() * (1 - (bonusReduction / 10F)));
            }

        }
    }

}
