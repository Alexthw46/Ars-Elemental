package alexthw.ars_elemental.common.items.armor;

import alexthw.ars_elemental.registry.ModRegistry;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.Perk;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

import static alexthw.ars_elemental.ArsElemental.prefix;

public class SummonPerk extends Perk {

    public static final UUID PERK_UUID = UUID.fromString("3923ee66-4b1d-b216-756d-bb9338b0315b");

    public static SummonPerk INSTANCE = new SummonPerk();

    public SummonPerk() {
        super(prefix("thread_summon"));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getModifiers(EquipmentSlot pEquipmentSlot, ItemStack stack, int slotValue) {
        return attributeBuilder().put(ModRegistry.SUMMON_POWER.get(), new AttributeModifier(PERK_UUID, "SummonPower", slotValue, AttributeModifier.Operation.ADDITION)).build();
    }
}
