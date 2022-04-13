package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectExplosion;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFirework;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FireMage extends EntityMageBase {

    /**
     * ProjSpells
     * 1: Flare bolt
     * 2: Bouncing Firework
     * 3: Homing Explosion
     */
    public FireMage(EntityType<? extends EntityMageBase> type, Level level) {
        super(type, level);
        pSpells.add(new Spell(MethodProjectile.INSTANCE, EffectIgnite.INSTANCE, EffectFlare.INSTANCE));
        pSpells.add(new Spell(MethodCurvedProjectile.INSTANCE, AugmentAccelerate.INSTANCE, AugmentPierce.INSTANCE, EffectIgnite.INSTANCE, EffectFirework.INSTANCE, AugmentAOE.INSTANCE, AugmentAmplify.INSTANCE, AugmentExtendTime.INSTANCE));
        pSpells.add(new Spell(MethodHomingProjectile.INSTANCE, AugmentSensitive.INSTANCE, EffectIgnite.INSTANCE, EffectExplosion.INSTANCE, AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE));
        this.school = SpellSchools.ELEMENTAL_FIRE;
    }

    public FireMage(Level level) {
        this(ModEntities.FIRE_MAGE.get(), level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        ItemStack book = this.getItemInHand(InteractionHand.MAIN_HAND);
        book.getOrCreateTag().putInt("color", 14);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.isFire()) return false;
        return super.hurt(pSource, pAmount);
    }

}
