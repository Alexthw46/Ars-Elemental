package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.common.entity.ai.HijackTurretGoal;
import alexthw.ars_elemental.common.entity.ai.HoverAroundTargetGoal;
import alexthw.ars_elemental.common.entity.spells.FlashLightning;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.alexthw.sauce.api.item.ISchoolProvider;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.api.util.SummonUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.IAnimationListener;
import com.hollingsworth.arsnouveau.common.block.tile.RotatingTurretTile;
import com.hollingsworth.arsnouveau.common.entity.goal.GoBackHomeGoal;
import com.hollingsworth.arsnouveau.common.items.data.ICharmSerializable;
import com.hollingsworth.arsnouveau.common.items.data.PersistentFamiliarData;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketANEffect;
import com.hollingsworth.arsnouveau.common.network.PacketAnimEntity;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import com.hollingsworth.nuggets.client.overlay.IWorldTooltipProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static com.hollingsworth.nuggets.common.registry.RegistryHelper.getRegistryName;

public class FlashjackEntity extends Parrot implements GeoEntity, ICharmSerializable, IAnimationListener, IDispellable, ISchoolProvider, IWorldTooltipProvider, IWandable {
    public static final EntityDataAccessor<String> COLOR = SynchedEntityData.defineId(FlashjackEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Optional<BlockPos>> HOME = SynchedEntityData.defineId(FlashjackEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation idle = RawAnimation.begin().thenLoop("idle.air");
    public static final RawAnimation inactive = RawAnimation.begin().thenPlayAndHold("idle.ground");
    public static final RawAnimation flapping = RawAnimation.begin().thenLoop("idle.flapping");
    public static final RawAnimation attack = RawAnimation.begin().thenPlayXTimes("attack", 2).thenWait(40).thenLoop("idle.flapping");
    public static final EntityDataAccessor<Boolean> BEING_TAMED = SynchedEntityData.defineId(FlashjackEntity.class, EntityDataSerializers.BOOLEAN);

    List<BlockPos> turrets = new ArrayList<>();
    List<EntityType<?>> blacklist = new ArrayList<>();
    public int tamingTime;

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 36.0F).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.4F).add(Attributes.ATTACK_DAMAGE, 6.0F);
    }

    AnimationController<FlashjackEntity> actionController;

    public List<BlockPos> getTurrets() {
        return turrets;
    }

    public FlashjackEntity(EntityType<? extends Parrot> entityType, Level level) {
        super(entityType, level);
    }

    public FlashjackEntity(Level world) {
        super(ModEntities.FLASHJACK_ENTITY.get(), world);
    }

    List<EntityType<?>> getBlacklist() {
        return blacklist;
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

    @Override
    protected int getBaseExperienceReward() {
        return 0;
    }

    @Override
    public SpellSchool getSchool() {
        return SpellSchools.ELEMENTAL_AIR;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(5, new ParrotWanderGoal(this, 1.0F));
        this.goalSelector.addGoal(2, new HijackTurretGoal(this, 30));
        this.goalSelector.addGoal(3, new HoverAroundTargetGoal(this, 0.75, 3.0f, 6.0f));
        this.goalSelector.addGoal(2, new GoBackHomeGoal(this, this::getHome, 10, () -> this.getTarget() == null || this.getHome() != null && this.distanceToSqr(this.getHome().getCenter()) > 600) {
            @Override
            public void start() {
                // When going back home, clear the mob's target so it doesn't try to attack while flying back
                setTarget(null);
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, true, (entity) -> entity instanceof Enemy && entity.isAlive() && !getBlacklist().contains(entity.getType())));
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        return SummonUtil.canSummonTakeDamage(pSource) && super.hurt(pSource, pAmount);
    }

    private static PlayState attackPredicate(AnimationState<FlashjackEntity> event) {
        if (event.isCurrentAnimation(attack))
            return PlayState.CONTINUE;

        if (event.getAnimatable().entityData.get(BEING_TAMED) && !event.getAnimatable().isTamed())
            return event.setAndContinue(attack);

        // If the entity is on the ground, stop the animation
        if (event.getAnimatable().onGround())
            return PlayState.STOP;

        // If the entity is in the attack animation, continue it until the end
        // Defaults to flapping animation if in the air and not attacking
        return event.setAndContinue(flapping);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        if (!level.isClientSide && isTamed()) {
            ItemStack stack = new ItemStack(ModItems.FLASHJACK_CHARM);
            stack.set(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, createCharmData());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
        }
    }

    @Override
    public void tick() {
        super.tick();
        SummonUtil.healOverTime(this);

        // Add taming process logic
        if (!isTamed() && this.entityData.get(BEING_TAMED)) {
            tamingTime++;

            // Show particles every 20 ticks
            if (tamingTime % 20 == 0 && !level.isClientSide()) {
                Networking.sendToNearbyClient(level, this, new PacketANEffect(PacketANEffect.EffectType.TIMED_HELIX, blockPosition(), ParticleColor.YELLOW));
            }

            // After 60 ticks (3 seconds), drop shards and create lightning effect
            if (tamingTime > 60 && !level.isClientSide) {
                ItemStack stack = new ItemStack(ModItems.FLASHJACK_SHARDS.get(), 1 + level.random.nextInt(2));
                level.addFreshEntity(new ItemEntity(level, getX(), getY() + 0.5, getZ(), stack));

                // Spawn lightning effect
                var flash = new FlashLightning(this.level);
                flash.setPos(this.getX(), this.getY(), this.getZ());
                level.addFreshEntity(flash);

                this.remove(RemovalReason.DISCARDED);
                level.playSound(null, getX(), getY(), getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.NEUTRAL, 1f, 1f);
            }
        }
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (!player.level().isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (this.isOwnedBy(player)) {

                if (stack.is(Tags.Items.DYES_YELLOW) && !this.getColor().equals("flashjack")) {
                    this.setColor("flashjack");
                    stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }

                if (stack.is(Tags.Items.DYES_RED) && !this.getColor().equals("flapjack")) {
                    this.setColor("flapjack");
                    stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }

//                if (stack.is(Tags.Items.DYES_BLUE) && !this.getColor().equals("bluejay")) {
//                    this.setColor("bluejay");
//                    stack.shrink(1);
//                    return InteractionResult.SUCCESS;
//                }

                if (stack.getItem() == ModItems.FLASHING_POD.get().asItem()) {
                    var flash = new FlashLightning(this.level);
                    flash.setPos(this.getX(), this.getY(), this.getZ());
                    player.level().addFreshEntity(flash);
                    Networking.sendToNearbyClient(this.level, this, new PacketAnimEntity(this.getId(), 0));
                }

            } else if (stack.getItem() == ModItems.FLASHING_POD.get().asItem()) {
                entityData.set(BEING_TAMED, true);
                stack.shrink(1);
            }

            if (!this.isFlying() && this.isTame() && this.isOwnedBy(player) && stack.isEmpty()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public boolean isTamed() {
        return this.getOwner() != null;
    }

    public static boolean checkSpawnRules(EntityType<? extends Parrot> parrot, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    public boolean isActive() {
        return this.getTarget() != null;
    }

    @Override
    public void thunderHit(@NotNull ServerLevel level, @NotNull LightningBolt lightning) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "idle_controller", 5, event -> onGround() ? event.setAndContinue(inactive) : event.setAndContinue(idle)));
        actionController = new AnimationController<>(this, "action_controller", 5, FlashjackEntity::attackPredicate);
        data.add(actionController);
    }

    @Override
    public void startAnimation(int arg) {
        if (arg == 0 && actionController != null) {
            actionController.setAnimation(attack);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    public @Nullable BlockPos getHome() {
        return this.entityData.get(HOME).orElse(null);
    }

    public void setHome(BlockPos home) {
        this.entityData.set(HOME, Optional.of(home));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HOME, Optional.empty());
        builder.define(COLOR, "flashjack");
        builder.define(BEING_TAMED, false);
    }

    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if (this.isRemoved())
            return false;

        if (!level.isClientSide && isTamed()) {
            ItemStack stack = new ItemStack(ModItems.FLASHJACK_CHARM);
            stack.set(DataComponentRegistry.PERSISTENT_FAMILIAR_DATA, createCharmData());
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), stack));
            ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
            this.remove(RemovalReason.DISCARDED);
        }
        return this.isTamed();
    }

    @Override
    public void fromCharmData(PersistentFamiliarData data) {
        setColor(data.color());
        setCustomName(data.name());
    }

    public String getColor() {
        return this.entityData.get(COLOR);
    }

    public void setColor(String color) {
        this.entityData.set(COLOR, color);
    }

    public ResourceLocation getTexture() {
        return prefix("textures/entity/" + (getColor().isEmpty() ? "flashjack" : getColor()) + ".png");
    }

    @Override
    public void getTooltip(List<Component> tooltip) {
        if (getHome() != null) {
            String home = getHome().getX() + ", " + getHome().getY() + ", " + getHome().getZ();
            tooltip.add(Component.translatable("ars_nouveau.weald_walker.home", home));
        } else {
            tooltip.add(Component.translatable("ars_nouveau.weald_walker.home", Component.translatable("ars_nouveau.nothing").getString()));
        }
    }


    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("color", this.entityData.get(COLOR));
        NBTUtil.storeBlockPos(compound, "home", getHome());

        int counter = 0;
        for (BlockPos pos : this.turrets) {
            NBTUtil.storeBlockPos(compound, "turret_" + counter, pos);
            counter++;
        }
        counter = 0;
        for (EntityType<?> type : this.blacklist) {
            compound.putString("blacklist_" + counter, getRegistryName(type).toString());
            counter++;
        }
        if (!this.isTamed()) {
            compound.putInt("taming", tamingTime);
            compound.putBoolean("beingTamed", this.entityData.get(BEING_TAMED));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(COLOR, compound.getString("color"));
        if (NBTUtil.hasBlockPos(compound, "home")) {
            setHome(NBTUtil.getNullablePos(compound, "home"));
        }
        this.turrets.clear();
        this.blacklist.clear();
        int counter = 0;
        while (NBTUtil.hasBlockPos(compound, "turret_" + counter)) {
            BlockPos pos = NBTUtil.getNullablePos(compound, "turret_" + counter);
            if (pos != null) {
                this.turrets.add(pos);
            }
            counter++;
        }
        counter = 0;
        while (compound.contains("blacklist_" + counter)) {
            EntityType.byString(compound.getString("blacklist_" + counter)).
                    ifPresent(type -> this.blacklist.add(type));
            counter++;
        }
        if (!this.isTamed()) {
            this.tamingTime = compound.getInt("taming");
            this.entityData.set(BEING_TAMED, compound.getBoolean("beingTamed"));
        }
    }

    @Override
    public Result onFirstConnection(@Nullable GlobalPos storedPos, @Nullable Direction face, @Nullable LivingEntity
            storedEntity, Player playerEntity) {
        if (storedPos != null && playerEntity.level().getBlockEntity(storedPos.pos()) instanceof RotatingTurretTile) {
            this.turrets.add(storedPos.pos());
            // result msg
            playerEntity.sendSystemMessage(Component.translatable("ars_elemental.flashjack.connect"));
        }

        return IWandable.super.onFirstConnection(storedPos, face, storedEntity, playerEntity);
    }


    @Override
    public Result onLastConnection(@Nullable GlobalPos storedPos, @Nullable Direction face, @Nullable LivingEntity
            storedEntity, Player playerEntity) {

        if (storedEntity != null) {
            if (blacklist.contains(storedEntity.getType())) {
                blacklist.remove(storedEntity.getType());
                playerEntity.sendSystemMessage(Component.translatable("ars_elemental.flashjack.deny.remove"));
            } else {
                blacklist.add(storedEntity.getType());
                playerEntity.sendSystemMessage(Component.translatable("ars_elemental.flashjack.deny"));
            }
        } else if (storedPos != null && !(playerEntity.level().getBlockEntity(storedPos.pos()) instanceof RotatingTurretTile)) {
            setHome(storedPos.pos());
            PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.home_set"));
        }

        return IWandable.super.onLastConnection(storedPos, face, storedEntity, playerEntity);
    }

}
