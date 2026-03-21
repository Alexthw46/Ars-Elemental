package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.entity.ai.SelfCastGoal;
import alexthw.ars_elemental.common.glyphs.EffectBubbleShield;
import alexthw.ars_elemental.common.glyphs.EffectCavitate;
import alexthw.ars_elemental.common.glyphs.EffectSpike;
import alexthw.ars_elemental.common.glyphs.EffectWaterGrave;
import alexthw.ars_elemental.common.glyphs.EffectWaterJet;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBubble;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBurst;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectColdSnap;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectDelay;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WaterMage extends EntityMageBase {

    public static final Spell MANA_SHIELD_SPELL = new Spell(MethodSelf.INSTANCE, EffectBubbleShield.INSTANCE);

    /**
     * ProjSpells
     * 1: WaterGrave
     * 2: Cold Snapper
     * 3: Burst Spike
     * <p>
     * SelfSpells
     * 1: Bubble Shield
     */
    public WaterMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level, SpellSchools.ELEMENTAL_WATER);
        pSpells.add(new Spell(MethodHomingProjectile.INSTANCE, AugmentSensitive.INSTANCE, EffectBubble.INSTANCE, EffectWaterGrave.INSTANCE, AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE));
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectFreeze.INSTANCE, EffectColdSnap.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectBurst.INSTANCE, AugmentSensitive.INSTANCE, EffectSpike.INSTANCE, AugmentAmplify.INSTANCE));

        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectBubble.INSTANCE, EffectDelay.INSTANCE, EffectCavitate.INSTANCE));
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectWaterJet.INSTANCE, EffectDelay.INSTANCE, EffectWaterJet.INSTANCE));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SelfCastGoal<>(this, () -> MANA_SHIELD_SPELL, () -> !this.hasEffect(ModPotions.MANA_BUBBLE) && this.getHealth() < this.getMaxHealth() * 0.8, 1, 10));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource randomSource, @NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(randomSource, pDifficulty);
        for (var book : this.getHandSlots()) book.set(DataComponents.BASE_COLOR, DyeColor.CYAN);
    }

    public WaterMage(Level level) {
        this(ModEntities.WATER_MAGE.get(), level);
    }

}
