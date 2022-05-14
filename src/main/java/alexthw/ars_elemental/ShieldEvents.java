package alexthw.ars_elemental;

import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.enchantment.EnchantmentRegistry;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.items.EnchantersShield;
import com.hollingsworth.arsnouveau.common.spell.casters.ReactiveCaster;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class ShieldEvents {

    @SubscribeEvent
    public static void onBlock(ProjectileImpactEvent event){
        if (event.getProjectile() instanceof EntityProjectileSpell spell){
            if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY){
                if (((EntityHitResult) event.getRayTraceResult()).getEntity() instanceof Player player && player.isBlocking()){
                    ItemStack stack = player.getOffhandItem();
                    if (stack.getItem() instanceof EnchantersShield){
                        int level = EnchantmentHelper.getItemEnchantmentLevel(ModRegistry.MIRROR.get(), stack);
                        if (level > 0 && player.getRandom().nextInt(5) < level){
                            spell.setDeltaMovement(spell.getDeltaMovement().reverse());
                            //spell.spellResolver.expendMana(player);
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
