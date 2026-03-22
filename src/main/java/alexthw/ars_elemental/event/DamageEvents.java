package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.entity.mages.EntityMageBase;
import alexthw.ars_elemental.common.entity.mages.WaterMage;
import alexthw.ars_elemental.common.glyphs.EffectBubbleShield;
import alexthw.ars_elemental.datagen.AETagsProvider;
import alexthw.ars_elemental.recipe.HeadCutRecipe;
import alexthw.ars_elemental.registry.ModRegistry;
import com.alexthw.sauce.api.item.ISchoolBangle;
import com.alexthw.sauce.api.item.ISchoolFocus;
import com.alexthw.sauce.registry.SauceTags;
import com.hollingsworth.arsnouveau.api.entity.ISummon;
import com.hollingsworth.arsnouveau.api.event.SpellDamageEvent;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.util.DamageUtil;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.hollingsworth.arsnouveau.setup.registry.DamageTypesRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import com.hollingsworth.arsnouveau.setup.registry.RegistryHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.Optional;
import java.util.Set;

import static alexthw.ars_elemental.registry.ModPotions.FROZEN;
import static alexthw.ars_elemental.registry.ModPotions.MAGIC_FIRE;
import static alexthw.ars_elemental.registry.ModPotions.MANA_BUBBLE;
import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_EARTH;

@EventBusSubscriber(modid = ArsElemental.MODID)
public class DamageEvents {

    @SubscribeEvent
    public static void changeDamageType(SpellDamageEvent.Pre preSpellDamageEvent) {
        if (preSpellDamageEvent.damageSource.is(DamageTypeTags.IS_FIRE) && preSpellDamageEvent.target instanceof LivingEntity living) {
            if (living.hasEffect(MobEffects.FIRE_RESISTANCE) && living.hasEffect(MAGIC_FIRE)) {
                // preserve the luck level of the spell damage source
                DamageUtil.SpellDamageSource newSource = (DamageUtil.SpellDamageSource) DamageUtil.source(living.level(), ModRegistry.MAGIC_FIRE, preSpellDamageEvent.damageSource.getEntity());
                if (preSpellDamageEvent.damageSource instanceof DamageUtil.SpellDamageSource oldspellDamageSource) {
                    newSource.setLuckLevel(oldspellDamageSource.getLuckLevel());
                }
                preSpellDamageEvent.damageSource = newSource;
            }
        }
    }

    @SubscribeEvent
    public static void bypassDmgInv(EntityInvulnerabilityCheckEvent event) {
        Entity living = event.getEntity();
        // the target was invulnerable to the damage source
        if (event.getSource().getEntity() instanceof Player player && event.getOriginalInvulnerability()) {
            var source = event.getSource();
            Set<SpellSchool> focus = ISchoolFocus.getFociSchools(player);
            if (!focus.isEmpty()) {
                boolean flag = true;
                for (SpellSchool school : focus) {
                    switch (school.getId()) {
                        case "fire" -> {
                            // check if target was invulnerable only to fire damage, not a true invulnerability
                            if (source.is(DamageTypeTags.IS_FIRE) && living.fireImmune()) {
                                flag = flag && (living.isRemoved()
                                        || living.isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer());
                            }
                        }
                        case "air" -> {
                            // check if target was invulnerable only to fall damage, not a true invulnerability
                            if (source.is(DamageTypeTags.IS_FALL) && living.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
                                flag = flag && (living.isRemoved()
                                        || living.isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer());
                            }
                        }
                        /*
                        case "water" -> {
                            // check if target was invulnerable only to drowning damage, not a true invulnerability
                            if (source.is(DamageTypeTags.IS_DROWNING) && living.getType().is(EntityTypeTags.CAN_BREATHE_UNDER_WATER)){
                                flag = flag && (living.isRemoved()
                                        || living.isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer());
                            }
                        }
                         */
                    }
                }
                // the target was invulnerable to the damage source and the player has a focus that bypasses it
                if (!flag)
                    event.setInvulnerable(false);
            }
        }
    }

