package alexthw.ars_elemental.api;

import com.hollingsworth.arsnouveau.api.entity.ISummon;

public interface IUndeadSummon extends ISummon {

    default void inherit(ISummon living){
        //copy the old summon data to this summon
        int ticksLeft = Math.max(living.getTicksLeft(), 400);
        setTicksLeft(ticksLeft);
        setOwnerID(living.getOwnerUUID());
        living.setTicksLeft(0);
    }

}
