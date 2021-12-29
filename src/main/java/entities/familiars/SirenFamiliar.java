package entities.familiars;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.familiar.IFamiliar;
import entities.MermaidEntity;
import net.minecraft.world.level.Level;

public class SirenFamiliar extends AbstractFamiliarHolder {
    public SirenFamiliar() {
        super("siren", (e) -> e instanceof MermaidEntity);
    }

    public IFamiliar getSummonEntity(Level world) {
        return new MermaidFamiliar(ModRegistry.SIREN_FAMILIAR.get(), world);
    }

    public String getBookName() {
        return "Siren";
    }

    public String getBookDescription() {
        return "A Siren";
    }
}
