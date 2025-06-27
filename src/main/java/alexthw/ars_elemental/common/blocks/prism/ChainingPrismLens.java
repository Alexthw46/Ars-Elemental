package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.ConfigHandler;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.common.entity.EntityProjectileSpell;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ChainingPrismLens extends AbstractPrismLens implements ICasterTool {

    public ChainingPrismLens(Properties properties) {
        super(properties.component(DataComponentRegistry.SPELL_CASTER, new SpellCaster()), "chaining");
    }

    @Override
    public void shoot(ServerLevel world, BlockPos pos, EntityProjectileSpell spell, Vec3 angle) {
        super.shoot(world, pos, spell, angle);
        if (world.getBlockEntity(pos) instanceof AdvancedPrismTile ap) {
            AbstractCaster<?> spellCaster = getSpellCaster(ap.getLens());
            if (spellCaster != null) {
                SpellResolver spellResolver = spell.resolver();
                Spell.Mutable mutable = spellResolver.spell.mutable();
                mutable.add(spellCaster.getSpell().mutable().recipe.toArray(AbstractSpellPart[]::new));
                spellResolver.spellContext = spellResolver.spellContext.withSpell(mutable.immutable());
                spellResolver.spell = mutable.immutable();
                int cost = spellResolver.getResolveCost();
                // source check done in canConvert
                SourceUtil.takeSourceMultipleWithParticles(pos, world, 10, cost);
            }
        }
    }

    @Override
    public boolean canConvert(EntityProjectileSpell projectileSpell, Level level, BlockPos pos) {
        // get the prism and lens
        if (level.getBlockEntity(pos) instanceof AdvancedPrismTile ap) {
            AbstractCaster<?> spellCaster = getSpellCaster(ap.getLens());
            if (spellCaster != null) {
                Spell.Mutable mutable = projectileSpell.resolver().spell.mutable();
                mutable.add(spellCaster.getSpell().mutable().recipe.toArray(AbstractSpellPart[]::new));
                List<SpellValidationError> validationErrors = ArsNouveauAPI.getInstance().getSpellCraftingSpellValidator().validate(mutable.recipe);
                int manaCost = mutable.immutable().getCost();
                return mutable.recipe.size() < ConfigHandler.Common.CHAIN_LENS_LIMIT.get() && validationErrors.isEmpty() && SourceUtil.hasSourceNearby(pos, level, 10, manaCost);
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip2, @NotNull TooltipFlag flagIn) {
        stack.addToTooltip(DataComponentRegistry.SPELL_CASTER, context, tooltip2::add, flagIn);
        super.appendHoverText(stack, context, tooltip2, flagIn);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack pStack) {
        AbstractCaster<?> caster = getSpellCaster(pStack);
        if (caster != null && Config.GLYPH_TOOLTIPS.get() && !Screen.hasShiftDown() && !caster.isSpellHidden() && !caster.getSpell().isEmpty())
            return Optional.of(new SpellTooltip(caster));
        return Optional.empty();
    }


    @Override
    public void addTooltip(List<Component> tooltip, ItemStack lensStack) {
        AbstractCaster<?> spellCaster = getSpellCaster(lensStack);
        if (spellCaster != null && !spellCaster.isSpellHidden()) {
            tooltip.add(Component.literal("Adding glyphs: "));
            if (!spellCaster.getSpellName().isEmpty()) tooltip.add(Component.literal(spellCaster.getSpellName()));
            tooltip.add(Component.literal(spellCaster.getSpell().getDisplayString()));
        }
    }

}
