package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.spells.EntityHomingProjectile;
import alexthw.ars_elemental.registry.ModAdvTriggers;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.items.EnchantersShield;
import com.hollingsworth.arsnouveau.common.spell.casters.ReactiveCaster;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.hollingsworth.arsnouveau.setup.registry.EnchantmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class ShieldEvents {

    @SubscribeEvent
    public static void onBlock(ProjectileImpactEvent event){
        //if the entity is blocking and the projectile is a spell projectile
        if (event.getProjectile() instanceof EntityProjectileSpell projectileSpell && event.getRayTraceResult() instanceof EntityHitResult result && result.getEntity() instanceof Player player && player.isBlocking()) {
            ItemStack stack = player.getOffhandItem();
            //if the shield is an enchanters shield then check if the shield has the mirror enchantment
            if (stack.getItem() instanceof EnchantersShield) {
                int level = stack.getEnchantmentLevel(ModRegistry.MIRROR.get());
                //bounce back the spell if the random number is less than the level of the enchantment
                if (level * .25 >= Math.random()) {
                    projectileSpell.setDeltaMovement(projectileSpell.getDeltaMovement().reverse().add(0, 0.2, 0));
                    if (projectileSpell instanceof EntityHomingProjectile homing && level > 3) {
                        homing.getIgnored().add((e) -> e == player);
                    }
                    //trigger the advancement and remove mana from the player and set the cooldown of the shield to 1 second per level
                    if (player instanceof ServerPlayer) {
                        ModAdvTriggers.MIRROR.trigger((ServerPlayer) player);
                        float pay = projectileSpell.spellResolver.getResolveCost() / (level * 2f);
                        CapabilityRegistry.getMana(player).ifPresent(mana -> mana.removeMana(pay));
                        player.getCooldowns().addCooldown(stack.getItem(), 20 * level);
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockReactive(ShieldBlockEvent event){

        if (!(event.getEntity() instanceof Player player) || player.getCommandSenderWorld().isClientSide)
            return;
        ItemStack s = player.getOffhandItem();
        //if the shield is an enchanters shield then check if the shield has the reactive enchantment and if the roll is successful then cast the spell
        if (s.getItem() instanceof EnchantersShield) {
            if (s.getEnchantmentLevel(EnchantmentRegistry.REACTIVE_ENCHANTMENT.get()) * .25 >= Math.random() && new ReactiveCaster(s).getSpell().isValid()) {
                ReactiveCaster reactiveCaster = new ReactiveCaster(s);
                reactiveCaster.castSpell(player.getCommandSenderWorld(), player, InteractionHand.OFF_HAND, null);
            }
        }

    }

}
