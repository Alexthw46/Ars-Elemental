package alexthw.ars_elemental.api.item;

import alexthw.ars_elemental.common.items.armor.AAMaterials;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.armor.Materials;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IElementalArmor extends ISpellModifierItem {

    Map<SpellSchool, List<DamageSource>> damageResistances = new ConcurrentHashMap<>();

    static ArmorMaterial schoolToMaterial(SpellSchool element) {
        return switch (element.getId()) {
            case "fire" -> AAMaterials.fire;
            case "air" -> AAMaterials.air;
            case "earth" -> AAMaterials.earth;
            case "water" -> AAMaterials.water;

            default -> Materials.MEDIUM;
        };
    }

    @Override
    default SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (getSchool().isPartOfSchool(spellPart)) {
            builder.addAmplification(0.5);
        }
        return builder;
    }

    default double getDiscount(List<AbstractSpellPart> recipe) {
        for (AbstractSpellPart part : recipe) {
            if (getSchool().isPartOfSchool(part))
                return 0.1;
        }
        return 0;
    }

    SpellSchool getSchool();

    String getTier();

    default boolean doAbsorb(DamageSource damageSource) {
        return damageResistances.getOrDefault(getSchool(), List.of()).
                stream().map(d -> d.msgId)
                .anyMatch(msg -> msg.equals(damageSource.msgId));
    }

}
