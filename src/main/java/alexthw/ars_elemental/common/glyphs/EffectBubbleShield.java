package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.registry.ModPotions;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectBubbleShield extends ElementalAbstractEffect implements IPotionEffect {

    public static EffectBubbleShield INSTANCE = new EffectBubbleShield();

    public EffectBubbleShield() {
        super("bubble_shield", "Bubble Shield");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {

        if (rayTraceResult.getEntity() instanceof LivingEntity livingEntity) {
            CapabilityRegistry.getMana(livingEntity).ifPresent(mana -> ((IPotionEffect) this).applyConfigPotion(livingEntity, ModPotions.MANA_BUBBLE.get(), spellStats));
            if (ISchoolFocus.hasFocus(world, shooter) == SpellSchools.ELEMENTAL_WATER) {
                if (livingEntity.hasEffect(ModPotions.HELLFIRE.get())) {
                    livingEntity.removeEffect(ModPotions.HELLFIRE.get());
                }
            }
        }

    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.TWO;
    }

    @Override
    public int getDefaultManaCost() {
        return 300;
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


    @Override
    public int getBaseDuration() {
        return POTION_TIME == null ? 30 : POTION_TIME.get();
    }

    @Override
    public int getExtendTimeDuration() {
        return EXTEND_TIME == null ? 8 : EXTEND_TIME.get();
    }

}
