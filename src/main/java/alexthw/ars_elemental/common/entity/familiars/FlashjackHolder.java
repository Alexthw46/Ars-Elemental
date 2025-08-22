package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.common.entity.FlashjackEntity;
import com.hollingsworth.arsnouveau.api.familiar.AbstractFamiliarHolder;
import com.hollingsworth.arsnouveau.api.familiar.IFamiliar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FlashjackHolder extends AbstractFamiliarHolder {

    public FlashjackHolder() {
        super(prefix("flashjack_familiar"), (e) -> e instanceof FlashjackEntity);
    }

    @Override
    public IFamiliar getSummonEntity(Level world, CompoundTag tag) {
        var flashjack = new FlashjackFamiliar(world);
        flashjack.setTagData(tag);
        return flashjack;
    }

    @Override
    public String getBookName() {
        return "Flashjack";
    }

    @Override
    public String getBookDescription() {
        return "A Flashjack Familiar increases the damage of Lightning spells by 2 and reduces movement-based spell costs by 20%. You can feed it a Flashpine to get a short Speed and Night Vision buff. Obtained by performing the Ritual of Binding near a Flashjack.";
    }


}
