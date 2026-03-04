package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectRage extends ElementalAbstractEffect {

    public static final EffectRage INSTANCE = new EffectRage("rage", "Rage");

    public EffectRage(String tag, String description) {
        super(tag, description);
    }

    @Override
    protected int getDefaultManaCost() {
        return 100;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    @Override
    public String getBookDescription() {
        return "Fills the target with rage, causing them to attack nearby entities and deal more damage.";
    }

}
