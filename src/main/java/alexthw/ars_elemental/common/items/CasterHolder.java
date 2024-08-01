package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.CasterHolderContainer;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.common.armor.AnimatedMagicArmor;
import com.hollingsworth.arsnouveau.common.items.SpellArrow;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CasterHolder extends CurioHolder {

    public CasterHolder(Properties pProperties) {
        super(pProperties);
    }

    public static boolean canStore(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof CurioHolder) return false;
        return item instanceof ICasterTool || item instanceof AnimatedMagicArmor || item instanceof SpellArrow || stack.is(ModRegistry.CASTER_BAGGABLE);
    }

    @Override
    public void openContainer(Level level, Player player, ItemStack bag, int index) {
        if (!level.isClientSide) {
            MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new CasterHolderContainer(w, p, bag), bag.getHoverName());
            //NetworkHooks.openScreen((ServerPlayer) player, container, b -> b.writeInt(index));
            player.level().playSound(null, player.blockPosition(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 1, 1);
        }
    }

}
