package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.mixin.ZombieInvoker;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EffectWaterGrave extends AbstractEffect {

    @Override
    public boolean isRenderAsIcon() {
        return false;
    }

    public static EffectWaterGrave INSTANCE = new EffectWaterGrave();

    private EffectWaterGrave() {
        super("watery_grave", "Watery Grave");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (rayTraceResult.getEntity() instanceof LivingEntity living) {
            if (spellStats.hasBuff(AugmentExtendTime.INSTANCE)) {
                applyConfigPotion(living, ModRegistry.WATER_GRAVE.get(), spellStats);
            } else {
                Vec3 delta = living.getDeltaMovement();
                double dy = Math.min(-1.0D, delta.y - 0.05D);
                living.setDeltaMovement(delta.x, dy, delta.z);
            }
            if (living instanceof Zombie zombie && !(living instanceof Drowned)) {
                ((ZombieInvoker) zombie).callStartUnderWaterConversion(20);
                return;
            }
            int airSupply = living.getAirSupply();
            if (airSupply <= 0 || living.getMobType() == MobType.WATER) {
                double damage = DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier();
                dealDamage(world,shooter, (float) damage, spellStats, living, shooter != null ? new EntityDamageSource(DamageSource.DROWN.getMsgId(),shooter) : DamageSource.DROWN);
            } else {
                double newSupply = Math.max(-19, airSupply - 50 * (3 + spellStats.getAmpMultiplier()));
                living.setAirSupply((int) newSupply);
            }
            living.hurtMarked = true;
        }
    }

    @Override
    public int getDefaultManaCost() {
        return 25;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addPotionConfig(builder, 30);
        addDamageConfig(builder, 5.0);
        addAmpConfig(builder, 2.0);
        addExtendTimeConfig(builder, 5);
    }

    /**
     * Returns the set of augments that this spell part can be enhanced by.
     */
    @NotNull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return getPotionAugments();
    }

    @Override
    public String getBookDescription() {
        return "Causes entities to drown. When augmented with Extend Time, they will be dragged down and unable to swim up.";
    }

}
