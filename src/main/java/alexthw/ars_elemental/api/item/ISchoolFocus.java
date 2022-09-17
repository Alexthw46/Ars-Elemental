package alexthw.ars_elemental.api.item;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;

public interface ISchoolFocus extends ISpellModifierItem, ISchoolItem {
    static @Nullable SpellSchool hasFocus(Level world, Entity entity) {
        if (entity instanceof FirenandoEntity) return SpellSchools.ELEMENTAL_FIRE;
        if (entity instanceof EntityMageBase mage) return mage.school;
        if (!world.isClientSide && entity instanceof Player player) {
            for (InteractionHand curHand : InteractionHand.values()) {
                Item hand = player.getItemInHand(curHand).getItem();
                if (hand instanceof ISchoolFocus focus) {
                    return focus.getSchool();
                }
            }
            SlotResult curio = CuriosApi.getCuriosHelper().findFirstCurio(player, c -> (c.getItem() instanceof ISchoolFocus)).orElse(null);
            if (curio != null && curio.stack().getItem() instanceof ISchoolFocus focus) {
                return focus.getSchool();
            }
        }
        return null;
    }

}
