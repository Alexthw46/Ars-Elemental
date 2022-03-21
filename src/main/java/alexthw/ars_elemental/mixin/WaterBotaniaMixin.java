package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.BotaniaCompat;
import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.ModRegistry;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectConjureWater;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EffectConjureWater.class)
public class WaterBotaniaMixin {

    @Inject(method = "onResolveBlock", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveBlock(BlockRayTraceResult rayTraceResult, World world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {

        if (ModList.get().isLoaded("botania")){
            if (BotaniaCompat.tryFillApothecary(rayTraceResult.getBlockPos(), world)) ci.cancel();
        }

    }
}
