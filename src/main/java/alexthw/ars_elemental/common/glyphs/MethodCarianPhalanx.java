package alexthw.ars_elemental.common.glyphs;

import alexthw.ars_elemental.common.entity.spells.EntityCarianPhalanx;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class MethodCarianPhalanx extends ElementalAbstractForm {

    public static final MethodCarianPhalanx INSTANCE = new MethodCarianPhalanx("carian_phalanx", "Carian Phalanx");
    // Map of player UUID to their active phalanx projectiles (only orbiting ones)
    public static final Map<UUID, List<EntityCarianPhalanx>> PLAYER_PHALANX_MAP = new HashMap<>();
    // Maximum number of phalanx projectiles per player
    private static final int MAX_PHALANX_PER_PLAYER = 5;

    public MethodCarianPhalanx(String tag, String description) {
        super(tag, description);
    }

    /**
     * Public method to unregister a phalanx when it's launched
     * Call this from CarianPhalanx when it transitions to launched state
     */
    public static void onPhalanxLaunched(EntityCarianPhalanx phalanx) {
        if (phalanx.getOwner() != null) {
            UUID ownerUUID = phalanx.getOwner().getUUID();
            List<EntityCarianPhalanx> phalanxList = PLAYER_PHALANX_MAP.get(ownerUUID);
            if (phalanxList != null) {
                phalanxList.remove(phalanx);
                if (phalanxList.isEmpty()) {
                    PLAYER_PHALANX_MAP.remove(ownerUUID);
                }
            }
        }
    }

    @Override
    public CastResolveType onCast(@Nullable ItemStack stack, LivingEntity caster, Level world, SpellStats spellStats, SpellContext context, SpellResolver resolver) {
        if (context.getRemainingSpell().isEmpty()) {
            return CastResolveType.FAILURE;
        }

        if (!world.isClientSide) {
            summonPhalanx(world, caster, spellStats, context, resolver);
        }

        return CastResolveType.SUCCESS;
    }

    @Override
    public CastResolveType onCastOnBlock(UseOnContext context, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (context.getPlayer() == null || spellContext.getRemainingSpell().isEmpty()) {
            return CastResolveType.FAILURE;
        }

        if (!context.getLevel().isClientSide) {
            summonPhalanx(context.getLevel(), context.getPlayer(), spellStats, spellContext, resolver);
        }

        return CastResolveType.SUCCESS;
    }

    @Override
    public CastResolveType onCastOnBlock(BlockHitResult blockRayTraceResult, LivingEntity caster, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (spellContext.getRemainingSpell().isEmpty()) {
            return CastResolveType.FAILURE;
        }

        if (!caster.level().isClientSide) {
            summonPhalanx(caster.level(), caster, spellStats, spellContext, resolver);
        }

        return CastResolveType.SUCCESS;
    }

    /**
     * Summons the Carian Phalanx projectiles around the caster
     */

    @Override
    public CastResolveType onCastOnEntity(@Nullable ItemStack stack, LivingEntity caster, Entity target, InteractionHand hand, SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (spellContext.getRemainingSpell().isEmpty()) {
            return CastResolveType.FAILURE;
        }

        if (!caster.level().isClientSide) {
            summonPhalanx(caster.level(), caster, spellStats, spellContext, resolver);
        }

        return CastResolveType.SUCCESS;
    }

    /**
     * Summons the Carian Phalanx projectiles around the caster
     */
    private void summonPhalanx(Level world, LivingEntity caster, SpellStats stats, SpellContext context, SpellResolver resolver) {
        UUID casterUUID = caster.getUUID();

        // Clean up any removed projectiles from the map
        cleanupRemovedPhalanx(casterUUID);

        // Calculate number of projectiles (base 3 + Split augments)
        int total = 1 + stats.getBuffCount(AugmentSplit.INSTANCE);

        // Get current phalanx count for this player
        List<EntityCarianPhalanx> existingPhalanx = PLAYER_PHALANX_MAP.getOrDefault(casterUUID, new ArrayList<>());

        // Check if we need to remove old projectiles
        int currentCount = existingPhalanx.size();
        int newTotal = currentCount + total;

        if (newTotal > MAX_PHALANX_PER_PLAYER) {
            // Remove the oldest projectiles to make room
            int toRemove = newTotal - MAX_PHALANX_PER_PLAYER;
            for (int i = 0; i < toRemove && !existingPhalanx.isEmpty(); i++) {
                EntityCarianPhalanx oldest = existingPhalanx.removeFirst();
                if (!oldest.isRemoved()) {
                    oldest.attemptRemoval();
                }
            }
            newTotal = MAX_PHALANX_PER_PLAYER;
        }

        boolean[] reserved = new boolean[MAX_PHALANX_PER_PLAYER];
        for (var phalanx : existingPhalanx) {
            reserved[phalanx.getIndex()] = true;
        }

        // Get the remaining spell to execute when projectiles hit
        Spell remainingSpell = context.getRemainingSpell();

        // Create a child context for the projectiles
        SpellContext newContext = resolver.spellContext.makeChildContext()
                .withSpell(remainingSpell);

        // Setup ignore predicates for targeting
        List<Predicate<LivingEntity>> ignorePredicates = new ArrayList<>();

        // Ignore the caster
        ignorePredicates.add(e -> e.equals(caster));

        // Ignore caster's allies if they're on a team
        if (caster.getTeam() != null) {
            ignorePredicates.add(e -> e.isAlliedTo(caster));
        }

        ignorePredicates.add(e -> e instanceof Animal);

        // Spawn each phalanx projectile
        List<EntityCarianPhalanx> newPhalanxList = new ArrayList<>();
        for (int i = 0; i < newTotal; i++) {
            if (reserved[i]) continue; // Loop over the full total to fill the gaps in the indexes
            EntityCarianPhalanx phalanx = new EntityCarianPhalanx(world, caster.getX(), caster.getEyeY(), caster.getZ());

            // Set the spell resolver for this projectile
            phalanx.setResolver(resolver.getNewResolver(newContext));

            // Set owner
            phalanx.setOwner(caster);

            // Configure formation position
            phalanx.setIndex(i);

            // Apply augments
            phalanx.setAccelerates((int) stats.getAccMultiplier());
            phalanx.setAoe((float) stats.getAoeMultiplier());
            phalanx.extendTimes = (int) stats.getDurationMultiplier();

            // Set ignore predicates for targeting
            phalanx.setIgnored(ignorePredicates);

            // Set pierce/sensitive if needed
            phalanx.pierceLeft = stats.getBuffCount(AugmentPierce.INSTANCE);
            phalanx.numSensitive = stats.getBuffCount(AugmentSensitive.INSTANCE);

            // Add to world
            world.addFreshEntity(phalanx);

            // Track this phalanx
            newPhalanxList.add(phalanx);
        }

        // Update the tracking map
        existingPhalanx.addAll(newPhalanxList);
        PLAYER_PHALANX_MAP.put(casterUUID, existingPhalanx);
    }

    /**
     * Removes any projectiles from the tracking map that have been removed from the world
     * or have been launched (no longer orbiting)
     */
    private void cleanupRemovedPhalanx(UUID playerUUID) {
        List<EntityCarianPhalanx> phalanxList = PLAYER_PHALANX_MAP.get(playerUUID);
        if (phalanxList != null) {
            // Remove projectiles that are gone or have been launched
            phalanxList.removeIf(p -> p.isRemoved() || p.isLaunched());

            // Clean up empty lists
            if (phalanxList.isEmpty()) {
                PLAYER_PHALANX_MAP.remove(playerUUID);
            }
        }
    }

    @Override
    protected int getDefaultManaCost() {
        return 175;
    }

    @Override
    public SpellTier defaultTier() {
        return SpellTier.THREE;
    }

    @Override
    public String getBookDescription() {
        return "Summons floating magical projectiles that orbit around you. When an enemy gets close, they automatically launch and home in on the target. " +
                "You can have up to " + MAX_PHALANX_PER_PLAYER + " projectiles orbiting at once - casting more will dismiss the oldest ones. " +
                "Additional projectiles, their speed, radius, duration, and homing capability can be augmented. ";
    }

    @Override
    public void addAugmentDescriptions(Map<AbstractAugment, String> map) {
        super.addAugmentDescriptions(map);
        map.put(AugmentSplit.INSTANCE, "Summons one additional phalanx projectile.");
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(
                AugmentSplit.INSTANCE,          // More projectiles
                AugmentAccelerate.INSTANCE,     // Faster orbit/homing
                AugmentDecelerate.INSTANCE,     // Slower orbit
                AugmentAOE.INSTANCE,            // Larger orbit radius
                AugmentExtendTime.INSTANCE,     // Longer duration
                AugmentDurationDown.INSTANCE,   // Shorter duration
                AugmentPierce.INSTANCE,         // Pierce through enemies
                AugmentSensitive.INSTANCE,      // Hit blocks
                AugmentAmplify.INSTANCE         // More damage when resolved
        );
    }
}