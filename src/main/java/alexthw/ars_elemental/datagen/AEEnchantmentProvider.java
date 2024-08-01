package alexthw.ars_elemental.datagen;

import alexthw.ars_elemental.ArsElemental;
import alexthw.ars_elemental.registry.ModRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AEEnchantmentProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, AEEnchantmentProvider::bootstrap);

    public AEEnchantmentProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
        super(output, registries, datapackEntriesBuilder, modIds);
    }


    public static void bootstrap(BootstrapContext<Enchantment> ctx) {
        HolderGetter<Item> holdergetter2 = ctx.lookup(Registries.ITEM);
        register(ctx, ModRegistry.SOULBOUND, Enchantment.enchantment(Enchantment.definition(
                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                5,
                3,
                Enchantment.dynamicCost(1, 11),
                Enchantment.dynamicCost(12, 11),
                1,
                EquipmentSlotGroup.ANY
        )));
        register(ctx, ModRegistry.MIRROR, Enchantment.enchantment(Enchantment.definition(
                holdergetter2.getOrThrow(Tags.Items.TOOLS_SHIELD),
                5,
                3,
                Enchantment.dynamicCost(1, 25),
                Enchantment.dynamicCost(50, 25),
                1,
                EquipmentSlotGroup.OFFHAND
        )));
    }

    protected static void register(BootstrapContext<Enchantment> ctx, ResourceKey<Enchantment> enchantment, Enchantment.Builder builder) {
        ctx.register(enchantment, builder.build(enchantment.location()));
    }

    public AEEnchantmentProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ArsElemental.MODID));
    }

    @Override
    @NotNull
    public String getName() {
        return "Ars Elemental's Enchantment Data";
    }
}
