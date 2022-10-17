package alexthw.ars_elemental.common.blocks;

import alexthw.ars_elemental.common.entity.spells.EntityLerpedProjectile;
import alexthw.ars_elemental.registry.ModEntities;
import alexthw.ars_elemental.util.BotaniaCompat;
import alexthw.ars_elemental.util.CompatUtils;
import com.hollingsworth.arsnouveau.api.client.ITooltipProvider;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.NBTUtil;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import com.hollingsworth.arsnouveau.common.items.DominionWand;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EverfullUrnTile extends ModdedTile implements ITickable, IWandable, ITooltipProvider {

    Set<BlockPos> toList = new HashSet<>();

    boolean needsSource;

    public EverfullUrnTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModEntities.URN_TILE, pWorldPosition, pBlockState);
    }

    @Override
    public void tick() {
        if (level == null || level.getGameTime() % 60 != 0 || toList.isEmpty() || level.isClientSide)
            return;

        ArrayList<BlockPos> stale = new ArrayList<>();

        for (BlockPos toPos : toList) {
            if (this.needsSource)
                break;
            if (!level.isLoaded(toPos))
                continue;
            if (!isRefillable(toPos, level)) {
                stale.add(toPos);
                continue;
            }
            if (SourceUtil.hasSourceNearby(this.worldPosition, level, 6, 100) && tryRefill(level, toPos)) {
                SourceUtil.takeSourceNearbyWithParticles(getBlockPos(), level, 6, 100);
                createParticles(this.worldPosition, toPos);
            }
        }
        for (BlockPos s : stale) {
            toList.remove(s);
        }
        updateBlock();
    }

    private boolean tryRefill(Level world, BlockPos toPos) {

        if (world.getBlockState(toPos) == Blocks.CAULDRON.defaultBlockState()) {
            world.setBlockAndUpdate(toPos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
            return true;
        }
        /* TODO allow tank refilling
        BlockEntity be = world.getBlockEntity(toPos);
        if ( be != null && be.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent()){
            Optional<IFluidHandler> tankO = be.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve();
            if (tankO.isPresent()) {
                IFluidHandler tank = tankO.get();
            }
        }
         */
        return CompatUtils.isBotaniaLoaded() && BotaniaCompat.tryFillApothecary(toPos, world);
    }


    @Override
    public void onWanded(Player playerEntity) {
        this.clearPos();
        PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.connections.cleared"));
    }

    public void clearPos() {
        this.toList.clear();
        updateBlock();
    }

    @Override
    public void onFinishedConnectionFirst(@Nullable BlockPos storedPos, @Nullable LivingEntity storedEntity, Player playerEntity) {
        if (storedPos == null || !(level instanceof ServerLevel) || storedPos.equals(getBlockPos()))
            return;
        // Let relays take from us, no action needed.
        if (this.isRefillable(storedPos, level)) {
            if (this.setSendTo(storedPos.immutable())) {
                PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.connections.send", DominionWand.getPosString(storedPos)));
                ParticleUtil.beam(storedPos, worldPosition, level);
            } else {
                PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.connections.fail"));
            }
        } else {
            PortUtil.sendMessage(playerEntity, Component.translatable("ars_nouveau.connections.fail.urn"));
        }
    }

    private boolean isRefillable(BlockPos storedPos, Level level) {
        if (storedPos == null) return false;
        if (level.getBlockState(storedPos).getBlock() instanceof AbstractCauldronBlock) {
            return true;
        } else return CompatUtils.isBotaniaLoaded() && BotaniaCompat.isApothecary(storedPos, level);
    }

    public void createParticles(BlockPos from, BlockPos to) {
        if (level == null) return;
        EntityLerpedProjectile orb = new EntityLerpedProjectile(level,
                from, to,
                20, 50, 255);
        level.addFreshEntity(orb);
    }

    public boolean setSendTo(BlockPos pos) {
        if (closeEnough(pos)) {
            toList.add(pos);
            updateBlock();
            return true;
        }
        return false;
    }

    public boolean closeEnough(BlockPos pos) {
        return BlockUtil.distanceFrom(pos, this.worldPosition) <= getMaxDistance() && !pos.equals(getBlockPos());
    }

    private double getMaxDistance() {
        return 30D;
    }

    @Override
    public void getTooltip(List<Component> tooltip) {
        if (toList == null || toList.isEmpty()) {
            tooltip.add(Component.translatable("ars_nouveau.relay.no_to"));
        } else {
            tooltip.add(Component.translatable("ars_nouveau.relay.one_to", toList.size()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        toList = new HashSet<>();
        int counter = 0;

        while (NBTUtil.hasBlockPos(tag, "to_" + counter)) {
            BlockPos pos = NBTUtil.getNullablePos(tag, "to_" + counter);
            if (pos != null) this.toList.add(pos);
            counter++;
        }

    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        int counter = 0;
        for (BlockPos p : this.toList) {
            NBTUtil.storeBlockPos(tag, "to_" + counter, p);
            counter++;
        }
    }
}
