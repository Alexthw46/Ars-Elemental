package alexthw.ars_elemental.common.entity.ai;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class MermaidJump<M extends MermaidEntity> extends Behavior<M> {

    public MermaidJump(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pDuration) {
        super(pEntryCondition, pDuration);
    }


}
