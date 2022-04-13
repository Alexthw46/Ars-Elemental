package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.items.ISchoolItem;
import alexthw.ars_elemental.util.BotaniaCompat;
import alexthw.ars_elemental.util.CompatUtils;
import alexthw.ars_elemental.util.GlyphEffectUtil;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectConjureWater;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectFreeze;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.ELEMENTAL_WATER;

@Mixin(EffectConjureWater.class)
public class EffectWaterMixin {

    @Inject(method = "onResolveBlock", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {

        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;
        if (ISchoolItem.hasFocus(world, shooter) == ELEMENTAL_WATER && spellContext.getSpell().recipe.contains(EffectFreeze.INSTANCE)) {
            BlockState toPlace = Blocks.ICE.defaultBlockState();
            GlyphEffectUtil.placeBlocks(rayTraceResult, world, shooter, spellStats, toPlace);
            ci.cancel();
        }
        if (CompatUtils.isBotaniaLoaded()) {
            if (BotaniaCompat.tryFillApothecary(rayTraceResult.getBlockPos(), world)) ci.cancel();
        }

    }
}
