package alexthw.ars_elemental.common.entity.spells;

import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.block.IPrismaticBlock;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtract;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCurvedProjectile extends EntityProjectileSpell {


    private boolean isCareful = false;

    public EntityCurvedProjectile(EntityType<? extends EntityProjectileSpell> entityType, Level world) {
        super(entityType, world);
        isNoGravity = false;
    }

    public EntityCurvedProjectile(Level world, SpellResolver resolver) {
        super(ModEntities.CURVED_PROJECTILE.get(), world, resolver);
        this.isCareful = resolver.spell.getBuffsAtIndex(0, resolver.spellContext.getUnwrappedCaster(), AugmentExtract.INSTANCE) > 0;
        isNoGravity = false;
    }

    @Override
    public EntityType<?> getType() {
        return ModEntities.CURVED_PROJECTILE.get();
    }

    @Override
    public void tickNextPosition() {
        Vec3 motion = getDeltaMovement();
        setDeltaMovement(motion.x * 0.96, (motion.y > 0 ? motion.y * 0.96 : motion.y) - 0.03f, motion.z * 0.96);
        Vec3 pos = position();
        setPos(pos.x + motion.x, pos.y + motion.y, pos.z + motion.z);
    }

    @Override
    public void tick() {
        if (!level.isClientSide && this.age > getExpirationTime()) {
            this.onHit(new BlockHitResult(getNextHitPosition(), Direction.UP, blockPosition(), true));
        }
        super.tick();
    }

    @Override
    protected void attemptRemoval() {
        this.pierceLeft--;
        if (this.pierceLeft < 0) {
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.remove(RemovalReason.DISCARDED);
        } else {
            Vec3 vel = getDeltaMovement();
            setDeltaMovement(vel.x(), -0.9 * vel.y(), vel.z());
        }
    }

    private void attemptRemoval(BlockHitResult blockraytraceresult) {
        this.pierceLeft--;
        if (this.pierceLeft < 0) {
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.remove(RemovalReason.DISCARDED);
        } else {
            Direction direction = blockraytraceresult.getDirection();
            float factor = -0.9F;
            // bounce off the block according to the face hit and reduce momentum
            switch (direction) {
                case UP, DOWN -> {
                    Vec3 vel = getDeltaMovement();
                    setDeltaMovement(vel.x(), factor * vel.y(), vel.z());
                }
                case EAST, WEST -> {
                    Vec3 vel = getDeltaMovement();
                    setDeltaMovement(factor * vel.x(), vel.y(), vel.z());
                }
                case NORTH, SOUTH -> {
                    Vec3 vel = getDeltaMovement();
                    setDeltaMovement(vel.x(), vel.y(), factor * vel.z());
                }
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        result = transformHitResult(result);
        if (!level.isClientSide && result instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getEntity().equals(this.getOwner())) return;
            if (this.spellResolver != null) {
                this.spellResolver.onResolveEffect(level, result);
                Networking.sendToNearby(level, new BlockPos(result.getLocation()), new PacketANEffect(PacketANEffect.EffectType.BURST,
                        new BlockPos(result.getLocation()), getParticleColorWrapper()));
                attemptRemoval();
            }
        }

        if (!level.isClientSide && result instanceof BlockHitResult blockRaytraceResult && !this.isRemoved() && !hitList.contains(blockRaytraceResult.getBlockPos())) {

            BlockState state = level.getBlockState(blockRaytraceResult.getBlockPos());

            if (state.getBlock() instanceof IPrismaticBlock prismaticBlock) {
                prismaticBlock.onHit((ServerLevel) level, blockRaytraceResult.getBlockPos(), this);
                return;
            }

            if (state.getMaterial() == Material.PORTAL) {
                state.getBlock().entityInside(state, level, blockRaytraceResult.getBlockPos(), this);
                return;
            }

            if (this.spellResolver != null) {
                this.hitList.add(blockRaytraceResult.getBlockPos());
                if (!isCareful || pierceLeft == 0) this.spellResolver.onResolveEffect(this.level, blockRaytraceResult);
            }
            Networking.sendToNearby(level, ((BlockHitResult) result).getBlockPos(), new PacketANEffect(PacketANEffect.EffectType.BURST,
                    new BlockPos(result.getLocation()).below(), getParticleColorWrapper()));
            attemptRemoval(blockRaytraceResult);
        }
    }

}
