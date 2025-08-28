package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.api.event.SpellCostCalcEvent;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
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
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static alexthw.ars_elemental.ArsElemental.prefix;
import static alexthw.ars_elemental.common.entity.FlashjackEntity.attack;
import static alexthw.ars_elemental.common.entity.FlashjackEntity.flapping;
import static alexthw.ars_elemental.common.entity.FlashjackEntity.idle;
import static alexthw.ars_elemental.common.entity.FlashjackEntity.inactive;

public class FlashjackFamiliar extends FlyingFamiliarEntity implements ISpellCastListener {

    static final Map<DyeColor, String> dyeToVariantMap = Map.of(
            DyeColor.RED, "flapjack",
            DyeColor.YELLOW, "flashjack"
    );
    public static List<AbstractSpellPart> movementGlyphs = new ArrayList<>();

    public FlashjackFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    public FlashjackFamiliar(Level world) {
        super(ModEntities.FLASHJACK_FAMILIAR.get(), world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new Parrot.ParrotWanderGoal(this, 1.0F));
    }

    public boolean isActive() {
        return this.getTarget() != null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "idle_controller", 5, event -> onGround() ? event.setAndContinue(inactive) : event.setAndContinue(idle)));
        data.add(new AnimationController<>(this, "action_controller", 5, event -> {
            // Play the attack animation if the entity has a target, try to not loop it
            if (isActive() && !event.isCurrentAnimation(attack)) {
                return event.setAndContinue(attack);
            }
            // If the entity is on the ground, stop the animation
            if (event.getAnimatable().onGround())
                return PlayState.STOP;
            // If the entity is in the attack animation, continue it until the end
            if (event.isCurrentAnimation(attack) && event.animationTick < 100) {
                return PlayState.CONTINUE;
            }
            // Defaults to flapping animation if in the air and not attacking
            return event.setAndContinue(flapping);
        }));
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!player.level.isClientSide && player.equals(getOwner())) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() == ModItems.FLASHING_POD.get().asItem()) {
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 6000, 0, false, false));
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, false, false));
            } else if (player.getMainHandItem().is(Tags.Items.DYES)) {
                DyeColor color = DyeColor.getColor(stack);
                if (color == null) return InteractionResult.PASS;
                String variant = dyeToVariantMap.getOrDefault(color, "flashjack");
                if (this.entityData.get(COLOR).equals(variant))
                    return InteractionResult.SUCCESS;
                setColor(variant);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void onModifier(SpellModifierEvent event) {
        // if the mermaid is alive and the owner is the player who cast the spell, and the spell is a water spell, increase the damage of the spell by 2
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster) && SpellSchools.ELEMENTAL_AIR.isPartOfSchool(event.spellPart)) {
            event.builder.addDamageModifier(2.0D);
        }
    }

    @Override
    public void onCostCalc(SpellCostCalcEvent event) {
        if (this.isAlive())
            if (this.getOwner() != null && this.getOwner().equals(event.context.getUnwrappedCaster())) {
                if (movementGlyphs.contains(event.context.getSpell().unsafeList().getFirst())) {
                    event.currentCost = (int) (event.currentCost - (event.context.getSpell().getCost() * 0.5));
                }
            }
    }


    public @NotNull ResourceLocation getTexture() {
        String variant = getColor().toLowerCase();
        if (variant.isEmpty())
            variant = "flashjack";
        return prefix("textures/entity/" + variant + ".png");
    }

}
