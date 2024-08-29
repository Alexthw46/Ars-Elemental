package alexthw.ars_elemental.recipe;

import alexthw.ars_elemental.ConfigHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record ConfigCondition(String configPath) implements ICondition {
    public static final MapCodec<ConfigCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("config").forGetter(ConfigCondition::configPath)
    ).apply(instance, ConfigCondition::new));

    @Override
    public String toString() {
        return "config(\"" + configPath + "\")";
    }

    @Override
    public boolean test(@NotNull IContext iContext) {
        ModConfigSpec.BooleanValue config = ConfigHandler.COMMON_SPEC.getValues().get(this.configPath);
        return config.get();
    }

    @Override
    public @NotNull MapCodec<ConfigCondition> codec() {
        return CODEC;
    }
}