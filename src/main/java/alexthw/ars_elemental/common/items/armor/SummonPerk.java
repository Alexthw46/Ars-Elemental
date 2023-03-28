package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.perk.Perk;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SummonPerk extends Perk {

    public static SummonPerk INSTANCE = new SummonPerk();

    public SummonPerk() {
        super(prefix("thread_summon"));
    }

}
