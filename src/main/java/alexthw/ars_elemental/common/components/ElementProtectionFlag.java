package alexthw.ars_elemental.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record ElementProtectionFlag(boolean flag) implements TooltipProvider {
    public static Codec<ElementProtectionFlag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("flag").forGetter(ElementProtectionFlag::flag)).apply(instance, ElementProtectionFlag::new));

    public static StreamCodec<RegistryFriendlyByteBuf, ElementProtectionFlag> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, ElementProtectionFlag::flag, ElementProtectionFlag::new);

    public ElementProtectionFlag() {
        this(false);
    }

    @Override
    public void addToTooltip(Item.@NotNull TooltipContext pContext, @NotNull Consumer<Component> pTooltipAdder, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipAdder.accept(Component.translatable("tooltip.ars_nouveau.blessed").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GREEN)));
    }
}

