package alexthw.ars_elemental.mixin;

import net.minecraft.world.entity.animal.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(Fox.class)
public interface FoxInvoker {

    @Invoker
    boolean callTrusts(UUID uuid);

    @Invoker
    void callAddTrustedUUID(UUID uuid);

}
