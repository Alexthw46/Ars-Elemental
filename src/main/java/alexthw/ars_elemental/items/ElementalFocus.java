package alexthw.ars_elemental.items;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ArsNouveau.MODID)
public class ElementalFocus extends Item implements ISpellModifierItem {

    public final SpellSchool element;

    public ElementalFocus(Properties properties, SpellSchool element) {
        super(properties);
        this.element = element;
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (element.isPartOfSchool(spellPart)) {
            builder.addAmplification(2.0D);
        }else{
            builder.addAmplification(-1.0D);
        }
        return builder;
    }

    public SpellStats.Builder getSimpleStats(SpellStats.Builder builder) {
        builder.addDamageModifier(1.0D);
        return builder;
    }


    public static boolean containsThis(Level world, Entity entity) {
        if (!world.isClientSide && entity instanceof Player) {
            IItemHandlerModifiable items = CuriosUtil.getAllWornItems((LivingEntity) entity).orElse(null);
            if (items != null) {
                for (int i = 0; i < items.getSlots(); ++i) {
                    Item item = items.getStackInSlot(i).getItem();
                    if (item instanceof ElementalFocus) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<AbstractCastMethod> sympatheticMethods = new ArrayList<>();

    static {
        sympatheticMethods.add(MethodProjectile.INSTANCE);
    }
}
