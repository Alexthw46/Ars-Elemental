package alexthw.ars_elemental.registry;

import com.hollingsworth.arsnouveau.common.advancement.ANCriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModAdvTriggers extends ANCriteriaTriggers {

    public static void init() {}

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> MIRROR = register("mirror_shield");

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> BLOSSOM = register("blossoming");

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> LEVITATE = register("levitating");


}
