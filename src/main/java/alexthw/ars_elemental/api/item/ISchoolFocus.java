package alexthw.ars_elemental.api.item;

import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;

public interface ISchoolFocus extends ISpellModifierItem, ISchoolProvider {
    static @Nullable SpellSchool hasFocus(Entity entity) {
        if (entity instanceof ISchoolProvider mage) return mage.getSchool();
        else if (entity instanceof Player player) {
            var focus = getFocus(player);
            if (focus != null) return focus.getSchool();
        }
        return null;
    }

    static ISchoolFocus getFocus(@NotNull Player player) {
        //check the player's hands and curios for a focus and return the school if found
        for (InteractionHand curHand : InteractionHand.values()) {
            Item hand = player.getItemInHand(curHand).getItem();
            if (hand instanceof ISchoolFocus focus) {
                return focus;
            }
        }
        SlotResult curio = CuriosApi.getCuriosHelper().findFirstCurio(player, c -> (c.getItem() instanceof ISchoolFocus)).orElse(null);
        if (curio != null && curio.stack().getItem() instanceof ISchoolFocus focus) {
            return focus;
        }
        return null;
    }

    double getDiscount();

}
