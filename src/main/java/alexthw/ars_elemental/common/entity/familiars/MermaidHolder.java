package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.familiar.IFamiliar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MermaidHolder extends AbstractFamiliarHolder {
    public MermaidHolder() {
        super(prefix("siren_familiar"), (e) -> e instanceof MermaidEntity);
    }

    @Override
    public IFamiliar getSummonEntity(Level level, CompoundTag compoundTag) {
        MermaidFamiliar mermaid = new MermaidFamiliar(level);
        mermaid.setTagData(compoundTag);
        return mermaid;
    }

    public String getBookName() {
        return "Siren";
    }

}
