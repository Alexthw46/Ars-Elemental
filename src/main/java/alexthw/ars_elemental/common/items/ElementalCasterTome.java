package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.api.item.ISchoolFocus;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.items.CasterTome;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElementalCasterTome extends CasterTome implements ISchoolFocus {
    private final SpellSchool school;

    public ElementalCasterTome(Properties properties, SpellSchool school) {
        super(properties);
        this.school = school;
    }

    @Override
    public SpellSchool getSchool() {
        return school;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        tooltip2.add(Component.translatable("tooltip.ars_elemental.caster_tome"));
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
    }
}
