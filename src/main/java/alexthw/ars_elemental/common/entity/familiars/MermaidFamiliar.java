package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.common.entity.MermaidEntity.Variants;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.compat.PatchouliHandler;
import com.hollingsworth.arsnouveau.common.entity.familiar.FlyingFamiliarEntity;
import com.hollingsworth.arsnouveau.common.entity.familiar.ISpellCastListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.PlayState;
import software.bernie.ars_nouveau.geckolib3.core.builder.AnimationBuilder;
import software.bernie.ars_nouveau.geckolib3.core.controller.AnimationController;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationData;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MermaidFamiliar extends FlyingFamiliarEntity implements ISpellCastListener, IVariantColorProvider<MermaidFamiliar> {
    public MermaidFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public MermaidFamiliar(Level level) {
        this(ModEntities.SIREN_FAMILIAR.get(), level);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND || pPlayer.getCommandSenderWorld().isClientSide)
            return InteractionResult.PASS;

        ItemStack stack = pPlayer.getItemInHand(hand);
        String color = Variants.getColorFromStack(stack);
        if (color != null && !getColor().equals(color)) {
            setColor(color);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(pPlayer, pVec, hand);
    }

    public void onModifier(SpellModifierEvent event) {
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster) && SpellSchools.ELEMENTAL_WATER.isPartOfSchool(event.spellPart)) {
            event.builder.addDamageModifier(2.0D);
        }
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.isInWater()) {
            if (this.level.getGameTime() % 60L == 0L && this.getOwner() != null) {
                this.getOwner().addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
                this.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(new AnimationController<>(this, "actionController", 10, this::actionPredicate));
    }

    public PlayState walkPredicate(AnimationEvent event) {
        if (isOnGround() && !isInWater() || (level.isClientSide && PatchouliHandler.isPatchouliWorld())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("ground"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("floating"));
        }
        return PlayState.CONTINUE;
    }

    private <T extends IAnimatable>  PlayState actionPredicate(AnimationEvent<T> event) {
        if (getDeltaMovement().length() > 0 || (level.isClientSide && PatchouliHandler.isPatchouliWorld())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        }
        return PlayState.CONTINUE;
    }


    public @NotNull EntityType<?> getType() {
        return ModEntities.SIREN_FAMILIAR.get();
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        PathNavigation newNav = super.createNavigation(world);
        newNav.setCanFloat(false);
        return newNav;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.set(COLOR, Variants.KELP.toString());
    }

    public String getColor(MermaidFamiliar mermaidFamiliar) {
        return this.entityData.get(COLOR);
    }

    @Override
    public ResourceLocation getTexture(MermaidFamiliar entity) {
        return prefix("textures/entity/mermaid_" + (getColor().isEmpty() ? Variants.KELP.toString() : getColor()) + ".png");
    }

}
