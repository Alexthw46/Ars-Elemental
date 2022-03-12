package alexthw.ars_elemental.common.blocks.mermaid_block;

import alexthw.ars_elemental.ModRegistry;
import alexthw.ars_elemental.common.entity.MermaidEntity;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.client.ITooltipProvider;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.SummoningTile;
import com.hollingsworth.arsnouveau.common.entity.EntityFollowProjectile;
import com.hollingsworth.arsnouveau.setup.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO make it working
public class MermaidTile extends SummoningTile implements ITooltipProvider {

    public int progress;
    public int bonus;
    public boolean needsMana;
    private List<LivingEntity> nearbyEntities;

    public MermaidTile(BlockPos pos, BlockState state) {
        super(ModRegistry.MERMAID_TILE, pos, state);
    }

    public void convertedEffect() {
        super.convertedEffect();
        if (level instanceof ServerLevel world) {
            if (tickCounter >= 120) {
                converted = true;
                level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(SummoningTile.CONVERTED, true));
                MermaidEntity mermaid = new MermaidEntity(level, true);
                mermaid.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5);
                mermaid.homePos = new BlockPos(getBlockPos());
                level.addFreshEntity(mermaid);
                ParticleUtil.spawnPoof(world, worldPosition.above());
                tickCounter = 0;
                return;
            }

            if (tickCounter % 10 == 0) {
                Random r = level.random;
                int min = -2;
                int max = 2;
                EntityFollowProjectile proj1 = new EntityFollowProjectile(level, worldPosition.offset(r.nextInt(max - min) + min, 3, r.nextInt(max - min) + min), worldPosition, r.nextInt(255), r.nextInt(255), r.nextInt(255));
                level.addFreshEntity(proj1);
            }
        }

    }

    public int getMaxProgress(){
        return Config.DRYGMY_MAX_PROGRESS.get();
    }

    public void giveProgress(){
        if(progress < getMaxProgress()){
            progress += 1;
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        }

    }

    public void refreshEntitiesAndBonus(){

    }
    public void generateItems(){
        List<ItemStack> stacks = new ArrayList<>();
        ANFakePlayer fakePlayer = ANFakePlayer.getPlayer((ServerLevel) level);
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null) return;
        if (level.isClientSide()){
            for(int i = 0; i < progress/2; i++){
                level.addParticle(
                        GlowParticleData.createData(new ParticleColor(
                                50,
                                255,
                                20
                        )),
                        getBlockPos().getX() +0.5 + ParticleUtil.inRange(-0.1, 0.1)  , getBlockPos().getY() + 1  + ParticleUtil.inRange(-0.1, 0.1) , getBlockPos().getZ() +0.5 + ParticleUtil.inRange(-0.1, 0.1),
                        0,0,0);
            }

        }else {

            if (level.getGameTime() % 100 == 0) {
                refreshEntitiesAndBonus();
            }

            if (level.getGameTime() % 80 == 0 && needsMana && SourceUtil.takeSourceNearbyWithParticles(worldPosition, level, 7, Config.DRYGMY_MANA_COST.get()) != null) {
                this.needsMana = false;
                level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
            }

            if (level.getGameTime() % 100 == 0 && !needsMana && progress >= getMaxProgress()) {
                generateItems();
            }
        }
    }

    /**
     * A list of tool tips to render on the screen when looking at this target.
     */
    @Override
    public void getTooltip(List<Component> tooltip) {
        if(this.needsMana){
            tooltip.add(new TranslatableComponent("ars_nouveau.wixie.need_mana"));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        this.progress = compound.getInt("progress");
        this.bonus = compound.getInt("bonus");
        this.needsMana = compound.getBoolean("needsMana");
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("progress", progress);
        tag.putInt("bonus", bonus);
        tag.putBoolean("needsMana", needsMana);
    }

}
