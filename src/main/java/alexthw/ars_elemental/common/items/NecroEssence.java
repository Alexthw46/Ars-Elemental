package alexthw.ars_elemental.common.items;

import com.hollingsworth.arsnouveau.api.entity.ISummon;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

public class NecroEssence extends Item {

    public NecroEssence(Properties pProperties) {
        super(pProperties);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if (pInteractionTarget instanceof AbstractHorse horse && !(pInteractionTarget instanceof ISummon) && !pPlayer.level().isClientSide()) {

            AbstractHorse newHorse = null;

            // convert horse to skeleton horse, skeleton horse to zombie horse, zombie horse to horse
            switch (horse) {
                case Horse horse1 -> newHorse = EntityType.SKELETON_HORSE.create(pPlayer.level());
                case SkeletonHorse skeletonHorse -> newHorse = EntityType.ZOMBIE_HORSE.create(pPlayer.level());
                case ZombieHorse zombieHorse -> newHorse = EntityType.HORSE.create(pPlayer.level());
                default -> {
                }
            }
            if (newHorse == null) return InteractionResult.FAIL;

            // copy attributes
            if (horse.isTamed()) newHorse.tameWithName(pPlayer);
            if (horse.isSaddled()) newHorse.equipSaddle(Items.SADDLE.getDefaultInstance(),SoundSource.PLAYERS);
            if (horse.isWearingBodyArmor()) pPlayer.spawnAtLocation(horse.getItemBySlot(EquipmentSlot.CHEST));

            // copy position
            newHorse.absMoveTo(horse.getX(), horse.getY(), horse.getZ(), horse.getYRot(), horse.getXRot());

            AttributeInstance movementSpeed = newHorse.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeInstance health = newHorse.getAttribute(Attributes.MAX_HEALTH);
            AttributeInstance jumpHeight = newHorse.getAttribute(Attributes.JUMP_STRENGTH);

            movementSpeed.setBaseValue(horse.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
            health.setBaseValue(horse.getAttribute(Attributes.MAX_HEALTH).getValue());
            jumpHeight.setBaseValue(horse.getAttribute(Attributes.JUMP_STRENGTH).getValue());

            // spawn new horse
            newHorse.finalizeSpawn((ServerLevelAccessor) pPlayer.level(), pPlayer.level().getCurrentDifficultyAt(newHorse.blockPosition()), MobSpawnType.CONVERSION, null);
            newHorse.setAge(horse.getAge());
            horse.discard();
            pPlayer.level().addFreshEntity(newHorse);
            newHorse.spawnAnim();

            pStack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
