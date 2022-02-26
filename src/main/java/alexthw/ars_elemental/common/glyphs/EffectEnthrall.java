package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.mob_effects.EnthrallEffect;
import alexthw.ars_elemental.util.EntityCarryMEI;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.Set;

import static alexthw.ars_elemental.ModRegistry.ENTHRALLED;

public class EffectEnthrall extends AbstractEffect {

    public static EffectEnthrall INSTANCE = new EffectEnthrall();

    public EffectEnthrall() {
        super("enthrall", "Enthrall");
    }

    @Override
    public boolean wouldSucceed(HitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {
        return livingEntityHitSuccess(rayTraceResult);
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext) {

        if (rayTraceResult.getEntity() instanceof Mob mob && shooter instanceof Player player){

            //TODO balance
            if (mob.getMaxHealth() < 150) {

                int resistance = (int) (mob.getHealth()/mob.getMaxHealth() * 100);
                double chanceBoost = 10 + spellStats.getAmpMultiplier() * 5;

                if (rollToSeduce(resistance, chanceBoost, mob.getRandom())) {
                    applyConfigPotion(mob, player, ENTHRALLED.get(), spellStats);
                }
            }

        }

    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    private boolean rollToSeduce(int resistance, double chanceBoost, Random rand) {
        return (rand.nextInt(0,100) + chanceBoost) >= resistance;
    }

    @Override
    public int getDefaultManaCost() {
        return 30;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDefaultPotionConfig(builder);
    }

    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    public void applyConfigPotion(LivingEntity entity, LivingEntity owner, MobEffect potionEffect, SpellStats spellStats){
        applyPotion(entity, owner,  potionEffect, spellStats, POTION_TIME == null ? 30 : POTION_TIME.get(), EXTEND_TIME == null ? 8 : EXTEND_TIME.get(), true);
    }

    public void applyPotion(LivingEntity entity, LivingEntity owner, MobEffect potionEffect, SpellStats stats, int baseDurationSeconds, int durationBuffSeconds, boolean showParticles){
        if (entity == null || owner == null) return;
        int ticks = baseDurationSeconds * 20 + durationBuffSeconds * stats.getDurationInTicks();
        int amp = (int) stats.getAmpMultiplier();
        entity.addEffect(new EntityCarryMEI(potionEffect, ticks, amp, false, showParticles, owner, null));
    }

}
