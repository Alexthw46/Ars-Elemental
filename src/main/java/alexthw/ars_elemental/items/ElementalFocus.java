package alexthw.ars_elemental.items;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.Optional;

import static alexthw.ars_elemental.ConfigHandler.COMMON;

@Mod.EventBusSubscriber(modid = ArsNouveau.MODID)
public class ElementalFocus extends Item implements ISpellModifierItem {

    public final SpellSchool element;

    public ElementalFocus(Properties properties, SpellSchool element) {
        super(properties);
        this.element = element;
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (SpellSchools.ELEMENTAL.isPartOfSchool(spellPart)) {
            if (element.isPartOfSchool(spellPart)) {
                builder.addAmplification(getBoostMultiplier());
            } else {
                builder.addAmplification(getMalusMultiplier());
            }
        }
        return builder;
    }

    @SubscribeEvent
    public static void onCast(SpellCastEvent event) {
        if (!event.getWorld().isClientSide) {
            ElementalFocus focus = hasFocus(event.getWorld(), event.getEntityLiving());
            if (focus != null) {
                if (event.spell.recipe.stream().anyMatch(focus.getSchool()::isPartOfSchool))
                    event.spell.setCost((int) (event.spell.getCastingCost() * (1 - COMMON.FocusDiscount.get())));
            }
        }
    }

    public static ElementalFocus hasFocus(Level world, Entity entity) {
        if (!world.isClientSide && entity instanceof Player) {
            Optional<IItemHandlerModifiable> curios = CuriosUtil.getAllWornItems((LivingEntity)entity).resolve();
            if (curios.isPresent()) {
                IItemHandlerModifiable items = curios.get();
                for(int i = 0; i < items.getSlots(); ++i) {
                    Item item = items.getStackInSlot(i).getItem();
                    if (item instanceof ElementalFocus focus) {
                        return focus;
                    }
                }
            }
        }
        return null;
    }

    public SpellSchool getSchool() {
        return element;
    }

    double getBoostMultiplier(){
        return switch (element.getId()) {
            case "fire" -> COMMON.FireMasteryBuff.get();
            case "water" -> COMMON.WaterMasteryBuff.get();
            case "air" -> COMMON.AirMasteryBuff.get();
            case "earth" -> COMMON.EarthMasteryBuff.get();
            default -> 0;
        };
    }

    double getMalusMultiplier(){
        return switch (element.getId()) {
            case "fire" -> COMMON.FireMasteryDebuff.get();
            case "water" -> COMMON.WaterMasteryDebuff.get();
            case "air" -> COMMON.AirMasteryDebuff.get();
            case "earth" -> COMMON.EarthMasteryDebuff.get();
            default -> 0;
        };
    }

}
