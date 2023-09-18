package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = ArsElemental.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Datagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        PackOutput output = gen.getPackOutput();

        gen.addProvider(event.includeClient(), new AEBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(event.includeClient(), new AEItemModelProvider(gen, existingFileHelper));
        BlockTagsProvider BTP = new AETagsProvider.AEBlockTagsProvider(gen, provider, existingFileHelper);
        gen.addProvider(event.includeServer(), new AEDamageTypesProvider(output, provider));
        gen.addProvider(event.includeServer(), BTP);
        gen.addProvider(event.includeServer(), new AETagsProvider.AEMobEffectTagProvider(gen, provider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AETagsProvider.AEItemTagsProvider(gen, provider, BTP, existingFileHelper));
        gen.addProvider(event.includeServer(), new AETagsProvider.AEEntityTagProvider(gen, provider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AETagsProvider.AEDamageTypeProvider(gen, provider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModRecipeProvider(gen));
        gen.addProvider(event.includeServer(), new AELootTables(gen));

        gen.addProvider(event.includeServer(), new AEImbuementProvider(gen));
        gen.addProvider(event.includeServer(), new AEGlyphProvider(gen));
        gen.addProvider(event.includeServer(), new AEApparatusProvider(gen));

        gen.addProvider(event.includeServer(), new AEPatchouliProvider(gen));
        gen.addProvider(event.includeServer(), new AEAdvancementsProvider(output, provider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AECasterTomeProvider(gen));


        gen.addProvider(event.includeServer(), new AEWorldgenProvider(output, provider));
        gen.addProvider(event.includeServer(), new AETagsProvider.AEFeatureTagsProvider(gen, provider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AETagsProvider.AEBiomeTagsProvider(gen, provider, existingFileHelper));

    }

    public static <T> Collection<T> takeAll(Collection<T> src, Predicate<T> predicate) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (predicate.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            return Collections.emptyList();
        }
        return ret;
    }

}
