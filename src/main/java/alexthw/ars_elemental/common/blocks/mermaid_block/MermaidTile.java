package alexthw.ars_elemental.common.blocks.mermaid_block;

import alexthw.ars_elemental.ConfigHandler;
import alexthw.ars_elemental.common.entity.MermaidEntity;
import alexthw.ars_elemental.registry.ModEntities;
import com.hollingsworth.arsnouveau.api.ANFakePlayer;
import com.hollingsworth.arsnouveau.api.client.ITooltipProvider;
import com.hollingsworth.arsnouveau.api.util.BlockUtil;
import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.SummoningTile;
import com.hollingsworth.arsnouveau.common.entity.EntityFlyingItem;
import com.hollingsworth.arsnouveau.common.entity.EntityFollowProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;


public class MermaidTile extends SummoningTile implements ITooltipProvider {

    public int progress;
    public int bonus;
    public boolean needsMana;

    public MermaidTile(BlockPos pos, BlockState state) {
        super(ModEntities.MERMAID_TILE, pos, state);
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
        return ConfigHandler.Common.SIREN_MAX_PROGRESS.get();
    }

    public void giveProgress(){
        if(progress < getMaxProgress() && level != null){
            progress += 1;
            level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        }

    }

    //TODO make it balanced
    public void refreshEntitiesAndBonus(){

    }

    ItemStack getRod(){ return Items.FISHING_ROD.getDefaultInstance();}

    public void generateItems(){
        if (!(this.level instanceof ServerLevel server)) return;
        List<ItemStack> list;

        ANFakePlayer fakePlayer = ANFakePlayer.getPlayer(server);
        DamageSource damageSource = DamageSource.playerAttack(fakePlayer);
        ItemStack rod = getRod();
        rod.enchant(Enchantments.FISHING_LUCK, 2);
        LootTable lootTable = server.getServer().getLootTables().get(BuiltInLootTables.FISHING);
        LootContext lootContext = (new LootContext.Builder(server)).withRandom(level.getRandom())
                .withParameter(LootContextParams.ORIGIN, fakePlayer.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withParameter(LootContextParams.TOOL, rod)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, fakePlayer)
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, damageSource.getDirectEntity())
                .withLuck(fakePlayer.getLuck()+5).create(LootContextParamSets.FISHING);

        list = lootTable.getRandomItems(lootContext);

        for (ItemStack item : list){
            BlockUtil.insertItemAdjacent(level, worldPosition, item);
        }

        this.progress = 0;
        this.needsMana = true;
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null) return;
        if (level.isClientSide()){
            for(int i = 0; i < progress/2; i++){
                level.addParticle(
                        GlowParticleData.createData(new ParticleColor(20,50, 255),i/2F * 0.25f, i/2F * 0.75f,20),
                        getBlockPos().getX() + 0.5 + ParticleUtil.inRange(-0.1, 0.1),
                        getBlockPos().getY() + 1.5  + ParticleUtil.inRange(-0.1, 0.1),
                        getBlockPos().getZ() + 0.5 + ParticleUtil.inRange(-0.1, 0.1),
                        0,0,0);
            }
        }else {

            long gameTime = level.getGameTime();
            if (gameTime % 100 == 0) {
                refreshEntitiesAndBonus();
            }

            if (gameTime % 80 == 0 && needsMana && SourceUtil.takeSourceNearbyWithParticles(worldPosition, level, 7, ConfigHandler.Common.SIREN_MANA_COST.get()) != null) {
                this.needsMana = false;
                level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
            }

            if (gameTime % 20 == 0 && !needsMana){
                if (progress >= getMaxProgress()) {
                    generateItems();
                }else{
                    giveProgress();
                    //TODO move this to Mermaid Goal
                    List<MermaidEntity> mermaids = level.getEntitiesOfClass(MermaidEntity.class, new AABB(getBlockPos().north(6).west(6).below(6), getBlockPos().south(6).east(6).above(6)));
                    if (!mermaids.isEmpty()){
                        int rand = level.random.nextInt(0, mermaids.size());

                        LivingEntity mermaid = mermaids.get(rand);
                        EntityFlyingItem item = new EntityFlyingItem(level,
                                 mermaid.blockPosition().above(), getBlockPos(),
                                20, 50,  255);
                        level.addFreshEntity(item);
                    }
                }
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
        }else {
            tooltip.add(new TranslatableComponent("progress: " + progress));
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
