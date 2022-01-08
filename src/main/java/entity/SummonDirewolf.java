package entity;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;

public class SummonDirewolf extends SummonWolf {
    public SummonDirewolf(EntityType<? extends Wolf> type, Level levelIn) {
        super(type, levelIn);
    }

    public SummonDirewolf(Level level){
        super(ModRegistry.DIREWOLF_SUMMON.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.4F).add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.ATTACK_DAMAGE, 4.0D);
    }
}
