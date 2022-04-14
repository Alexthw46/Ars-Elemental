package alexthw.ars_elemental.common.entity.mages;

import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.common.spell.method.MethodOrbit;
import com.hollingsworth.arsnouveau.common.spell.method.MethodSelf;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AirMage extends EntityMageBase {


    /**
     * SelfSpells
     * 0: Leap towards the enemy
     * 1: To use after leap, launch and shear enemies
     * <p>
     * ProjSpells
     * 1: Bouncing Lightning
     */
    public AirMage(EntityType<? extends EntityMageBase> p_21368_, Level level) {
        super(p_21368_, level);
        this.school = SpellSchools.ELEMENTAL_AIR;
        sSpells.add(new Spell(MethodSelf.INSTANCE, EffectLeap.INSTANCE, AugmentAmplify.INSTANCE, EffectBounce.INSTANCE));
        sSpells.add(new Spell(MethodOrbit.INSTANCE, EffectLaunch.INSTANCE, EffectDelay.INSTANCE, EffectWindshear.INSTANCE, EffectGravity.INSTANCE, AugmentExtendTime.INSTANCE));
        pSpells.add(new Spell(MethodCurvedProjectile.INSTANCE, AugmentPierce.INSTANCE, AugmentPierce.INSTANCE, EffectLightning.INSTANCE));
    }

    public AirMage(Level level) {
        this(ModEntities.AIR_MAGE.get(), level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        ItemStack book = this.getItemInHand(InteractionHand.MAIN_HAND);
        book.getOrCreateTag().putInt("color", 4);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.isFall() || pSource == DamageSource.LIGHTNING_BOLT) return false;
        return super.hurt(pSource, pAmount);
    }
}
