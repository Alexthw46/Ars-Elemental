package alexthw.ars_elemental.common.entity;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.glyphs.MethodHomingProjectile;
import com.hollingsworth.arsnouveau.api.entity.IDispellable;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.entity.WealdWalker;
import com.hollingsworth.arsnouveau.common.entity.goal.GoBackHomeGoal;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFlare;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Optional;

public class FirenandoEntity extends PathfinderMob implements RangedAttackMob, IAnimatable, IWandable, IDispellable {
    public FirenandoEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    public FirenandoEntity(Level world){
        super(ModRegistry.FIRENANDO_ENTITY.get(),world);
    }

    private int castCooldown = 0;
    private final ParticleColor color = new ParticleColor(250, 15, 15);
    public final Spell spell = new Spell(MethodHomingProjectile.INSTANCE, EffectIgnite.INSTANCE, AugmentSensitive.INSTANCE, EffectFlare.INSTANCE);

    @Override
    public void tick() {
        super.tick();
        if (castCooldown > 0) {
            this.castCooldown--;
        }
        if(!level.isClientSide() && level.getGameTime() % 20 == 0 && !this.isDeadOrDying()){
            this.heal(1.0f);
        }
    }

    @Override
    public void die(DamageSource source) {
        if(!level.isClientSide){
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(ModRegistry.FIRENANDO_CHARM.get())));
        }
        super.die(source);
    }

    @Override
    public boolean onDispel(@Nullable LivingEntity caster) {
        if (this.isRemoved() || level.isClientSide) return false;

        level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(ModRegistry.FIRENANDO_CHARM.get())));
        ParticleUtil.spawnPoof((ServerLevel) level, blockPosition());
        this.remove(RemovalReason.DISCARDED);

        return true;
    }

    @Override
    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
        EntitySpellResolver resolver = new EntitySpellResolver(new SpellContext(spell, this).withColors(color.toWrapper()));
        EntityHomingProjectile projectileSpell = new EntityHomingProjectile(level, this, resolver);
        projectileSpell.setColor(color.toWrapper());
        projectileSpell.shoot(this, this.getXRot(), this.getYRot(), 0.0F, 0.1f, 0.8f);
        level.addFreshEntity(projectileSpell);
        this.castCooldown = 20;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new GoBackHomeGoal(this, this::getHome, 10, () -> this.getTarget() == null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, false, (entity) -> entity instanceof Enemy));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new CastSpellGoal(this, 0.8d, 20,36f, () -> castCooldown <= 0, WealdWalker.Animations.CAST.ordinal(), 10));
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

    @Override
    protected int getExperienceReward(Player p_70693_1_) {
        return 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2d)
                .add(Attributes.FOLLOW_RANGE, 16D);
    }

    public static final EntityDataAccessor<Optional<BlockPos>> HOME = SynchedEntityData.defineId(FirenandoEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    public static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(FirenandoEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public void onFinishedConnectionFirst(@Nullable BlockPos storedPos, @Nullable LivingEntity storedEntity, Player playerEntity) {
        if(storedPos != null){
            setHome(storedPos);
            PortUtil.sendMessage(playerEntity, new TranslatableComponent("ars_nouveau.home_set"));
        }
    }

    public void setHome(BlockPos home){
        this.entityData.set(HOME, Optional.of(home));
    }

    public @Nullable
    BlockPos getHome(){
        return this.entityData.get(HOME).orElse(null);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOTING, false);
        this.entityData.define(HOME, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        NBTUtil.storeBlockPos(tag, "home", getHome());
        tag.putInt("cast", castCooldown);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source == DamageSource.CACTUS || source == DamageSource.SWEET_BERRY_BUSH || source == DamageSource.DROWN)
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(NBTUtil.hasBlockPos(tag, "home")){
            setHome(NBTUtil.getBlockPos(tag, "home"));
        }
        this.castCooldown = tag.getInt("cast");
    }

    //TODO GeckoStuff
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this,"idle_controller", 0, this::idlePredicate));
        data.addAnimationController(new AnimationController<>(this,"attack_controller", 5f, this::attackPredicate));
    }

    <T extends IAnimatable> PlayState attackPredicate(AnimationEvent<T> event){
        if (entityData.get(SHOOTING)){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("shoot",true));
        }else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        }
        return PlayState.CONTINUE;
    }

    <T extends IAnimatable> PlayState idlePredicate(AnimationEvent<T> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle.body"));
        return PlayState.CONTINUE;
    }

    AnimationFactory factory = new AnimationFactory(this);
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
