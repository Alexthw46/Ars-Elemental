package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.glyphs.EffectWaterGrave;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectColdSnap;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WaterMage extends EntityMageBase {
    /**
     * ProjSpells
     * 1: WaterGrave
     * 2: Cold Snapper
     */
    public WaterMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level);
        this.school = SpellSchools.ELEMENTAL_WATER;
        pSpells.add(new Spell(MethodHomingProjectile.INSTANCE, EffectWaterGrave.INSTANCE, AugmentExtendTime.INSTANCE, EffectWaterGrave.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE, EffectWaterGrave.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectFreeze.INSTANCE, EffectColdSnap.INSTANCE, AugmentAmplify.INSTANCE));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        ItemStack book = this.getItemInHand(InteractionHand.MAIN_HAND);
        book.getOrCreateTag().putInt("color", 9);
    }

    public WaterMage(Level level) {
        this(ModEntities.WATER_MAGE.get(), level);
    }

}
