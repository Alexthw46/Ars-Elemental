package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.common.entity.MermaidEntity;
import alexthw.ars_elemental.common.entity.MermaidEntity.Variants;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.compat.PatchouliHandler;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
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
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Locale;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class MermaidFamiliar extends FlyingFamiliarEntity implements ISpellCastListener, IVariantColorProvider<FamiliarEntity> {
    public MermaidFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public MermaidFamiliar(Level level) {
        this(ModEntities.SIREN_FAMILIAR.get(), level);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public @NotNull InteractionResult interactAt(@NotNull Player pPlayer, @NotNull Vec3 pVec, @NotNull InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND || pPlayer.getCommandSenderWorld().isClientSide)
            return InteractionResult.PASS;

        // if interacting with a dye, change the color of the mermaid
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
        // if the mermaid is alive and the owner is the player who cast the spell, and the spell is a water spell, increase the damage of the spell by 2
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster) && SpellSchools.ELEMENTAL_WATER.isPartOfSchool(event.spellPart)) {
            event.builder.addDamageModifier(2.0D);
        }
    }

    public void tick() {
        super.tick();
        // if the mermaid is in water, give the player and the mermaid the dolphin's grace effect
        if (!this.level().isClientSide && this.isInWater()) {
            if (this.level().getGameTime() % 60L == 0L && this.getOwner() != null) {
                this.getOwner().addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
                this.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 600, 1, false, false, true));
            }
        }
        // if the mermaid's name is Jeb_, change its color to a random color every 10 ticks
        if (!level().isClientSide && level().getGameTime() % 10 == 0 && this.getName().getString().toLowerCase(Locale.ROOT).equals("jeb_")) {
            this.entityData.set(COLOR, MermaidEntity.Variants.random().toString());
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        super.registerControllers(data);
        data.add(new AnimationController<>(this, "actionController", 10, event -> event.setAndContinue(getDeltaMovement().length() > 0 || (level().isClientSide && PatchouliHandler.isPatchouliWorld()) ? swim : idle)))
        ;
    }

    RawAnimation swim = RawAnimation.begin().thenLoop("swim");
    RawAnimation idle = RawAnimation.begin().thenLoop("idle");
    RawAnimation ground = RawAnimation.begin().thenLoop("ground");
    RawAnimation floating = RawAnimation.begin().thenLoop("floating");

    @Override
    public PlayState walkPredicate(AnimationState event) {

        return event.setAndContinue(onGround() && !isInWater() || (level().isClientSide && PatchouliHandler.isPatchouliWorld()) ? ground : floating);

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

    @Override
    public void setColor(String color, FamiliarEntity object) {
        super.setColor(color);
    }

    @Override
    public String getColor(FamiliarEntity mermaidFamiliar) {
        return this.entityData.get(COLOR);
    }

    @Override
    public ResourceLocation getTexture(FamiliarEntity entity) {
        return prefix("textures/entity/mermaid_" + (getColor().isEmpty() ? Variants.KELP.toString() : getColor()) + ".png");
    }

}
