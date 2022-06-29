package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.items.foci.NecroticFocus;
import alexthw.ars_elemental.mixin.FoxInvoker;
import alexthw.ars_elemental.util.EntityCarryMEI;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static alexthw.ars_elemental.registry.ModRegistry.ENTHRALLED;

public class EffectCharm extends ElementalAbstractEffect {

    public static EffectCharm INSTANCE = new EffectCharm();

    public EffectCharm() {
        super("charm", "Charm");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        if (shooter instanceof Player player && world instanceof ServerLevel level) {
            if (rayTraceResult.getEntity() instanceof Monster mob) {

                if (mob.getMaxHealth() < GENERIC_INT.get() || player.getUUID().equals(ArsElemental.Dev)) {

                    int resistance = (int) (mob.getHealth() / mob.getMaxHealth() * 100);
                    double chanceBoost = 10 + spellStats.getAmpMultiplier() * 5;

                    if (mob.getMobType() == MobType.UNDEAD && NecroticFocus.hasFocus(world, shooter)) {
                        chanceBoost += 50;
                    }

                    if (rollToSeduce(resistance, chanceBoost, level.getRandom())) {
                        applyConfigPotion(mob, player, ENTHRALLED.get(), spellStats);
                        playHeartParticles(mob,level);
                    }
                }

            } else if (rayTraceResult.getEntity() instanceof Animal animal) {

                if (animal instanceof TamableAnimal tamable && !tamable.isTame()) {
                    if (rollToSeduce(100, 25 * (1+spellStats.getAmpMultiplier()), level.getRandom()))
                        tamable.tame(player);
                }else if (animal instanceof AbstractHorse horse && !horse.isTamed()) {
                    if (rollToSeduce(100, 25 * (1 + spellStats.getAmpMultiplier()), level.getRandom()))
                        horse.setTamed(true);
                } else if (animal instanceof Fox fox && !((FoxInvoker) fox).callTrusts(player.getUUID())) {
                    if (rollToSeduce(100, 25 * (1 + spellStats.getAmpMultiplier()), level.getRandom()))
                        ((FoxInvoker) fox).callAddTrustedUUID(player.getUUID());
                } else if (animal.canFallInLove()) {
                    if (rollToSeduce(90, 25 * (1 + spellStats.getAmpMultiplier()), level.getRandom()))
                        animal.setInLove(player);
                }

            }
        }
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    private boolean rollToSeduce(int resistance, double chanceBoost, RandomSource rand) {
        return (rand.nextInt(0, 75) + chanceBoost) >= resistance;
    }

    @Override
    public int getDefaultManaCost() {
        return 30;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDefaultPotionConfig(builder);
        addGenericInt(builder,150, "Set the max hp limit for Charm, mobs with more max hp will be immune.","charm_hp_limit");
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

    private void playHeartParticles(LivingEntity entity, ServerLevel world){
        for(int i = 0; i < 5; ++i) {
            double d0 = entity.getRandom().nextGaussian() * 0.02D;
            double d1 = entity.getRandom().nextGaussian() * 0.02D;
            double d2 = entity.getRandom().nextGaussian() * 0.02D;
            world.sendParticles(ParticleTypes.HEART, entity.getX() + (world.random.nextFloat() - 0.5)/2, entity.getY() + (world.random.nextFloat() + 0.5), entity.getZ() + (world.random.nextFloat() - 0.5)/2,2 , d0, d1, d2, 0.1f);
        }
    }

}
