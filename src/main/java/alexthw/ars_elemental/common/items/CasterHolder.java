package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.CasterHolderContainer;
import com.hollingsworth.arsnouveau.common.crafting.recipes.IDyeable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CasterHolder extends CurioHolder implements IDyeable {

    public CasterHolder(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void openContainer(Level level, Player player, ItemStack bag, int index) {
        if (!level.isClientSide) {
            MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new CasterHolderContainer(w, p, bag), bag.getHoverName());
            player.openMenu(container, b -> b.writeInt(index));
            player.level().playSound(null, player.blockPosition(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 1, 1);
        }
    }

}
