package alexthw.ars_elemental.common.blocks;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.block.tile.BasicSpellTurretTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class ElementalSpellTurretTile extends BasicSpellTurretTile {

    private SpellSchool school;

    public ElementalSpellTurretTile(BlockPos pos, BlockState state) {
        super(ModTiles.ELEMENTAL_TURRET.get(), pos, state);
    }

    public SpellSchool getSchool() {
        return this.school;
    }

    @Override
    public int getManaCost() {
        Spell spell = this.spellCaster.getSpell();
        if (spell.recipe.stream().anyMatch(p -> this.school.isPartOfSchool(p))) {
            return (int) (spell.getNoDiscountCost() * 0.35);
        } else {
            return (int) (spell.getNoDiscountCost() * 0.6);
        }
    }

    public ElementalSpellTurretTile setSchool(SpellSchool school) {
        this.school = school;
        return this;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("school", school.getId());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.school = switch (tag.getString("school")) {
            case "fire" -> SpellSchools.ELEMENTAL_FIRE;
            case "water" -> SpellSchools.ELEMENTAL_WATER;
            case "air" -> SpellSchools.ELEMENTAL_AIR;
            case "earth" -> SpellSchools.ELEMENTAL_EARTH;
            default -> SpellSchools.ELEMENTAL;
        };
    }
}
