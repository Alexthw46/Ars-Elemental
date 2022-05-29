package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.items.ISchoolItem;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
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
    public static void makeMagic(SpellDamageEvent event) {
        if (COMMON.GlobalMagic.get()) event.damageSource.setMagic();
    }

    @SubscribeEvent
    public static void bypassRes(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity living) {
            SpellSchool focus = ISchoolItem.hasFocus(event.getEntity().level, player);
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
    public static void boostHealing(LivingHealEvent event) {
        if (COMMON.EnableGlyphEmpowering.get() || event.getEntity() instanceof Player player && ISchoolItem.hasFocus(player.getLevel(), player) == ELEMENTAL_EARTH) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }

    @SubscribeEvent
    public static void saveFromElytra(LivingHurtEvent event) {
        if (event.getSource() == DamageSource.FLY_INTO_WALL && event.getEntity() instanceof Player player) {
            SpellSchool focus = ISchoolItem.hasFocus(event.getEntity().level, player);
            if (focus == ELEMENTAL_AIR) {
                event.setAmount(event.getAmount() * 0.1f);
            }
        }
    }

}
