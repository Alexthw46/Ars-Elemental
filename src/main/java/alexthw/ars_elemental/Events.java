package alexthw.ars_elemental;

import alexthw.ars_elemental.common.items.ISchoolItem;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
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

public class Events {

    @SubscribeEvent
    public static void bypassRes(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity living) {
            SpellSchool focus = ISchoolItem.hasFocus(event.getEntity().level, player);
            if (focus != null) {
                switch (focus.getId()) {
                    case "fire" -> {
                        if (event.getSource().isFire() && living.fireImmune()) {
                            event.setCanceled(true);
                            living.hurt(DamageSource.playerAttack(player).setMagic(), event.getAmount());
                        }
                    }
                    case "water" -> {
                        if (event.getSource().getMsgId().equals("drown") && living.getMobType() == MobType.WATER) {
                            event.setCanceled(true);
                            living.hurt(DamageSource.playerAttack(player).setMagic(), event.getAmount());
                        }
                    }
                    case "air" -> {
                        if (living.invulnerableTime > 0 && (event.getSource().isMagic() || event.getSource().isFall())) {
                            living.invulnerableTime = 0;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void boostHealing(LivingHealEvent event){
        if (COMMON.EnableGlyphEmpowering.get() || event.getEntity() instanceof Player player && ISchoolItem.hasFocus(player.getLevel(), player) == ELEMENTAL_EARTH) {
            event.setAmount(event.getAmount() * 1.5F);
        }
    }

    @SubscribeEvent
    public static void saveFromElytra(LivingHurtEvent event) {
        if (event.getSource() == DamageSource.FLY_INTO_WALL && event.getEntity() instanceof Player player) {
            SpellSchool focus = ISchoolItem.hasFocus(event.getEntity().level, player);
            if (focus != null && focus == ELEMENTAL_AIR) {
                event.setAmount(event.getAmount() * 0.1f);
            }
        }
    }

}