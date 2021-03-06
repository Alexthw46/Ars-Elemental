package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.ArsElemental;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.event.SpellCastEvent;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.UUID;

import static alexthw.ars_elemental.ConfigHandler.COMMON;
import static com.hollingsworth.arsnouveau.common.potions.ModPotions.MANA_REGEN_EFFECT;

@Mod.EventBusSubscriber(modid = ArsNouveau.MODID)
public class ElementalFocus extends ElementalCurio implements ISchoolFocus {

    protected SpellSchool element;

    public ElementalFocus(Properties properties, SpellSchool element) {
        super(properties);
        this.element = element;
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (element.isPartOfSchool(spellPart)) {
            builder.addAmplification(getBoostMultiplier());
        }
        return builder;
    }

    @SubscribeEvent
    public static void onCast(SpellCastEvent event) {
        if (!event.getWorld().isClientSide) {
            SpellSchool focus = ISchoolFocus.hasFocus(event.getWorld(), event.getEntityLiving());
            if (focus != null) {
                if (event.spell.recipe.stream().anyMatch(focus::isPartOfSchool))
                    event.spell.setCost((int) (event.spell.getCastingCost() * (1 - COMMON.FocusDiscount.get())));
            }
        }
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
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().getLevel().isClientSide() || !(slotContext.entity() instanceof Player player) || !COMMON.EnableRegenBonus.get())
            return;

        if (player.tickCount % 20 == 0) {
            switch (getSchool().getId()) {
                case "fire" -> {
                    if (player.isOnFire() || player.isInLava())
                        player.addEffect(new MobEffectInstance(MANA_REGEN_EFFECT, 200, 1));
                }
                case "water" -> {
                    if (player.isInWaterRainOrBubble()) {
                        if (player.isSwimming()) {
                            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 200, 1));
                            player.addEffect(new MobEffectInstance(MANA_REGEN_EFFECT, 120, 1));
                        } else {
                            player.addEffect(new MobEffectInstance(MANA_REGEN_EFFECT, 120, 0));
                        }
                    }
                }
                case "air" -> {
                    if (player.getY() > 200 || player.fallDistance > 3 || (player.hasEffect(MobEffects.SLOW_FALLING) && player.getDeltaMovement().y() < -0.3))
                        player.addEffect(new MobEffectInstance(MANA_REGEN_EFFECT, 120, 0));
                }
                case "earth" -> {
                    if (player.getY() < 0)
                        player.addEffect(new MobEffectInstance(MANA_REGEN_EFFECT, 120, 0));
                }
            }
        }

    }

    @NotNull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return switch (element.getId()){
            case "fire" -> new ICurio.SoundInfo(SoundEvents.FIRE_AMBIENT,1,1);
            case "water" -> new ICurio.SoundInfo(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT,1,1);
            case "earth" -> new ICurio.SoundInfo(SoundEvents.ROOTED_DIRT_BREAK,1,1);
            case "air" -> new ICurio.SoundInfo(SoundEvents.LIGHTNING_BOLT_THUNDER, 1, 1);
            default -> super.getEquipSound(slotContext, stack);
        };
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = super.getAttributeModifiers(slotContext, uuid, stack);
        if (element.equals(SpellSchools.ELEMENTAL_EARTH) && !(this instanceof LesserElementalFocus)) {
            map.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, ArsElemental.MODID + ":earth_focus", 0.3f, AttributeModifier.Operation.ADDITION));
        }
        return map;
    }
}
