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

public class ElementalTurret extends BasicSpellTurret {

    public final SpellSchool school;

    public ElementalTurret(Properties properties, SpellSchool school) {
        super(properties.noOcclusion().forceSolidOn());
        this.school = school;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
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
            if (stack.getItem() instanceof ISchoolFocus focus) {
                return school == focus.getSchool();
            } else if (stack.getItem() == ItemsRegistry.SHAPERS_FOCUS.get()) {
                return school == SpellSchools.MANIPULATION;
            }
            return super.hasFocus(stack);
        }

        @Override
        public boolean hasFocus(Item item) {
            return hasFocus(item.getDefaultInstance());
        }


        @Override
        public SpellResolver getNewResolver(SpellContext context) {
            return new TurretSpellResolver(context, school);
        }
    }

}
