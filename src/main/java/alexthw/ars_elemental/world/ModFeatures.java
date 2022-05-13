package alexthw.ars_elemental.world;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFeatures {

    //placeholders since we do things only in jsons for now
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ArsElemental.MODID);
    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ArsElemental.MODID);

}
