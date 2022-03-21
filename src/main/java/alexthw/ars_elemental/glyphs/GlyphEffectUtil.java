package alexthw.ars_elemental.glyphs;

import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SpellUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayerFactory;

public class GlyphEffectUtil {

    public static PlayerEntity getPlayer(LivingEntity entity, ServerWorld world){
        return entity instanceof PlayerEntity ? (PlayerEntity) entity : FakePlayerFactory.getMinecraft(world);
    }
    public static void placeBlocks(BlockRayTraceResult rayTraceResult, World world, LivingEntity shooter, SpellStats spellStats, BlockState toPlace) {
        ANFakePlayer fakePlayer = ANFakePlayer.getPlayer((ServerWorld) world);
        for(BlockPos pos : SpellUtil.calcAOEBlocks(shooter, rayTraceResult.getBlockPos(), rayTraceResult, spellStats)) {
            pos =  rayTraceResult.isInside() ? pos : pos.relative(rayTraceResult.getDirection());
            if(!BlockUtil.destroyRespectsClaim(getPlayer(shooter, (ServerWorld) world), world, pos))
                continue;
            BlockState state = world.getBlockState(pos);
            if (state.getMaterial().isReplaceable() && world.isUnobstructed(toPlace, pos, ISelectionContext.of(fakePlayer))){
                world.setBlockAndUpdate(pos, toPlace);
            }
        }
    }




}
