package alexthw.ars_elemental.item;

import alexthw.ars_elemental.ArsElemental;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.item.ISpellModifierItem;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CuriosUtil;
import com.hollingsworth.arsnouveau.common.spell.method.MethodProjectile;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static alexthw.ars_elemental.ConfigHandler.COMMON;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ElementalFocus extends Item implements ISpellModifierItem {

    private final SpellSchool school;

    public ElementalFocus(Properties props, SpellSchool school) {
        super(props);
        this.school = school;
    }

    double getBoostMultiplier(){
        double mult;
        switch (school.getId()){
            case "fire" :
                mult = COMMON.FireMasteryBuff.get();
                break;
            case "water":
                mult = COMMON.WaterMasteryBuff.get();
                break;
            case "air":
                mult = COMMON.AirMasteryBuff.get();
                break;
            case "earth":
                mult = COMMON.EarthMasteryBuff.get();
                break;
            default:
                mult = 0;
        }
        return mult;
    }

    double getMalusMultiplier(){
        double mult;
        switch (school.getId()){
            case "fire" :
                mult = COMMON.FireMasteryDebuff.get();
                break;
            case "water":
                mult = COMMON.WaterMasteryDebuff.get();
                break;
            case "air":
                mult = COMMON.AirMasteryDebuff.get();
                break;
            case "earth":
                mult = COMMON.EarthMasteryDebuff.get();
                break;
            default:
                mult = 0;
        }
        return mult;
    }
    public SpellSchool getSchool() {
        return school;
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, RayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (!SpellSchools.ELEMENTAL.isPartOfSchool(spellPart)) return builder;
            if (school.isPartOfSchool(spellPart)) {
                builder.addAmplification(getBoostMultiplier());
            }else {
                builder.addAmplification(getMalusMultiplier());
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

    public static ElementalFocus hasFocus(World world, Entity entity) {
        if (!world.isClientSide && entity instanceof PlayerEntity) {
            IItemHandlerModifiable items = CuriosUtil.getAllWornItems((LivingEntity)entity).orElse(null);
            if (items != null) {
                for(int i = 0; i < items.getSlots(); ++i) {
                    Item item = items.getStackInSlot(i).getItem();
                    if (item instanceof ElementalFocus) {
                        return (ElementalFocus)item;
                    }
                }
            }
        }

        return null;
    }

}
