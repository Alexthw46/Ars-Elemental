package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.api.item.IElementalArmor;
import alexthw.ars_elemental.api.item.ISchoolBangle;
import alexthw.ars_elemental.api.item.ISchoolFocus;
import alexthw.ars_elemental.common.blocks.ElementalSpellTurretTile;
import alexthw.ars_elemental.common.entity.FirenandoEntity;
import alexthw.ars_elemental.common.glyphs.EffectBubbleShield;
import alexthw.ars_elemental.recipe.HeadCutRecipe;
import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.RegistryHelper;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.spell.IFilter;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentFortune;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCut;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

import static alexthw.ars_elemental.ConfigHandler.COMMON;
import static alexthw.ars_elemental.registry.ModPotions.HELLFIRE;
import static alexthw.ars_elemental.registry.ModPotions.MANA_BUBBLE;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_AIR;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class DamageEvents {

    @SubscribeEvent
    public static void betterFilters(SpellDamageEvent.Pre event) {
        if (event.context != null && event.context.getCurrentIndex() > 0 && event.context.getSpell().recipe.get(event.context.getCurrentIndex() - 1) instanceof IFilter filter) {
            if (!filter.shouldResolveOnEntity(event.target)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void bypassRes(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        if (event.getSource().getEntity() instanceof Player player && living != null) {
            SpellSchool focus = ISchoolFocus.hasFocus(event.getEntity().level, player);
            if (focus != null) {
                switch (focus.getId()) {
                    case "fire" -> {
                        if (event.getSource().isFire() && living.fireImmune()) {
                            event.setCanceled(true);
                            DamageSource newDamage = new EntityDamageSource("hellflare", player).setMagic();
                            if (event.getSource().isBypassArmor()) newDamage.bypassArmor();
                            if (event.getSource().isBypassMagic()) newDamage.bypassMagic();
                            living.hurt(newDamage, event.getAmount());
                        }
                    }
                    case "water" -> {
                        if (event.getSource().getMsgId().equals("drown") && living.getMobType() == MobType.WATER) {
                            event.setCanceled(true);
                            DamageSource newDamage = DamageSource.playerAttack(player).setMagic();
                            if (event.getSource().isBypassArmor()) newDamage.bypassArmor();
                            if (event.getSource().isBypassMagic()) newDamage.bypassMagic();
                            living.hurt(newDamage, event.getAmount());
                        }
                    }
                    case "air" -> {
                        if (living.invulnerableTime > 0 && event.getSource().isFall()) {
                            living.invulnerableTime = 0;
                        }
                    }
                }
            }
        } else if (event.getSource().getEntity() instanceof FirenandoEntity FE) {
            if (!(living instanceof Monster mob)) {
                event.setCanceled(true);
                living.clearFire();
            } else {
                if (mob.fireImmune() && event.getSource().isFire()) {
                    event.setCanceled(true);
                    mob.hurt(DamageSource.mobAttack(FE).setMagic().bypassArmor(), event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public static void banglesSpecials(LivingAttackEvent event) {

        LivingEntity living = event.getEntity();

        if (event.getSource().getEntity() instanceof Player player && living != null && living != player) {
            SpellSchool bangle = ISchoolBangle.hasBangle(event.getEntity().level, player);
            if (bangle != null) {
                switch (bangle.getId()) {
                    case "fire" -> living.setSecondsOnFire(5);
                    case "water" -> living.setTicksFrozen(living.getTicksFrozen() + 100);
                    case "earth" -> living.addEffect(new MobEffectInstance(ModPotions.SNARE_EFFECT.get(), 60));
                }
            }
        }
        if (living instanceof Player player && (event.getSource() == DamageSource.CACTUS || event.getSource() == DamageSource.SWEET_BERRY_BUSH)) {
            if (ISchoolBangle.hasBangle(event.getEntity().level, player) == ELEMENTAL_EARTH) {
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public static void handleHealing(LivingHealEvent event) {
        if (COMMON.EnableGlyphEmpowering.get() || event.getEntity() instanceof Player player && ISchoolFocus.hasFocus(player.getLevel(), player) == ELEMENTAL_EARTH) {
            event.setAmount(event.getAmount() * 1.5F);
        }
        if (event.getEntity().hasEffect(HELLFIRE.get())) {
            MobEffectInstance inst = event.getEntity().getEffect(HELLFIRE.get());
            if (inst == null) return;
            int amplifier = Math.min(9, inst.getAmplifier());
            event.setAmount(event.getAmount() * (10 - amplifier) / 10);
            event.getEntity().invulnerableTime = 0;
        }
    }

    @SubscribeEvent
    public static void damageReduction(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource() == DamageSource.FLY_INTO_WALL && ISchoolFocus.hasFocus(event.getEntity().level, player) == ELEMENTAL_AIR) {
                event.setAmount(event.getAmount() * 0.1f);
            }
            HashMap<SpellSchool, Integer> bonusMap = new HashMap<>();
            int bonusReduction = 0;
            for (ItemStack stack : player.getArmorSlots()) {
                Item item = stack.getItem();
                if (item instanceof IElementalArmor armor && armor.doAbsorb(event.getSource())) {
                    bonusReduction++;
                    if (bonusMap.containsKey(armor.getSchool())) {
                        bonusMap.put(armor.getSchool(), bonusMap.get(armor.getSchool()) + 1);
                    } else {
                        bonusMap.put(armor.getSchool(), 1);
                    }
                }
            }
            if (bonusMap.getOrDefault(SpellSchools.ELEMENTAL_FIRE, 0) == 4 && event.getSource().isFire() || event.getSource().msgId.equals("hellflare")) {
                player.clearFire();
            }
            if (bonusMap.getOrDefault(SpellSchools.ELEMENTAL_WATER, 0) == 4 && event.getSource() == DamageSource.DROWN) {
                player.setAirSupply(player.getMaxAirSupply());
                bonusReduction += 5;
            }
            if (bonusMap.getOrDefault(ELEMENTAL_EARTH, 0) == 4 && player.getEyePosition().y() < 20 && player.getFoodData().getFoodLevel() < 2) {
                player.getFoodData().setFoodLevel(20);
            }
            if (bonusMap.getOrDefault(ELEMENTAL_AIR, 0) == 4 && event.getSource().isFall()) {
                bonusReduction += 5;
            }

            if (bonusReduction > 0) {
                int finalBonusReduction = bonusReduction;
                CapabilityRegistry.getMana(player).ifPresent(mana -> {
                    if (finalBonusReduction > 3) mana.addMana(event.getAmount() * 5);
                    event.getEntity().addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 200, finalBonusReduction / 2));
                });
                event.setAmount(event.getAmount() * (1 - (bonusReduction / 10F)));
            }

        }

        int ManaBubbleCost = EffectBubbleShield.INSTANCE.GENERIC_INT.get();

        if (event.getEntity() != null && event.getEntity().hasEffect(MANA_BUBBLE.get())) {
            LivingEntity living = event.getEntity();
            CapabilityRegistry.getMana(event.getEntity()).ifPresent(mana -> {
                double maxReduction = mana.getCurrentMana() / ManaBubbleCost;
                double amp = Math.min(1 + living.getEffect(MANA_BUBBLE.get()).getAmplifier() / 2D, maxReduction);
                float newDamage = (float) Math.max(0.1, event.getAmount() - amp);
                float actualReduction = event.getAmount() - newDamage;
                if (actualReduction > 0 && mana.getCurrentMana() >= actualReduction * ManaBubbleCost) {
                    event.setAmount(newDamage);
                    mana.removeMana(actualReduction * ManaBubbleCost);
                }
                if (mana.getCurrentMana() < ManaBubbleCost) {
                    living.removeEffect(MANA_BUBBLE.get());
                }
            });
        }
    }

    @SubscribeEvent
    public static void statusProtect(MobEffectEvent.Applicable event) {
        if (event.getEntity().hasEffect(MANA_BUBBLE.get()) && event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL) {

            int ManaBubbleCost = EffectBubbleShield.INSTANCE.GENERIC_INT.get() * 2;
            if (event.getEntity().getRandom().nextInt(10) == 0) {
                CapabilityRegistry.getMana(event.getEntity()).ifPresent(mana -> {
                    if (mana.getCurrentMana() >= ManaBubbleCost) {
                        mana.removeMana(ManaBubbleCost);
                        event.setCanceled(true);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void vorpalCut(SpellDamageEvent.Post event) {
        if (!(event.target instanceof LivingEntity living) || living.getHealth() > 0) return;
        SpellSchool school = event.context.castingTile instanceof ElementalSpellTurretTile turret ? turret.getSchool() : ISchoolFocus.hasFocus(event.caster.level, event.caster);
        Spell subspell = new Spell(event.context.getSpell().recipe.subList(event.context.getCurrentIndex() - 1, event.context.getSpell().recipe.size()));
        if (subspell.recipe.get(0) == EffectCut.INSTANCE && school == ELEMENTAL_AIR) {
            ItemStack skull = null;
            int chance = 0;
            ResourceLocation mob = RegistryHelper.getRegistryName(living.getType());
            if (living instanceof Player player) {
                GameProfile gameprofile = player.getGameProfile();
                skull = new ItemStack(Items.PLAYER_HEAD);
                chance = 20;
                skull.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), gameprofile));
            } else {
                for (HeadCutRecipe recipe : living.getLevel().getRecipeManager().getAllRecipesFor(ModRegistry.HEAD_CUT.get())) {
                    if (recipe.mob.equals(mob)) {
                        skull = recipe.result.copy();
                        chance = recipe.chance;
                        break;
                    }
                }
            }
            if (skull == null) return;

            int looting = Math.max(4, subspell.getBuffsAtIndex(0, event.caster, AugmentFortune.INSTANCE));
            for (int i = -1; i < looting; i++)
                if (living.level.random.nextInt(100) <= chance) {
                    living.spawnAtLocation(skull);
                    break;
                }
        }
    }

}
