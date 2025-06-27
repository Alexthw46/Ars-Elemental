package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.ArsNouveauRegistry;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.particle.ParticleEmitter;
import com.hollingsworth.arsnouveau.api.particle.PropertyParticleOptions;
import com.hollingsworth.arsnouveau.api.particle.configurations.properties.WallProperty;
import com.hollingsworth.arsnouveau.api.particle.timelines.LingerTimeline;
import com.hollingsworth.arsnouveau.api.particle.timelines.TimelineEntryData;
import com.hollingsworth.arsnouveau.api.particle.timelines.TimelineMap;
import com.hollingsworth.arsnouveau.api.registry.ParticlePropertyRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.client.ClientInfo;
import com.hollingsworth.arsnouveau.common.entity.EntityLingeringSpell;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class EntityMagnetSpell extends EntityProjectileSpell {

    List<Predicate<Entity>> ignored;
    public static final EntityDataAccessor<Float> DURATION_UP = SynchedEntityData.defineId(EntityMagnetSpell.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> AOE = SynchedEntityData.defineId(EntityMagnetSpell.class, EntityDataSerializers.FLOAT);

    LivingEntity tracked;
    public EntityMagnetSpell(EntityType<? extends EntityProjectileSpell> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityMagnetSpell(Level worldIn) {
        super(ModEntities.LINGER_MAGNET.get(), worldIn);
    }

    static public EntityMagnetSpell createMagnet(Level world, LivingEntity shooter, SpellStats spellStats, SpellResolver spellResolver, Vec3 location) {
        EntityMagnetSpell magnet = new EntityMagnetSpell(world);
        SpellContext spellContext = spellResolver.spellContext;
        magnet.ignored = makeIgnores(shooter, spellContext.getSpell(), spellContext.getCurrentIndex() + 1);
        magnet.setPos(location);
        magnet.setAoe((float) spellStats.getAoeMultiplier());
        magnet.setOwner(shooter);
        magnet.setExtendedTime(spellStats.getDurationMultiplier());
        magnet.setResolver(spellResolver);
        return magnet;
    }

    @Override
    public @NotNull EntityType<?> getType() {
        return ModEntities.LINGER_MAGNET.get();
    }

    public float getAoe() {
        return 3 + entityData.get(AOE);
    }

    public void setAoe(float aoe) {
        entityData.set(AOE, aoe);
    }

    @Override
    public void tickNextPosition() {
        if (this.tracked != null) {
            this.setPos(tracked.getX(), tracked.getY(), tracked.getZ());
        }
    }

    public float getExtendedTime() {
        return this.entityData.get(DURATION_UP);
    }

    private void setExtendedTime(double durationMultiplier) {
        this.entityData.set(DURATION_UP, (float) durationMultiplier);
    }

    @Override
    public int getExpirationTime() {
        return 70 + (int) (getExtendedTime() * 200);
    }

    @Override
    public void tick() {
        super.tick();
        // Magnetize entities
        if (!level().isClientSide() && this.age % 5 == 0) {
            for (Entity entity : level().getEntities(this, new AABB(this.blockPosition()).inflate(getAoe()))) {
                if (testFilters(entity)) continue;
                Vec3 vec3d = new Vec3(this.getX() - entity.getX(), this.getY() - entity.getY(), this.getZ() - entity.getZ());
                if (vec3d.length() < 1) continue;
                entity.setDeltaMovement(entity.getDeltaMovement().add(vec3d.normalize()).scale(0.5F));
                entity.hurtMarked = true;
            }
        }
    }

    @Override
    public void buildEmitters() {
        TimelineMap timelineMap = this.resolver().spell.particleTimeline();
        LingerTimeline projectileTimeline = timelineMap.get(ArsNouveauRegistry.GRAVITY_TIMELINE.get());
        TimelineEntryData trailConfig = projectileTimeline.trailEffect;
        TimelineEntryData resolveConfig = projectileTimeline.onResolvingEffect;
        this.tickEmitter = new ParticleEmitter(() -> this.getPosition(ClientInfo.partialTicks), this::getRotationVector, trailConfig);
        this.resolveEmitter = new ParticleEmitter(() -> this.getPosition(ClientInfo.partialTicks), this::getRotationVector, resolveConfig);
        if (this.tickEmitter.particleOptions instanceof PropertyParticleOptions propertyParticleOptions) {
            propertyParticleOptions.map.set(ParticlePropertyRegistry.WALL_PROPERTY.get(), new WallProperty(Math.round(getAoe()), 5, 20, getDirection()));
        }
        this.resolveSound = projectileTimeline.resolveSound.sound;
    }

    public boolean testFilters(Entity entity) {
        return ignored.stream().anyMatch(filter -> entity == this.tracked || filter.test(entity));
    }

    public static List<Predicate<Entity>> makeIgnores(LivingEntity shooter, Spell spell, int index) {
        List<Predicate<Entity>> ignore = new ArrayList<>();
        // prevent magnet from pulling itself and other lingering spells and familiars and entities that are ignored by filters
        ignore.add((entity -> entity instanceof EntityLingeringSpell));
        ignore.add((entity -> entity == shooter));
        ignore.add(entity -> entity instanceof FamiliarEntity);
        ignore.add(shooter::isAlliedTo);
        Set<IFilter> filters = GlyphEffectUtil.getFilters(spell.unsafeList(), index);
        if (!filters.isEmpty()) {
            ignore.add(entity -> GlyphEffectUtil.checkIgnoreFilters(entity, filters));
        }
        return ignore;
    }

    public void setTracked(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            this.tracked = livingEntity;
        }
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DURATION_UP, 0.0F);
        pBuilder.define(AOE, 0f);
    }

    @Override
    public void traceAnyHit(@Nullable HitResult raytraceresult, Vec3 thisPosition, Vec3 nextPosition) {
    }

    @Override
    public int getParticleDelay() {
        return 0;
    }
}
