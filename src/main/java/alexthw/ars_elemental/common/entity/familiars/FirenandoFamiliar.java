package alexthw.ars_elemental.common.entity.familiars;

import alexthw.ars_elemental.common.entity.FirenandoEntity.Variants;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.client.IVariantColorProvider;
import com.hollingsworth.arsnouveau.api.event.SpellModifierEvent;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.entity.familiar.FamiliarEntity;
import com.hollingsworth.arsnouveau.common.entity.familiar.ISpellCastListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.PlayState;
import software.bernie.ars_nouveau.geckolib3.core.builder.AnimationBuilder;
import software.bernie.ars_nouveau.geckolib3.core.controller.AnimationController;
import software.bernie.ars_nouveau.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationData;

import java.util.ArrayList;
import java.util.List;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class FirenandoFamiliar extends FamiliarEntity implements ISpellCastListener, IVariantColorProvider<FamiliarEntity> {
    public FirenandoFamiliar(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public FirenandoFamiliar(Level world) {
        super(ModEntities.FIRENANDO_FAMILIAR.get(), world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
    @Override
    public void registerControllers(AnimationData data) {
        super.registerControllers(data);
        data.addAnimationController(new AnimationController<>(this, "idle_controller", 0, this::idlePredicate));
    }

    public static List<AbstractSpellPart> projectileGlyphs = new ArrayList<>();
    public void onModifier(SpellModifierEvent event) {
        // as long as the familiar is alive and the owner is the caster, the familiar will increase the damage of fire spells by 2 and reduce the mana cost of projectiles by 20%
        if (this.isAlive() && this.getOwner() != null && this.getOwner().equals(event.caster)) {
            if (SpellSchools.ELEMENTAL_FIRE.isPartOfSchool(event.spellPart)) {
                event.builder.addDamageModifier(2.0D);
            }
            if (projectileGlyphs.contains(event.spellPart)){
                event.spellContext.getSpell().addDiscount((int) (event.spellContext.getSpell().getDiscountedCost() * 0.5));
            }
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (!player.level.isClientSide && player.equals(getOwner())) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() == Items.MAGMA_CREAM) {
                stack.shrink(1);
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200));
                return InteractionResult.SUCCESS;
            }
            if (stack.getItem() == Blocks.MAGMA_BLOCK.asItem() && !getColor().equals(Variants.MAGMA.toString())) {
                setColor(Variants.MAGMA.toString());
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            if (stack.getItem() == Blocks.SOUL_SAND.asItem() && !getColor().equals(Variants.SOUL.toString())) {
                setColor(Variants.SOUL.toString());
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.set(COLOR,Variants.MAGMA.toString());
    }

    @Override
    public PlayState walkPredicate(AnimationEvent event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle"));
        return PlayState.CONTINUE;
    }

    <T extends IAnimatable> PlayState idlePredicate(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle.body"));
        return PlayState.CONTINUE;
    }

    @Override
    public void setColor(String color, FamiliarEntity object) {
        super.setColor(color);
    }

    @Override
    public String getColor(FamiliarEntity firenandoFamiliar) {
        return this.entityData.get(COLOR);
    }

    @Override
    public ResourceLocation getTexture(FamiliarEntity entity) {
        return prefix("textures/entity/firenando_" + (getColor().isEmpty() ? Variants.MAGMA.toString() : getColor()) + ".png");
    }


}
