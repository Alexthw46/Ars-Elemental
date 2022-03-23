package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.entity.summon.IUndeadSummon;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.util.TooManyCompats;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import io.github.derringersmods.toomanyglyphs.common.glyphs.AbstractEffectFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class EntityHomingProjectile extends EntityProjectileSpell {

    Player owner;
    Entity ignore;
    LivingEntity target;
    Set<AbstractEffectFilter> filters;
    boolean targetPlayers = false;

    public EntityHomingProjectile(Level world, Player shooter, SpellResolver resolver, boolean targetPlayers) {
        this(world,shooter,resolver);
        this.targetPlayers = targetPlayers;
        filters = TooManyCompats.getFilters(resolver.spell.recipe);
    }

    @Override
    public int getExpirationTime() {
        return 20 * 30;
    }

    public EntityHomingProjectile(Level worldIn, Player owner, LivingEntity ignore, SpellResolver resolver) {
        super(ModRegistry.HOMING_PROJECTILE.get(), worldIn, resolver);
        this.owner = owner;
        this.ignore = ignore;
    }

    public EntityHomingProjectile(Level world, SpellResolver resolver) {
        super(ModRegistry.HOMING_PROJECTILE.get(), world, resolver);

    }

    public EntityHomingProjectile(Level world, Player shooter, SpellResolver resolver) {
        this(world,resolver);
        this.owner = shooter;
    }

    public EntityHomingProjectile(EntityType<EntityHomingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tickNextPosition() {
        if (!this.isRemoved()) {

            if ((target != null) && (!target.isAlive() || (target.distanceToSqr(this) > 50))) target = null;

            if (target == null && tickCount % 5 == 0) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(4), this::shouldTarget);

                //update target or keep going
                if (entities.isEmpty() && target == null) {
                    super.tickNextPosition();
                } else if (!entities.isEmpty()) {
                    target = entities.stream().filter(e -> e.distanceToSqr(this) < 50).min(Comparator.comparingDouble(e -> e.distanceToSqr(this))).orElse(target);
                }
            }

            if (target != null){
                homeTo(target.blockPosition());
            }else{
                super.tickNextPosition();
            }

        }
    }

    private boolean shouldTarget(LivingEntity e) {

        boolean flag = !(e instanceof FirenandoEntity || e instanceof IUndeadSummon);

        if (e instanceof Player) flag &= targetPlayers;
        if (CompatUtils.tooManyGlyphsLoaded()) flag &= TooManyCompats.checkFilters(e, this.filters);

        return flag && e != owner && e != ignore && e.isAlive();
    }

    @Override
    protected void attemptRemoval() {
        this.pierceLeft--;
        if(this.pierceLeft < 0){
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove(RemovalReason.DISCARDED);
        }else if (getHitResult().getType() == HitResult.Type.ENTITY){

            Vec3 vel = getDeltaMovement();
            this.target = null;
        }
    }

    @Override
    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
    {
        float f = -Mth.sin(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, 0f, inaccuracy);
        Vec3 vec3d = entityThrower.getLookAngle();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3d.x, vec3d.y, vec3d.z).scale(velocity));
    }


    private void homeTo(BlockPos dest) {

        double posX = getX();
        double posY = getY();
        double posZ = getZ();
        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (dest.getX() != 0 || dest.getY() != 0 || dest.getZ() != 0){
            double targetX = dest.getX()+0.5;
            double targetY = dest.getY()+0.75;
            double targetZ = dest.getZ()+0.5;
            Vec3 targetVector = new Vec3(targetX-posX,targetY-posY,targetZ-posZ);
            double length = targetVector.length();
            targetVector = targetVector.scale(0.3/length);
            double weight  = 0;
            if (length <= 3){
                weight = (3.0 - length) * 0.3;
            }

            motionX = (0.9-weight)*motionX+(0.1+weight)*targetVector.x;
            motionY = (0.9-weight)*motionY+(0.1+weight)*targetVector.y;
            motionZ = (0.9-weight)*motionZ+(0.1+weight)*targetVector.z;
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        this.setPos(posX, posY, posZ);

        this.setDeltaMovement(motionX, motionY, motionZ);
    }

    @Override
    public EntityType<?> getType() {
        return ModRegistry.HOMING_PROJECTILE.get();
    }

    @Override
    public boolean save(CompoundTag pCompound) {
        if (owner != null) {
            pCompound.putUUID("owner", owner.getUUID());
        }
        if (ignore != null) {
            pCompound.putInt("ignore", ignore.getId());
        }
        pCompound.putBoolean("aimPlayers", targetPlayers);
        return super.save(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.targetPlayers = tag.getBoolean("aimPlayers");
        this.owner = tag.hasUUID("owner") ? getCommandSenderWorld().getPlayerByUUID(tag.getUUID("owner")) : null;
        this.ignore = getCommandSenderWorld().getEntity(tag.getInt("ignore"));
    }

    @Override
    protected void onHit(HitResult result) {

        if (!level.isClientSide && result instanceof BlockHitResult blockraytraceresult && !this.isRemoved() && !hitList.contains(((BlockHitResult) result).getBlockPos())) {

            BlockState state = level.getBlockState(blockraytraceresult.getBlockPos());
            if (state.getBlock() == Blocks.NETHER_PORTAL || state.getBlock() == Blocks.END_PORTAL) return;
        }

        super.onHit(result);
    }
}