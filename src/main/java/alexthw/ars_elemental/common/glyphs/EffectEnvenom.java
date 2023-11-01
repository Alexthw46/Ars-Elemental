package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectEnvenom extends ElementalAbstractEffect implements IPotionEffect, IDamageEffect {

    public static EffectEnvenom INSTANCE = new EffectEnvenom();

    public EffectEnvenom() {
        super("envenom", "Envenom");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity target && canDamage(shooter, spellStats, spellContext, resolver, target)) {
            MobEffectInstance poison = target.getEffect(MobEffects.POISON);
            if (poison != null) {
                spellStats.setAmpMultiplier(poison.getAmplifier() / 2F + spellStats.getAmpMultiplier());
                ((IPotionEffect) this).applyConfigPotion(target, ModPotions.VENOM.get(), spellStats);
            } else {
                ((IPotionEffect) this).applyConfigPotion(target, MobEffects.POISON, spellStats);
            }
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 20;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }


    @NotNull
    @Override
    public Set<SpellSchool> getSchools() {
        return setOf(SpellSchools.ELEMENTAL_EARTH);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 5);
        addExtendTimeConfig(builder, 5);
    }

    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 30 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 8 : EXTEND_TIME.get();
    }
}
