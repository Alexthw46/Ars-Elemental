package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.common.blocks.ElementalTurret;
import alexthw.ars_elemental.common.blocks.SporeBlossomGround;
import alexthw.ars_elemental.common.blocks.UpstreamBlock;
import alexthw.ars_elemental.registry.ModItems;
import com.hollingsworth.arsnouveau.common.block.ArchfruitPod;
import com.hollingsworth.arsnouveau.common.block.StrippableLog;
import com.hollingsworth.arsnouveau.common.block.SummonBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static alexthw.ars_elemental.datagen.Datagen.takeAll;

public class AEBlockStateProvider extends BlockStateProvider {

    public AEBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ArsElemental.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Set<RegistryObject<Block>> blocks = new HashSet<>(ModItems.BLOCKS.getEntries());
        takeAll(blocks, b -> b.get() instanceof UpstreamBlock || b.get() instanceof SummonBlock || b.get() instanceof ElementalTurret);
        takeAll(blocks, b -> b.get() instanceof RotatedPillarBlock || b.get() instanceof StrippableLog).forEach(this::logBlock);
        takeAll(blocks, b -> b.get() instanceof SlabBlock).forEach(this::slabBlock);
        takeAll(blocks, b -> b.get() instanceof StairBlock).forEach(this::stairsBlock);
        takeAll(blocks, b -> b.get() instanceof LeavesBlock);
        takeAll(blocks, b -> b.get() instanceof SaplingBlock);
        takeAll(blocks, b -> b.get() instanceof ArchfruitPod);
        takeAll(blocks, b -> b.get() instanceof SporeBlossomGround);
        blocks.forEach(this::basicBlock);
    }

    public void slabBlock(RegistryObject<Block> blockRegistryObject) {
        String name = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath();
        String baseName = name.substring(0, name.length() - 5);
        slabBlock((SlabBlock) blockRegistryObject.get(), ArsElemental.prefix(baseName), ArsElemental.prefix("block/" + baseName));
    }

    public void logBlock(RegistryObject<Block> blockRegistryObject) {

    }

    public void stairsBlock(RegistryObject<Block> blockRegistryObject) {
        String name = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath();
        String baseName = name.substring(0, name.length() - 7);
        stairsBlock((StairBlock) blockRegistryObject.get(), ArsElemental.prefix("block/" + baseName));
    }

    public void basicBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get());
    }


    @NotNull
    @Override
    public String getName() {
        return "Ars Elemental BlockStates";
    }

}

