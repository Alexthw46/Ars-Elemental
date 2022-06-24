package alexthw.ars_elemental.util;

import alexthw.ars_elemental.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.world.tree.AbstractSupplierBlockStateProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SupplierBlockStateProviderAE extends AbstractSupplierBlockStateProvider {
    public SupplierBlockStateProviderAE(String key) {
        super(prefix(key));
    }

    @Override
    protected BlockStateProviderType<?> type() {
        return ModRegistry.AE_BLOCKSTATE_PROVIDER.get();
    }

    public static final Codec<SupplierBlockStateProviderAE> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(d -> d.key.getPath()))
            .apply(instance, SupplierBlockStateProviderAE::new));

}
