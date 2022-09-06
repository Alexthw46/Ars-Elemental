package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.network.RayEffectPacket;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;

import static alexthw.ars_elemental.registry.ModPotions.LIGHTNING_LURE;

public class EffectDischarge extends ElementalAbstractEffect {

    public static EffectDischarge INSTANCE = new EffectDischarge();

    public EffectDischarge() {
        super("discharge", "Discharge");
    }

    @Override
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!(rayTraceResult.getEntity() instanceof LivingEntity livingEntity) || !(world instanceof ServerLevel level))
            return;
        Vec3 vec = safelyGetHitPos(rayTraceResult);
        float damage = (float) (DAMAGE.get() + AMP_VALUE.get() * spellStats.getAmpMultiplier());
        double range = 5 + spellStats.getAoeMultiplier();
        DamageSource source = buildDamageSource(world, shooter);

        if (livingEntity.hasEffect(ModPotions.SHOCKED_EFFECT.get()) || livingEntity.hasEffect(LIGHTNING_LURE.get())) {
            if (livingEntity.hasEffect(LIGHTNING_LURE.get())) {
                damage *= 1.5;
                livingEntity.removeEffect(LIGHTNING_LURE.get());
            }
            for (ItemStack i : livingEntity.getArmorSlots()) {
                IEnergyStorage energyStorage = i.getCapability(ForgeCapabilities.ENERGY).orElse(null);
                if (energyStorage != null) {
                    energyStorage.extractEnergy((int) spellStats.getAmpMultiplier() * 500, false);
                    damage *= 1.1;
                }
            }
            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(livingEntity.blockPosition()).inflate(range), (e) -> !e.equals(shooter))) {
                dealDamage(world, shooter, damage, spellStats, entity, source);
                applyConfigPotion(entity, ModPotions.SHOCKED_EFFECT.get(), spellStats);
                RayEffectPacket.send(world, new ParticleColor(225, 200, 50), livingEntity.position(), entity.position());
            }
        }
    }

    @Override
    public DamageSource buildDamageSource(Level world, LivingEntity shooter) {
        shooter = !(shooter instanceof Player) ? ANFakePlayer.getPlayer((ServerLevel) world) : shooter;
        return new EntityDamageSource(DamageSource.LIGHTNING_BOLT.getMsgId(), shooter);
    }

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        addDamageConfig(builder, 7.0);
        addAmpConfig(builder, 3.0);
    }

    @Override
    public int getDefaultManaCost() {
        return 40;
    }

    @Override
    public SpellTier getTier() {
        return SpellTier.TWO;
    }

    @Nonnull
    @Override
    public Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE,
                AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE,
                AugmentAOE.INSTANCE,
                AugmentFortune.INSTANCE
        );
    }

    @NotNull
    @Override
    protected Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.ELEMENTAL_AIR);
    }

}
