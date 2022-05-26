package alexthw.ars_elemental.common.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface IPropagator {

    default void copyResolver(HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats stats, SpellContext spellContext) {
        spellContext.setCanceled(true);
        if (spellContext.getCurrentIndex() >= spellContext.getSpell().recipe.size())
            return;
        Spell newSpell = new Spell(new ArrayList<>(spellContext.getSpell().recipe.subList(spellContext.getCurrentIndex(), spellContext.getSpell().recipe.size())));
        SpellContext newContext = new SpellContext(newSpell, shooter).withColors(spellContext.colors).withCastingTile(spellContext.castingTile).withType(spellContext.getType());
        SpellResolver resolver = new EntitySpellResolver(newContext);
        propagate(world, rayTraceResult.getLocation(), shooter, stats, resolver);

    }

    void propagate(Level world, Vec3 pos, LivingEntity shooter, SpellStats stats, SpellResolver resolver);

}
