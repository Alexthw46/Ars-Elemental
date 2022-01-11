package alexthw.ars_elemental.entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import com.hollingsworth.arsnouveau.common.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.world.World;

public class AllyVhexEntity extends EntityAllyVex {
    public AllyVhexEntity(EntityType<? extends VexEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
    }

    public AllyVhexEntity(World p_i50190_2_, LivingEntity owner) {
        super(p_i50190_2_, owner);
    }

    public EntityType<?> getType() {
        return ModRegistry.VHEX_SUMMON.get();
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.5F);
    }
}
