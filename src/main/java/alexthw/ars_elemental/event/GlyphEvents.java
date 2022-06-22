package alexthw.ars_elemental.event;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.blocks.ElementalSpellTurretTile;
import alexthw.ars_elemental.common.entity.spells.EntityMagnetSpell;
import alexthw.ars_elemental.common.items.ISchoolFocus;
import alexthw.ars_elemental.registry.ModItems;
import alexthw.ars_elemental.registry.ModRegistry;
import alexthw.ars_elemental.util.BotaniaCompat;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.event.EffectResolveEvent;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.crafting.recipes.CrushRecipe;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.*;
import com.hollingsworth.arsnouveau.setup.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.*;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID)
public class GlyphEvents {

    @SubscribeEvent
    public static void sensitiveCrush(EffectResolveEvent.Pre event) {
        if (event.resolveEffect != EffectCrush.INSTANCE) return;
        if (!(event.spellStats.hasBuff(AugmentSensitive.INSTANCE))) return;
        event.setCanceled(true);
        double aoeBuff = event.spellStats.getAoeMultiplier();
        int pierceBuff = event.spellStats.getBuffCount(AugmentPierce.INSTANCE);
        int maxItemCrush = (int) (4 + (4 * aoeBuff) + (4 * pierceBuff));
        List<ItemEntity> itemEntities = event.world.getEntitiesOfClass(ItemEntity.class, new AABB(new BlockPos(event.rayTraceResult.getLocation())).inflate(aoeBuff + 1.0));
        if (!itemEntities.isEmpty()) {
            crushItems(event.world, itemEntities, maxItemCrush);
        }
    }

    @SubscribeEvent
    public static void empowerGlyphs(EffectResolveEvent.Pre event) {

        if (event.resolveEffect == EffectConjureWater.INSTANCE && CompatUtils.isBotaniaLoaded() && event.rayTraceResult instanceof BlockHitResult blockHitResult) {
            if (BotaniaCompat.tryFillApothecary(blockHitResult.getBlockPos(), event.world)) {
                event.setCanceled(true);
                return;
            }
        }

        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;
        SpellSchool school = event.context.castingTile instanceof ElementalSpellTurretTile turret ? turret.getSchool() : ISchoolFocus.hasFocus(event.world, event.shooter);
        if (event.rayTraceResult instanceof BlockHitResult blockHitResult)
            empowerResolveOnBlocks(event, blockHitResult, school);
        else if (event.rayTraceResult instanceof EntityHitResult entityHitResult)
            empowerResolveOnEntities(event, entityHitResult, school);
    }


