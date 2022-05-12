package alexthw.ars_elemental.mixin;

import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Zombie.class)
public interface ZombieInvoker {

    @Invoker
    void callStartUnderWaterConversion(int time);

}
