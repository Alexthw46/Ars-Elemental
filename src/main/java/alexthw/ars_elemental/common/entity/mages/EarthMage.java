package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectBreak;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectHarm;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EarthMage extends EntityMageBase {

    /**
     * ProjSpells
     * 1: Poison bolt
     * 2: Crushing spell
     * 3: Bouncing Hole trap
     */
    public EarthMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level);
        this.school = SpellSchools.ELEMENTAL_EARTH;
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectHarm.INSTANCE, AugmentExtendTime.INSTANCE, EffectHarm.INSTANCE));
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectCrush.INSTANCE, AugmentAmplify.INSTANCE, AugmentAmplify.INSTANCE));
        pSpells.add(new Spell(MethodCurvedProjectile.INSTANCE, AugmentPierce.INSTANCE, EffectBreak.INSTANCE, AugmentPierce.INSTANCE, AugmentAOE.INSTANCE, AugmentAOE.INSTANCE));
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        ItemStack book = this.getItemInHand(InteractionHand.MAIN_HAND);
        book.getOrCreateTag().putInt("color", 13);
    }

    public EarthMage(Level level) {
        this(ModEntities.EARTH_MAGE.get(), level);
    }
}
