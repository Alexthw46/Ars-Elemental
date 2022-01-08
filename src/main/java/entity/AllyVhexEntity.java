package entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.EntityAllyVex;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

public class AllyVhexEntity extends EntityAllyVex {
    public AllyVhexEntity(EntityType<? extends Vex> p_i50190_1_, Level p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
    }

    public AllyVhexEntity(Level p_i50190_2_, LivingEntity owner) {
        super(p_i50190_2_, owner);
    }

    public EntityType<?> getType() {
        return ModRegistry.VHEX_SUMMON.get();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.MOVEMENT_SPEED, 0.5F);
    }
}
