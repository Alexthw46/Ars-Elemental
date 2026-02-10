package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.common.glyphs.EffectWaterJet;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static alexthw.ars_elemental.common.blocks.mermaid_block.MermaidTile.shrineParticle;

public class EntityWaterJet extends Entity implements IEntityWithComplexSpawn {

    private static final int DELAY_TICKS = 25; // 1.25 Seconds delay
    // DATA STORAGE
    private List<Vec3> origins = new ArrayList<>();
    private List<Integer> targetIds = new ArrayList<>();
    private SpellStats stats;
    private SpellContext spellContext;
    private SpellResolver spellResolver;
    private float baseDamage;
    // LOGIC
    private int tickTimer = 0;

    public EntityWaterJet(EntityType<?> type, Level level) {
        super(type, level);
    }

    // Custom Constructor called by the Spell
    public EntityWaterJet(Level level, List<Vec3> origins, List<Integer> targetIds, float baseDamage, SpellStats stats, SpellContext spellContext, SpellResolver resolver) {
        this(ModEntities.WATER_JET_MARKER.get(), level);
        this.origins = origins;
        this.targetIds = targetIds;
        this.stats = stats;
        this.spellContext = spellContext;
        this.spellResolver = resolver;
        this.baseDamage = baseDamage;

        // Set position to the first jet for chunk loading relevance
        if (!origins.isEmpty()) {
            Vec3 first = origins.getFirst();
            this.setPos(first.x, first.y, first.z);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            // VISUALS: Charging Phase
            // Spawn bubbles at every stored origin point
            for (Vec3 pos : origins) {
                for (int i = 0; i < tickTimer / 4; i++) {
                    level.addParticle(
                            GlowParticleData.createData(shrineParticle, i / 2F * 0.2f, i / 2F * 0.75f, 20),
                            pos.x() + ParticleUtil.inRange(-0.1, 0.1),
                            pos.y() + ParticleUtil.inRange(-0.1, 0.1),
                            pos.z() + ParticleUtil.inRange(-0.1, 0.1),
                            0, 0, 0);
                }
            }
        } else {
            // LOGIC: Check Timer
            if (tickTimer >= DELAY_TICKS) {
                fire();
                this.discard(); // Destroy entity
            }
        }
        tickTimer++;
    }

    private void fire() {
        // Execute the stored data
        var owner = spellContext.getUnwrappedCaster();
        for (int i = 0; i < origins.size(); i++) {
            if (i >= targetIds.size()) break;

            Vec3 origin = origins.get(i);
            int targetId = targetIds.get(i);

            // Re-acquire the entity from the world using the ID
            Entity targetEntity = this.level().getEntity(targetId);

            if (targetEntity instanceof LivingEntity livingTarget && livingTarget.isAlive()) {
                // DEAL DAMAGE
                if (targetEntity.distanceToSqr(origin) > 144) {
                    if (targetEntity.level() instanceof ServerLevel sl)
                        sl.sendParticles(ParticleTypes.SPLASH, origin.x, origin.y, origin.z, 10, 0.5, 0.5, 0.5, 0.1);

                    continue;
                }
                EffectWaterJet.INSTANCE.attemptDamage(level(), owner, stats, spellContext, spellResolver, livingTarget, EffectWaterJet.INSTANCE.buildDamageSource(level(), owner), baseDamage);
                spawnBeam(origin, livingTarget.getEyePosition());
            }
        }
    }

    private void spawnBeam(Vec3 start, Vec3 end) {
        if (this.level() instanceof ServerLevel serverLevel) {
            Vec3 dir = end.subtract(start).normalize();
            double dist = start.distanceTo(end);
            for (double d = 0; d < dist; d += 0.25) {
                Vec3 p = start.add(dir.scale(d));
                serverLevel.sendParticles(ParticleTypes.FALLING_WATER, p.x, p.y, p.z, 2, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.tickTimer = tag.getInt("Timer");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Timer", tickTimer);
    }

    @Override
    public void writeSpawnData(@NotNull RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(origins.size());
        for (var pos : origins) {
            buffer.writeVec3(pos);
        }
    }

    @Override
    public void readSpawnData(@NotNull RegistryFriendlyByteBuf additionalData) {
        int size = additionalData.readInt();
        for (int i = 0; i < size; i++) {
            Vec3 pos = additionalData.readVec3();
            origins.add(pos);
        }
    }
}
