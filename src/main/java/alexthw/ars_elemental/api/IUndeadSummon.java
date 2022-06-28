package alexthw.ars_elemental.api;

import com.hollingsworth.arsnouveau.api.entity.ISummon;

public interface IUndeadSummon extends ISummon {

    default void inherit(ISummon living){
        int ticksLeft = Math.max(living.getTicksLeft(), 1200);
        setTicksLeft(ticksLeft);
        setOwnerID(living.getOwnerID());
        living.setTicksLeft(0);
    }

}