    public static void empowerResolveOnEntities(EffectResolveEvent.Pre event, EntityHitResult entityHitResult, SpellSchool school) {

        if (!(entityHitResult.getEntity() instanceof LivingEntity living && event.world instanceof ServerLevel))
            return;

        if (event.resolveEffect == EffectIgnite.INSTANCE) {
            if (event.shooter != living && school == ELEMENTAL_FIRE)
                living.addEffect(new MobEffectInstance(ModRegistry.HELLFIRE.get(), 200, (int) event.spellStats.getAmpMultiplier() / 2));
        }
        if (event.resolveEffect == EffectLaunch.INSTANCE) {
            if (event.spellStats.getDurationMultiplier() != 0 && school == ELEMENTAL_AIR) {
                living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 50 * (1 + (int) event.spellStats.getDurationMultiplier()), (int) event.spellStats.getAmpMultiplier() / 2));
            }
        }
        if (event.resolveEffect == EffectFreeze.INSTANCE) {
            if (event.shooter != living && school == ELEMENTAL_WATER) {
                if (living instanceof Skeleton skel && skel.getType() == EntityType.SKELETON) {
                    skel.setFreezeConverting(true);
                }
                living.setIsInPowderSnow(true);
                int newFrozenTicks = living.getTicksFrozen() + (int) (60 * event.spellStats.getAmpMultiplier());
                living.setTicksFrozen(newFrozenTicks);
                if (living.isFullyFrozen()) living.invulnerableTime = 0;
            }
            if (living.hasEffect(ModRegistry.HELLFIRE.get())) {
                living.removeEffect(ModRegistry.HELLFIRE.get());
            }
        }
        if (event.resolveEffect == EffectColdSnap.INSTANCE) {
            if (living.getPercentFrozen() > 0.75) {
                event.spellStats.setDamageModifier(event.spellStats.getDamageModifier() + 1);
            }
        }
        if (event.resolveEffect == EffectGrow.INSTANCE) {
            if (living.getMobType() == MobType.UNDEAD && school == ELEMENTAL_EARTH) {
                living.hurt(DamageSource.MAGIC, (float) (3 + 2 * event.spellStats.getAmpMultiplier() + event.spellStats.getDamageModifier()));
                if (living.isDeadOrDying() && event.world.getRandom().nextInt(10) < 3) {
                    BlockPos feet = living.getOnPos();
                    Material underfoot = event.world.getBlockState(feet).getMaterial();
                    Block blossom = ModItems.GROUND_BLOSSOM.get();
                    if ((underfoot == Material.DIRT || underfoot == Material.GRASS || underfoot == Material.MOSS) && event.world.getBlockState(feet.above()).isAir()) {
                        EffectPlaceBlock.attemptPlace(event.world, blossom.asItem().getDefaultInstance(), (BlockItem) blossom.asItem(), new BlockHitResult(living.position(), Direction.UP, feet, false), ANFakePlayer.getPlayer((ServerLevel) event.world));
                        //event.world.setBlockAndUpdate(feet.above(), ModItems.GROUND_BLOSSOM.get().defaultBlockState());
                    }
                }
            }
        }
        if (event.resolveEffect == EffectGravity.INSTANCE) {
            if (event.spellStats.hasBuff(AugmentSensitive.INSTANCE) && school == ELEMENTAL_EARTH) {
                EntityMagnetSpell.createMagnet(event.world, event.shooter, event.spellStats, event.context, event.rayTraceResult.getLocation());
                event.setCanceled(true);
            }
        }
    }

    public static void empowerResolveOnBlocks(EffectResolveEvent.Pre event, BlockHitResult blockHitResult, SpellSchool school) {

        if (event.resolveEffect == EffectConjureWater.INSTANCE) {
            if (school == ELEMENTAL_WATER) {
                if (GlyphEffectUtil.hasFollowingEffect(event.context, EffectFreeze.INSTANCE)) {
                    GlyphEffectUtil.placeBlocks(blockHitResult, event.world, event.shooter, event.spellStats, Blocks.ICE.defaultBlockState());
                    event.setCanceled(true);
                }
            }
        }

        if (event.resolveEffect == EffectGravity.INSTANCE) {
            if (event.spellStats.hasBuff(AugmentSensitive.INSTANCE) && school == ELEMENTAL_EARTH) {
                EntityMagnetSpell.createMagnet(event.world, event.shooter, event.spellStats, event.context, event.rayTraceResult.getLocation());
                event.setCanceled(true);
            }
        }

        if (event.resolveEffect == EffectIgnite.INSTANCE && event.shooter instanceof Player player) {
            //break or sublimate the ice
            int pierceBuff = event.spellStats.getBuffCount(AugmentPierce.INSTANCE);
            List<BlockPos> posList = SpellUtil.calcAOEBlocks(event.shooter, blockHitResult.getBlockPos(), blockHitResult, event.spellStats.getAoeMultiplier(), pierceBuff);
            BlockState state;

            boolean flag = school == ELEMENTAL_FIRE && GlyphEffectUtil.hasFollowingEffect(event.context, EffectEvaporate.INSTANCE);

            for (BlockPos pos1 : posList) {
                state = event.world.getBlockState(pos1);
                if (state.getBlock() instanceof IceBlock ice) {
                    if (flag) {
                        event.world.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);
                    } else {
                        ice.playerDestroy(event.world, player, pos1, state, null, ItemStack.EMPTY);
                    }
                    event.setCanceled(true);
                }
            }

        }

    }

    public static void crushItems(Level world, List<ItemEntity> itemEntities, int maxItemCrush) {
        List<CrushRecipe> recipes = world.getRecipeManager().getAllRecipesFor(RecipeRegistry.CRUSH_TYPE.get());
        CrushRecipe lastHit = null; // Cache this for AOE hits
        int itemsCrushed = 0;
        for (ItemEntity IE : itemEntities) {
            if (itemsCrushed > maxItemCrush) {
                break;
            }

            ItemStack stack = IE.getItem();
            Item item = stack.getItem();

            if (lastHit == null || !lastHit.matches(item.getDefaultInstance(), world)) {
                lastHit = recipes.stream().filter(recipe -> recipe.matches(item.getDefaultInstance(), world)).findFirst().orElse(null);
            }

            if (lastHit == null)
                continue;

            List<ItemStack> outputs = lastHit.getRolledOutputs(world.random);

            for (ItemStack result : outputs) {
                if (result.isEmpty())
                    continue;
                while (itemsCrushed <= maxItemCrush && !stack.isEmpty()) {
                    stack.shrink(1);
                    world.addFreshEntity(new ItemEntity(world, IE.getX(), IE.getY(), IE.getZ(), result.copy()));
                    itemsCrushed++;
                }
            }

        }
    }

}
