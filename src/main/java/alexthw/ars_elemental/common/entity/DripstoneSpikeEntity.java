package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.common.glyphs.EffectSpike;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.PlayState;
import software.bernie.ars_nouveau.geckolib3.core.builder.AnimationBuilder;
import software.bernie.ars_nouveau.geckolib3.core.controller.AnimationController;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationData;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationFactory;
import software.bernie.ars_nouveau.geckolib3.resource.GeckoLibCache;
import software.bernie.ars_nouveau.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public class DripstoneSpikeEntity extends Entity implements IAnimatable, IEntityAdditionalSpawnData {

    private LivingEntity caster;
    private UUID casterUUID;
    private int lifeTicks = 20;

    float damage;
    SpellStats stats;
    SpellContext context;
    SpellResolver resolver;

    public double getPierce() {
        return pierce;
    }

    public double getAoe() {
        return aoe;
    }

    double pierce, aoe = 1;


    public DripstoneSpikeEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DripstoneSpikeEntity(Level worldIn, double x, double y, double z, float damage, LivingEntity casterIn, SpellStats spellStats, SpellContext context, SpellResolver resolver) {
        this(worldIn, x, y, z, damage, casterIn, spellStats, context, resolver, ModEntities.DRIPSTONE_SPIKE.get());
    }

    public DripstoneSpikeEntity(Level worldIn, double x, double y, double z, float damage, LivingEntity casterIn, SpellStats spellStats, SpellContext context, SpellResolver resolver, EntityType<?> type) {
        this(type, worldIn);
        this.setOwner(casterIn);
        this.setPos(x, y + 0.5, z);
        this.damage = damage;
        this.stats = spellStats;
        this.context = context;
        this.resolver = resolver;
        this.pierce = 1 + spellStats.getBuffCount(AugmentPierce.INSTANCE) * 0.25;
        this.aoe = 1 + (spellStats.getAoeMultiplier() - 1) * 0.25;
        this.lifeTicks += (int) (5 * spellStats.getDurationMultiplier());
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.DRIPSTONE_SPIKE.get();
    }

    public void damage(LivingEntity entity) {
        EffectSpike.INSTANCE.attemptDamage(entity.level, caster, stats, context, resolver, entity, new EntityDamageSource(DamageSource.STALAGMITE.getMsgId(), caster), damage);
    }


    @Override
    public boolean isAlliedTo(@NotNull Entity pEntity) {
        if (this.getOwner() != null) {
            return pEntity == this.getOwner() || this.getOwner().isAlliedTo(pEntity);
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    protected void defineSynchedData() {

    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.caster = pOwner;
        this.casterUUID = pOwner == null ? null : pOwner.getUUID();
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide() && lifeTicks < (16 + stats.getDurationMultiplier() * 5) && lifeTicks % 5 == 0 ) {
            for (Entity entity : this.level.getEntities(this, this.getBoundingBox(), (e) -> e instanceof LivingEntity)) {
                if (entity instanceof LivingEntity target) {
                    damage(target);
                }
            }
        }
        if (--this.lifeTicks < 0) {
            this.remove(RemovalReason.DISCARDED);
        }
    }


    @Nullable
    public LivingEntity getOwner() {
        if (this.caster == null && this.casterUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level).getEntity(this.casterUUID);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity) entity;
            }
        }
        return this.caster;
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        //this.warmupDelayTicks = compound.getInt("Warmup");
        if (compound.hasUUID("OwnerUUID")) {
            this.casterUUID = compound.getUUID("OwnerUUID");
        }
        pierce = compound.getDouble("pierce");
        aoe = compound.getDouble("aoe");

    }

    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        //compound.putInt("Warmup", this.warmupDelayTicks);
        if (this.casterUUID != null) {
            compound.putUUID("OwnerUUID", this.casterUUID);
        }
        compound.putDouble("pierce", pierce);
        compound.putDouble("aoe", aoe);
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::sprout));
        animationData.addAnimationController(new AnimationController<>(this, "size", 0, this::size));
    }

    private PlayState size(AnimationEvent<DripstoneSpikeEntity> dripstoneSpikeEntityAnimationEvent) {
        GeckoLibCache.getInstance().parser.setValue("aoe", this::getAoe);
        GeckoLibCache.getInstance().parser.setValue("pierce", this::getPierce);
        dripstoneSpikeEntityAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("adjust_size"));
        return PlayState.CONTINUE;
    }

    private PlayState sprout(AnimationEvent<DripstoneSpikeEntity> dripstoneSpikeEntityAnimationEvent) {
        dripstoneSpikeEntityAnimationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("sprout"));
        return PlayState.CONTINUE;
    }

    public final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        var base = super.getDimensions(pPose);
        return new EntityDimensions((float) (base.width * aoe), (float) (base.height * (aoe + pierce - 0.5)), false);
    }

    /**
     * Called by the server when constructing the spawn packet.
     * Data should be added to the provided stream.
     *
     * @param buffer The packet data stream
     */
    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeDouble(pierce);
        buffer.writeDouble(aoe);
    }

    /**
     * Called by the client when it receives an Entity spawn packet.
     * Data should be read out of the stream in the same way as it was written.
     *
     * @param additionalData The packet data stream
     */
    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        pierce = additionalData.readDouble();
        aoe = additionalData.readDouble();
    }

}
