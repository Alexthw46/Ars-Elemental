package alexthw.ars_elemental.common.items.armor;

import com.hollingsworth.arsnouveau.api.perk.Perk;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SummonSickPerk extends Perk {

    public static SummonSickPerk INSTANCE = new SummonSickPerk();

    public SummonSickPerk() {
        super(prefix("thread_summon_sick"));
    }

}
