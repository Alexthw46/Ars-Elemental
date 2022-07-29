package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.util.EntityCarryMEI;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static alexthw.ars_elemental.registry.ModPotions.LIFE_LINK;

public class EffectLifeLink extends ElementalAbstractEffect {

    public static EffectLifeLink INSTANCE = new EffectLifeLink();

    public EffectLifeLink() {
        super("life_link", "Life Link");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        if (rayTraceResult.getEntity() instanceof LivingEntity livingEntity && shooter instanceof Player player && player != livingEntity) {

            applyConfigPotion(livingEntity, player, LIFE_LINK.get(), spellStats);

        }

    }

    @Override
    public SpellTier getTier() {
        return SpellTier.THREE;
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
        return getSummonAugments();
    } //just time boosters

    public void applyConfigPotion(LivingEntity entity, LivingEntity owner, MobEffect potionEffect, SpellStats spellStats){
        applyPotion(entity, owner,  potionEffect, spellStats, POTION_TIME == null ? 30 : POTION_TIME.get(), EXTEND_TIME == null ? 8 : EXTEND_TIME.get(), true);
    }

    public void applyPotion(LivingEntity entity, LivingEntity owner, MobEffect potionEffect, SpellStats stats, int baseDurationSeconds, int durationBuffSeconds, boolean showParticles) {
        if (entity == null || owner == null) return;
        int ticks = baseDurationSeconds * 20 + durationBuffSeconds * stats.getDurationInTicks();
        entity.addEffect(new EntityCarryMEI(potionEffect, ticks, 0, false, showParticles, owner, entity));
        owner.addEffect(new EntityCarryMEI(potionEffect, ticks, 0, false, showParticles, owner, entity));
    }
}
