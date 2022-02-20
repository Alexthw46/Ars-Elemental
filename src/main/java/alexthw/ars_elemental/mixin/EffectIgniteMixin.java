package alexthw.ars_elemental.mixin;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.items.ElementalFocus;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAOE;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentPierce;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectEvaporate;
import com.hollingsworth.arsnouveau.common.spell.effect.EffectIgnite;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;


@Mixin(EffectIgnite.class)
public class EffectIgniteMixin {

    @Inject(method = "onResolveEntity", at = {@At("HEAD")}, remap = false)
    public void onResolveEntity(EntityHitResult rayTraceResult, Level world, LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        if (!ConfigHandler.COMMON.EnableGlyphEmpowering.get()) return;
        if (rayTraceResult.getEntity() instanceof LivingEntity living && ElementalFocus.hasFocus(world, shooter) == ModRegistry.FIRE_FOCUS.get() && shooter!=living){
            living.addEffect(new MobEffectInstance(ModRegistry.HELLFIRE.get(), 200, (int) spellStats.getAmpMultiplier()/2));
        }
    }

    @Inject(method = "onResolveBlock", at = {@At("HEAD")}, remap = false, cancellable = true)
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @Nullable LivingEntity shooter, SpellStats spellStats, SpellContext spellContext, CallbackInfo ci) {
        BlockPos pos = rayTraceResult.getBlockPos();
        BlockState hitState = world.getBlockState(pos);
        if (hitState.getBlock() instanceof IceBlock){
            int aoeBuff = spellStats.getBuffCount(AugmentAOE.INSTANCE);
            int pierceBuff = spellStats.getBuffCount(AugmentPierce.INSTANCE);
            List<BlockPos> posList = SpellUtil.calcAOEBlocks(shooter, pos, rayTraceResult, aoeBuff, pierceBuff);
            BlockState state;

            if (ElementalFocus.hasFocus(world, shooter) == ModRegistry.FIRE_FOCUS.get() && spellContext.getSpell().recipe.contains(EffectEvaporate.INSTANCE)){
                //remove it
                for (BlockPos pos1 : posList) {
                    state = world.getBlockState(pos1);
                    if (state.getBlock() instanceof IceBlock) {
                        world.setBlock(pos1, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }else {
                ANFakePlayer fakePlayer = ANFakePlayer.getPlayer((ServerLevel) world);
                //just break it
                for (BlockPos pos1 : posList) {
                    state = world.getBlockState(pos1);
                    if (state.getBlock() instanceof IceBlock ice) {
                        ice.playerDestroy(world, fakePlayer, pos1, state, null, ItemStack.EMPTY);
                    }
                }
            }
            ci.cancel();
        }
    }
}
