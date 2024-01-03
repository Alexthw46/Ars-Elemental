package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IceSpikeEntity extends DripstoneSpikeEntity {
    public IceSpikeEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public IceSpikeEntity(Level world, double x, double y, double z, float baseDamage, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        super(world, x, y, z, baseDamage, shooter, spellStats, spellContext, resolver, ModEntities.ICE_SPIKE.get());
    }

    @Override
    public void damage(LivingEntity entity) {
        super.damage(entity);
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.ICE_SPIKE.get();
    }
}
