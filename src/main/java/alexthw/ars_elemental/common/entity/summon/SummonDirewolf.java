package alexthw.ars_elemental.common.entity.summon;

import alexthw.ars_elemental.api.IUndeadSummon;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.SummonWolf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SummonDirewolf extends SummonWolf implements IUndeadSummon {
    public SummonDirewolf(EntityType<? extends Wolf> type, Level levelIn) {
        super(type, levelIn);
    }

    public SummonDirewolf(Level level){
        super(ModEntities.DIREWOLF_SUMMON.get(), level);
    }

    public SummonDirewolf(Level world, Player player, SummonWolf oldWolf) {
        this(world);
        Vec3 hit = oldWolf.position();
        setPos(hit.x(), hit.y(), hit.z());
        setTarget(player.getLastHurtMob());
        setAggressive(true);
        tame(player);
        oldWolf.getActiveEffects().stream().filter(e -> e.getEffect().isBeneficial()).forEach(this::addEffect);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5F).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

}
