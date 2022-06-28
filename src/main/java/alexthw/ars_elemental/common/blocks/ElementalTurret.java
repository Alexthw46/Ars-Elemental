package alexthw.ars_elemental.common.blocks;

import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.common.block.BasicSpellTurret;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ElementalTurret extends BasicSpellTurret {

    public final SpellSchool school;

    public ElementalTurret(Properties properties, SpellSchool school) {
        super(properties.noOcclusion());
        this.school = school;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElementalSpellTurretTile(pos, state).setSchool(school);
    }

}
