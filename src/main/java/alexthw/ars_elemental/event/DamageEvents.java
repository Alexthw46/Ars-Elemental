package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.items.ISchoolBangle;
import alexthw.ars_elemental.common.items.ISchoolFocus;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
                        if (event.getSource().isFire() && living.hasEffect(ModRegistry.HELLFIRE.get())) {
                            event.setCanceled(true);
                            DamageSource newDamage = new EntityDamageSource("hellflare", player).setMagic();
                            if (event.getSource().isBypassArmor()) newDamage.bypassArmor();
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
                }
            }
        } else if (event.getSource().getEntity() instanceof FirenandoEntity FE) {
            if (event.getEntity().fireImmune() && event.getSource().isFire()) {
                event.setCanceled(true);
                event.getEntityLiving().hurt(DamageSource.mobAttack(FE).setMagic().bypassArmor(), event.getAmount());
            } else if (!(event.getEntity() instanceof Monster)) {
                event.setCanceled(true);
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
                    case "earth" -> living.addEffect(new MobEffectInstance(ModPotions.SNARE_EFFECT, 60));
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
    public static void boostHealing(LivingHealEvent event) {
        if (COMMON.EnableGlyphEmpowering.get() || event.getEntity() instanceof Player player && ISchoolFocus.hasFocus(player.getLevel(), player) == ELEMENTAL_EARTH) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }

    @SubscribeEvent
    public static void damageReduction(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && event.getSource() == DamageSource.FLY_INTO_WALL)
            if (ISchoolFocus.hasFocus(event.getEntity().level, player) == ELEMENTAL_AIR) {
                event.setAmount(event.getAmount() * 0.1f);
            }
    }

}
