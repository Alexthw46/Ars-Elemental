package alexthw.ars_elemental.common.items.caster_tools;

import alexthw.ars_elemental.client.castertools.SpellHornRenderer;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.util.TooManyCompats;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SpellHorn extends Item implements IAnimatable, ISpellModifierItem, ICasterTool {

    public SpellHorn(Properties properties) {
        super(properties);
    }

    public AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("wand_gem_spin", true));
        return PlayState.CONTINUE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level pLevel, LivingEntity pLivingEntity, int remainingTicks) {

        double j = getUseDuration(stack) - remainingTicks;

        if (j >= getMinUseDuration() && pLivingEntity instanceof Player player) {
            double aoeMult = Math.min(8, 0.5 + j / getMinUseDuration());
            if (pLevel instanceof ServerLevel) {
                ISpellCaster caster = getSpellCaster(stack);
                SpellResolver resolver = new SpellResolver(new SpellContext(caster.getSpell(), pLivingEntity).withColors(caster.getColor()));
                Predicate<Entity> filter = CompatUtils.tooManyGlyphsLoaded() ? TooManyCompats.getFilterPredicate(caster.getSpell()) : (e -> e instanceof LivingEntity);
                for (Entity l : pLevel.getEntities((Entity) null, new AABB(player.blockPosition()).inflate(aoeMult), filter)) {
                    resolver.onResolveEffect(pLevel, new EntityHitResult(l));
                }
                resolver.expendMana();
                //player.sendSystemMessage(Component.literal(j +"-"+ aoeMult));
            }
            if (j + 50 >= getMaxUseDuration()) {
                player.addEffect(new MobEffectInstance(ModPotions.SPELL_DAMAGE_EFFECT.get(), getMaxUseDuration() * 4));
            }
            play(pLevel, player, (float) aoeMult * 16);
            player.getCooldowns().addCooldown(this, 200);
        }

    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if ((getUseDuration(stack) - count) > getMaxUseDuration()) player.releaseUsingItem();
    }

    private static void play(Level level, Player player, float volume) {
        SoundEvent soundevent = SoundEvents.RAID_HORN;
        level.playSound(player, player, soundevent, SoundSource.RECORDS, volume, 1.0F);
        level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public int getMaxUseDuration() {
        return 300;
    }

    public int getMinUseDuration() {
        return 30;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public boolean setSpell(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        ArrayList<AbstractSpellPart> recipe = new ArrayList<>();
        caster.setSpell(spell);
        return true;
    }

    @Override
    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @org.jetbrains.annotations.Nullable LivingEntity shooter, SpellContext spellContext) {
        builder.addDurationModifier(1.0);
        return builder;
    }

    @Override
    public boolean isScribedSpellValid(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        return spell.recipe.stream().noneMatch(s -> s instanceof AbstractCastMethod);
    }

    @Override
    public void sendInvalidMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.wand.invalid"));
    }

    @Override
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new SpellHornRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        getInformation(stack, worldIn, tooltip2, flagIn);
        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
    }

}
