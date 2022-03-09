package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.common.entity.FirenandoEntity;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.familiar.IFamiliar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class FirenandoHolder extends AbstractFamiliarHolder {

    public FirenandoHolder() {
        super("firenando", (e) -> e instanceof FirenandoEntity);
    }

    @Override
    public IFamiliar getSummonEntity(Level world, CompoundTag tag) {
        FirenandoFamiliar Firenando = new FirenandoFamiliar(world);
        Firenando.setTagData(tag);
        return Firenando;
    }

    @Override
    public String getBookName() {
        return "Fire Golem";
    }
}
