package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import com.hollingsworth.arsnouveau.common.enchantment.EnchantmentRegistry;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.items.EnchantersShield;
import com.hollingsworth.arsnouveau.common.spell.casters.ReactiveCaster;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class ShieldEvents {

    @SubscribeEvent
    public static void onBlock(ProjectileImpactEvent event){
        if (event.getProjectile() instanceof EntityProjectileSpell projectileSpell) {
            if (event.getRayTraceResult() instanceof EntityHitResult result) {
                if (result.getEntity() instanceof Player player && player.isBlocking()) {
                    ItemStack stack = player.getOffhandItem();
                    if (stack.getItem() instanceof EnchantersShield) {
                        int level = EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.MIRROR.get(), stack);
                        if (level > 0 && player.getRandom().nextInt(4) < level) {
                            projectileSpell.setDeltaMovement(projectileSpell.getDeltaMovement().reverse().add(0, 0.3, 0));
                            float pay = projectileSpell.spellResolver.getCastingCost(projectileSpell.spellResolver.spell, player) / 10f;
                            CapabilityRegistry.getMana(player).ifPresent(mana -> mana.removeMana(pay));
                            player.getCooldowns().addCooldown(stack.getItem(), 100);
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockReactive(ShieldBlockEvent event){

        if(!(event.getEntityLiving() instanceof Player player) || player.getCommandSenderWorld().isClientSide)
            return;
        ItemStack s = player.getOffhandItem();
        if (s.getItem() instanceof EnchantersShield) {
            if(EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.REACTIVE_ENCHANTMENT, s) * .25 >= Math.random() && new ReactiveCaster(s).getSpell().isValid()){
                ReactiveCaster reactiveCaster = new ReactiveCaster(s);
                reactiveCaster.castSpell(player.getCommandSenderWorld(), player, InteractionHand.OFF_HAND, null);
            }
        }

    }

}
