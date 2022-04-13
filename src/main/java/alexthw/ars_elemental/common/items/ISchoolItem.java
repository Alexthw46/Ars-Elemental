package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Optional;

public interface ISchoolItem extends ISpellModifierItem {

    static SpellSchool hasFocus(Level world, Entity entity) {
        if (!world.isClientSide && entity instanceof Player player) {
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems(player).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                for (int i = 0; i < items.getSlots(); ++i) {
                    Item item = items.getStackInSlot(i).getItem();
                    if (item instanceof ISchoolItem focus) {
                        return focus.getSchool();
                    }
                }
            }
            Item hand = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
            if (hand instanceof ISchoolItem focus) {
                return focus.getSchool();
            }
        }
        if (entity instanceof FirenandoEntity) return SpellSchools.ELEMENTAL_FIRE;
        if (entity instanceof EntityMageBase mage) return mage.school;
        return null;
    }

    SpellSchool getSchool();

}