    @SubscribeEvent
    public static void banglesSpecials(LivingIncomingDamageEvent event) {

        LivingEntity eventTarget = event.getEntity();

        //if the player is wearing a bangle, apply special effects on hit
        if (event.getSource().getEntity() instanceof Player player && eventTarget != player) {
            if (eventTarget instanceof ISummon summon && summon.getOwnerAlt() == player) return;
            for (SpellSchool bangle : ISchoolBangle.getBangles(event.getEntity().level(), player)) {
                if (bangle != null) {
                    switch (bangle.getId()) {
                        case "fire" -> eventTarget.setRemainingFireTicks(20 * 5);
                        case "water" -> eventTarget.setTicksFrozen(eventTarget.getTicksFrozen() + 100);
                        case "earth" -> eventTarget.addEffect(new MobEffectInstance(ModPotions.SNARE_EFFECT, 60));
                        case "necromancy" -> {
                            if (player.getRandom().nextBoolean())
                                eventTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, 60));
                            else {
                                eventTarget.heal(1.0F);
                                player.heal(1.0F);
                            }
                        }
                        case "conjuration" -> {
                            BlockPos pos = player.blockPosition();
                            // fetch all summons around the player and aggro them to the target
                            player.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos.north(30).west(30).below(10).getCenter(), pos.south(30).east(30).above(10).getCenter()), e -> e instanceof ISummon s && player.equals(s.getOwnerAlt())).forEach(e -> {
                                if (e instanceof Monster mob) {
                                    mob.setTarget(eventTarget);
                                } else if (e instanceof NeutralMob neutralMob) {
                                    neutralMob.setTarget(eventTarget);
                                }
                                e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1));
                            });

                        }
                    }
                }
            }
        }

        if (eventTarget instanceof Player player && (event.getSource().is(DamageTypes.CACTUS) || event.getSource().is(DamageTypes.SWEET_BERRY_BUSH) || event.getSource().is(DamageTypesRegistry.SOURCE_BERRY_BUSH))) {
            if (ISchoolBangle.hasBangle(event.getEntity().level(), player, ELEMENTAL_EARTH)) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void handleHealing(LivingHealEvent event) {
        //boost healing if you have earth focus
        if (event.getEntity() instanceof Player player && ISchoolFocus.getFociSchools(player).contains(ELEMENTAL_EARTH)) {
            event.setAmount(event.getAmount() * 1.5F);
        }
        //cancel healing if under frozen effect
        if (event.getEntity().hasEffect(FROZEN)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void preReductionDamageTweaking(LivingIncomingDamageEvent event) {

        var dealer = event.getSource().getEntity();
        var target = event.getEntity();

        // if frozen, boost next fire damage
        if (target.hasEffect(FROZEN) && event.getSource().is(SauceTags.FIRE_DAMAGE)) {
            event.setAmount(event.getAmount() * 1.5F);
            target.removeEffect(FROZEN);
        }
        // if the target has magic fire, reduce earth damage
        if (target.hasEffect(MAGIC_FIRE) && event.getSource().is(SauceTags.EARTH_DAMAGE)) {
            event.setAmount(event.getAmount() * 0.85F);
        }

        if (dealer instanceof LivingEntity caster && (target instanceof Player || target instanceof EntityMageBase)) {
            Set<SpellSchool> foci = ISchoolFocus.getFociSchools(caster);
            for (SpellSchool focus : foci) {
                //if the player has a focus, apply the special effects
                switch (focus.getId()) {
                    case "water" -> {
                        // boost freezing damage if the target is already partially frozen
                        if (target.getPercentFrozen() > 0.75F && event.getSource().is(DamageTypeTags.IS_FREEZING)) {
                            event.setAmount(event.getAmount() * 1.25F);
                        }
                    }
                    case "air" -> {
                        //let's try to compensate the loss of iframe skip with a buff to WS
                        if (target.hasEffect(MobEffects.LEVITATION) && event.getSource().is(DamageTypeTags.IS_FALL)) {
                            event.setAmount(event.getAmount() * 1.25F);
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void postReductionDamageTweaking(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();

        // if damage is magic and target has magic fire, add back the half the damage that was reduced from the armor points
        if (event.getSource().is(Tags.DamageTypes.IS_MAGIC) && target.hasEffect(MAGIC_FIRE)) {
            var armorReduction = event.getContainer().getReduction(DamageContainer.Reduction.ARMOR);
            event.setNewDamage(event.getNewDamage() + armorReduction * 0.5F);
        }
        boolean not_bypassEnchants = !event.getSource().is(DamageTypeTags.BYPASSES_ENCHANTMENTS);

        //check if the entity has the mana bubble effect and if so, reduce the damage
        MobEffectInstance bubbleEffect = target.getEffect(MANA_BUBBLE);
        if (not_bypassEnchants && bubbleEffect != null) {
            float ManaBubbleCost = Math.max(0, EffectBubbleShield.INSTANCE.GENERIC_INT.get() - ManaUtil.getPlayerDiscounts(target, new Spell(EffectBubbleShield.INSTANCE), ItemStack.EMPTY) * 0.75F);
            var mana = CapabilityRegistry.getMana(target);
            // guard against infinite damage that would cause NaN mana
            if (mana != null && event.getNewDamage() < 100000) {
                double maxReduction = mana.getCurrentMana() / ManaBubbleCost;
                double amp = Math.min(1 + bubbleEffect.getAmplifier(), maxReduction);
                float newDamage = (float) Math.max(0, event.getNewDamage() - amp);
                float actualReduction = event.getNewDamage() - newDamage;
                // don't deplete mana if the entity is invulnerable due to a previous attack
                if (actualReduction > 0 && mana.getCurrentMana() >= ManaBubbleCost) {
                    event.setNewDamage(newDamage);
                    if (event.getContainer().getPostAttackInvulnerabilityTicks() != event.getEntity().invulnerableTime) {
                        mana.removeMana(actualReduction * ManaBubbleCost);
                    }
                }
                if (mana.getCurrentMana() < ManaBubbleCost) {
                    target.removeEffect(MANA_BUBBLE);
                }
            } else if (target instanceof WaterMage) {
                event.setNewDamage(event.getNewDamage() * 0.75F);
            }
        }
    }


    //When the entity have the mana bubble and is hit by a harmful effect, it will consume mana to try to protect against it
    @SubscribeEvent
    public static void statusProtect(MobEffectEvent.Applicable event) {
        if (event.getEntity().hasEffect(MANA_BUBBLE)) {
            event.getEffectInstance();
            if (event.getEffectInstance().getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                Optional<HolderSet.Named<MobEffect>> effects = event.getEntity().level().registryAccess().registryOrThrow(Registries.MOB_EFFECT).getTag(AETagsProvider.AEMobEffectTagProvider.BUBBLE_BLACKLIST);
                if (effects.isPresent() && effects.get().stream().anyMatch(effect -> effect == event.getEffectInstance().getEffect()))
                    return;

                int ManaBubbleCost = EffectBubbleShield.INSTANCE.GENERIC_INT.get() * 2;
                if (event.getEntity().getRandom().nextInt(10) == 0) {
                    var mana = CapabilityRegistry.getMana(event.getEntity());
                    if (mana != null) {
                        if (mana.getCurrentMana() >= ManaBubbleCost) {
                            mana.removeMana((double) ManaBubbleCost / 2);
                            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
                        }
                    }
                }
            } else if (event.getEffectInstance().getEffect() == MAGIC_FIRE.getDelegate()) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    @SubscribeEvent
    public static void vorpalCut(SpellDamageEvent.Post event) {
        if (!(event.target instanceof LivingEntity living) || living.getHealth() > 0) return;

        if (event.damageSource.is(ModRegistry.CUT)) {
            ItemStack skull = null;
            int chance = 0;
            ResourceLocation mob = RegistryHelper.getRegistryName(living.getType());
            if (living instanceof Player player) {
                GameProfile gameprofile = player.getGameProfile();
                skull = new ItemStack(Items.PLAYER_HEAD);
                chance = 20;
                skull.set(DataComponents.PROFILE, new ResolvableProfile(gameprofile));
            } else {
                for (RecipeHolder<HeadCutRecipe> recipeh : living.level().getRecipeManager().getAllRecipesFor(ModRegistry.HEAD_CUT.get())) {
                    HeadCutRecipe recipe = recipeh.value();
                    if (recipe.mob.equals(mob)) {
                        skull = recipe.result.copy();
                        chance = recipe.chance;
                        break;
                    }
                }
            }
            if (skull == null) return;

            int looting = Math.min(3, ((DamageUtil.SpellDamageSource) event.damageSource).getLuckLevel());
            for (int i = -1; i < looting; i++)
                if (living.getRandom().nextInt(100) <= chance) {
                    living.spawnAtLocation(skull);
                    break;
                }
        }
    }

}
