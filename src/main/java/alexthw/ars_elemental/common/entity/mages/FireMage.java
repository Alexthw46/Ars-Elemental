package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.entity.ai.SelfCastGoal;
import alexthw.ars_elemental.common.glyphs.EffectCauterize;
import alexthw.ars_elemental.common.glyphs.EffectConflagrate;
import alexthw.ars_elemental.common.glyphs.MethodArcProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectExplosion;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFirework;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHeal;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;
import org.jetbrains.annotations.NotNull;

public class FireMage extends EntityMageBase {

    /**
     * ProjSpells
     * 1: Flare bolt
     * 2: Bouncing Firework
     * 3: Homing Explosion
     */
    public FireMage(EntityType<? extends EntityMageBase> type, Level level) {
        super(type, level, SpellSchools.ELEMENTAL_FIRE);
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectIgnite.INSTANCE, EffectFlare.INSTANCE));
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectIgnite.INSTANCE, EffectConflagrate.INSTANCE));
        pSpells.add(new Spell(MethodArcProjectile.INSTANCE, AugmentSplit.INSTANCE, AugmentSplit.INSTANCE, AugmentAccelerate.INSTANCE, AugmentPierce.INSTANCE, EffectFirework.INSTANCE, AugmentAOE.INSTANCE, AugmentAmplify.INSTANCE, AugmentExtendTime.INSTANCE));
        pSpells.add(new Spell(MethodHomingProjectile.INSTANCE, AugmentSensitive.INSTANCE, EffectExplosion.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE));
    }

    public FireMage(Level level) {
        this(ModEntities.FIRE_MAGE.get(), level);
    }

    public static final Spell PURIFY_SPELL = new Spell(MethodSelf.INSTANCE, EffectCauterize.INSTANCE, EffectHeal.INSTANCE);

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SelfCastGoal<>(this, () -> PURIFY_SPELL, () -> this.getHealth() < this.getMaxHealth() * 0.8 && this.getActiveEffects().stream().anyMatch(e -> e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL && e.getCures().contains(EffectCures.MILK)), 1, 10));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        for (var book : this.getHandSlots()) book.set(DataComponents.BASE_COLOR, DyeColor.RED);
    }

}
