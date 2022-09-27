package alexthw.ars_elemental.registry;

import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class ModAdvTriggers extends ANCriteriaTriggers {

    public static void init() {}

    public static final PlayerTrigger MIRROR = register(new PlayerTrigger(prefix("mirror_shield")));

    public static final PlayerTrigger BLOSSOM = register(new PlayerTrigger(prefix("blossoming")));

    public static final PlayerTrigger LEVITATE = register(new PlayerTrigger(prefix("levitating")));


}
