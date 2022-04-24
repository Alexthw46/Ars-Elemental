package alexthw.ars_elemental.mixin;

import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.crafting.recipes.CrushRecipe;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectCrush;
import com.hollingsworth.arsnouveau.setup.RecipeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(EffectCrush.class)

public class EffectCrushMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (spellStats.hasBuff(AugmentSensitive.INSTANCE)) {
            int aoeBuff = spellStats.getBuffCount(AugmentAOE.INSTANCE);
            int pierceBuff = spellStats.getBuffCount(AugmentPierce.INSTANCE);
            int maxItemCrush = 4 * (1 + aoeBuff + pierceBuff);
            List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, new AABB(rayTraceResult.getEntity().blockPosition()).inflate(aoeBuff + 1.0));
            if (!itemEntities.isEmpty()) {
                crushItems(world, itemEntities, maxItemCrush);
                ci.cancel();
            }

        }
    }


    @Inject(method = "onResolveBlock", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (spellStats.hasBuff(AugmentSensitive.INSTANCE)){
            int aoeBuff = spellStats.getBuffCount(AugmentAOE.INSTANCE);
            int pierceBuff = spellStats.getBuffCount(AugmentPierce.INSTANCE);
            int maxItemCrush = 4 * (1 + aoeBuff + pierceBuff);
            List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, new AABB(rayTraceResult.getBlockPos()).inflate(aoeBuff + 1.0));
            if (!itemEntities.isEmpty()) crushItems(world, itemEntities, maxItemCrush);
            ci.cancel();
        }
    }

    public void crushItems(Level world, List<ItemEntity> itemEntities, int maxItemCrush) {
        List<CrushRecipe> recipes = world.getRecipeManager().getAllRecipesFor(RecipeRegistry.CRUSH_TYPE);
        CrushRecipe lastHit = null; // Cache this for AOE hits
        int itemsCrushed = 0;
        for (ItemEntity IE : itemEntities){
            if (itemsCrushed > maxItemCrush){
                break;
            }

            ItemStack stack = IE.getItem();
            Item item = stack.getItem();

            if(lastHit == null || !lastHit.matches(item.getDefaultInstance(), world)){
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
