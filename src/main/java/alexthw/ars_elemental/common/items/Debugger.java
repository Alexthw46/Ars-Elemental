package alexthw.ars_elemental.common.items;

import alexthw.ars_elemental.ArsNouveauRegistry;
import alexthw.ars_elemental.common.entity.mages.*;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.hollingsworth.arsnouveau.api.spell.SpellSchools.*;

public class Debugger extends ElementalFocus {

    private final List<SpellSchool> elements = List.of(ELEMENTAL_AIR, ELEMENTAL_FIRE, ELEMENTAL_EARTH, ELEMENTAL_WATER);
    private int index;

    public Debugger(Properties properties) {
        super(properties, ArsNouveauRegistry.NECROMANCY);
        index = 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isCrouching() && !pLevel.isClientSide()) {
            index = ++index % 4;
            this.element = elements.get(index);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        if (pContext.getLevel() instanceof ServerLevel level) {
            EntityMageBase mage =
                    switch (element.getId()) {
                        case "fire" -> new FireMage(level);
                        case "air" -> new AirMage(level);
                        case "earth" -> new EarthMage(level);
                        default -> new WaterMage(level);
                    };
            mage.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
            mage.setPos(pContext.getClickLocation());
            level.addFreshEntity(mage);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

}
