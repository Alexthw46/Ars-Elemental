package alexthw.ars_elemental.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;

public class SummonDirewolf extends SummonWolf {
    public SummonDirewolf(EntityType<? extends WolfEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public SummonDirewolf(World world){
        super(ModRegistry.DIREWOLF_SUMMON.get(), world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.4F).add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }
}
