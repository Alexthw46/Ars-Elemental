package alexthw.ars_elemental.common.blocks;

import alexthw.ars_elemental.api.item.ISchoolFocus;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ElementalTurret extends BasicSpellTurret {

    public final SpellSchool school;

    public ElementalTurret(Properties properties, SpellSchool school) {
        super(properties.noOcclusion().forceSolidOn());
        this.school = school;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ElementalSpellTurretTile(pos, state).setSchool(school);
    }

    static class TurretSpellResolver extends EntitySpellResolver {

        SpellSchool school;

        public TurretSpellResolver(SpellContext context, SpellSchool tile) {
            super(context);
            this.school = tile;
        }

        @Override
        public boolean hasFocus(ItemStack stack) {
            return hasFocus(stack.getItem());
        }

        @Override
        public boolean hasFocus(Item item) {
            if (item instanceof ISchoolFocus focus) {
                return school == focus.getSchool();
            } else if (item == ItemsRegistry.SHAPERS_FOCUS.get()) {
                return school == SpellSchools.MANIPULATION;
            }
            return super.hasFocus(item);
        }


        @Override
        public SpellResolver getNewResolver(SpellContext context) {
            return new TurretSpellResolver(context, school);
        }
    }

}
