package entity.familiars;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.familiar.IFamiliar;
import entity.MermaidEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class SirenFamiliar extends AbstractFamiliarHolder {
    public SirenFamiliar() {
        super("siren", (e) -> e instanceof MermaidEntity);
    }

    @Override
    public IFamiliar getSummonEntity(Level level, CompoundTag compoundTag) {
        MermaidFamiliar mermaid =  new MermaidFamiliar(ModRegistry.SIREN_FAMILIAR.get(), level);
        mermaid.setTagData(compoundTag);
        return mermaid;
    }

    public String getBookName() {
        return "Siren";
    }

    public String getBookDescription() {
        return "A Siren";
    }
}
