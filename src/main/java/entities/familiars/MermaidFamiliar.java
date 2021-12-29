package entities.familiars;

import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.entity.ModEntities;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.entity.familiar.ISpellCastListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MermaidFamiliar extends FamiliarEntity implements ISpellCastListener {
    public MermaidFamiliar(EntityType<? extends PathfinderMob> p_i48575_1_, Level p_i48575_2_) {
        super(p_i48575_1_, p_i48575_2_);
    }

    public void onModifier(SpellModifierEvent event) {
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster) && SpellSchools.ELEMENTAL_WATER.isPartOfSchool(event.spellPart)) {
            event.builder.addDamageModifier(2.0D);
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.isInWater()) {
            if (this.level.getGameTime() % 60L == 0L && this.getOwner() != null) {
                this.getOwner().addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
                this.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
            }
        }
    }

    public @NotNull EntityType<?> getType() {
        return ModRegistry.SIREN_FAMILIAR.get();
    }

}
