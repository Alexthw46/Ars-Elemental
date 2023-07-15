package alexthw.ars_elemental.common.items.foci;

import alexthw.ars_elemental.ArsElemental;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
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
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.UUID;

import static alexthw.ars_elemental.ConfigHandler.COMMON;


public class GreaterElementalFocus extends ElementalFocus {
    public GreaterElementalFocus(Properties properties, SpellSchool element) {
        super(properties, element);
    }

    @Override
    public double getDiscount() {
        return COMMON.MajorFocusDiscount.get();
    }

    public SpellStats.Builder applyItemModifiers(ItemStack stack, SpellStats.Builder builder, AbstractSpellPart spellPart, HitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellContext spellContext) {
        if (element.isPartOfSchool(spellPart)) {
            builder.addAmplification(getBoostMultiplier() * 2);
        }
        return builder;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level().isClientSide() || !(slotContext.entity() instanceof Player player) || !COMMON.EnableRegenBonus.get())
            return;
        // every 20 ticks, check if the player is in the right environment for the school, and apply the appropriate effect
        if (player.tickCount % 20 == 0) {
            switch (getSchool().getId()) {
                case "fire" -> {
                    if (player.isOnFire() || player.isInLava())
                        player.addEffect(new MobEffectInstance(ModPotions.SPELL_DAMAGE_EFFECT.get(), 200, 2));
                }
                case "water" -> {
                    if (player.isInWaterRainOrBubble()) {
                        if (player.isSwimming()) {
                            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 200, 1));
                            player.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 120, 1));
                        } else {
                            player.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 120, 0));
                        }
                    }
                }
                case "air" -> {
                    if (player.hasEffect(ModPotions.SHOCKED_EFFECT.get())) {
                        player.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 60, 1));
                        player.addEffect(new MobEffectInstance(ModPotions.SPELL_DAMAGE_EFFECT.get(), 60, 1));
                    } else if (player.getY() > 200)
                        player.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 120, 0));
                }
                case "earth" -> {
                    if (player.getY() < 0)
                        player.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 120, 0));
                }
            }
        }

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (element.equals(SpellSchools.ELEMENTAL_EARTH)) {
            Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
            map.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, ArsElemental.MODID + ":earth_focus", 0.2f, AttributeModifier.Operation.ADDITION));
            return map;
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
