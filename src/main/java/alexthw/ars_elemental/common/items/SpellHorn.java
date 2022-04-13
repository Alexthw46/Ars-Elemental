package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.client.castertools.SpellHornRenderer;
import alexthw.ars_elemental.common.glyphs.MethodCurvedProjectile;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.common.items.Wand;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAccelerate;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SpellHorn extends Wand {

    public SpellHorn(Properties properties) {
        super(properties);
    }

    @Override
    public boolean setSpell(ISpellCaster caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        ArrayList<AbstractSpellPart> recipe = new ArrayList<>();
        recipe.add(MethodCurvedProjectile.INSTANCE);
        recipe.add(AugmentAccelerate.INSTANCE);
        recipe.addAll(spell.recipe);
        spell.recipe = recipe;
        caster.setSpell(spell);
        return true;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new SpellHornRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
