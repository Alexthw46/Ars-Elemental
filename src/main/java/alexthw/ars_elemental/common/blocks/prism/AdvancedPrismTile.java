package alexthw.ars_elemental.common.blocks.prism;

import alexthw.ars_elemental.registry.ModTiles;
import com.hollingsworth.arsnouveau.api.item.IWandable;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.ars_nouveau.geckolib3.core.IAnimatable;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationData;
import software.bernie.ars_nouveau.geckolib3.core.manager.AnimationFactory;
import software.bernie.ars_nouveau.geckolib3.util.GeckoLibUtil;

public class AdvancedPrismTile extends ModdedTile implements IWandable, IAnimatable {
    private static final String TAG_LENTS = "prismLent";
    private static final String TAG_ROTATION_X = "rotationX";
    private static final String TAG_ROTATION_Y = "rotationY";
    public float rotationX, rotationY;

    private ItemStack prismLent;

    public AdvancedPrismTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    public AdvancedPrismTile(BlockPos pos, BlockState state) {
        super(ModTiles.ADVANCED_PRISM.get(), pos, state);
    }

    public void aim(@Nullable BlockPos blockPos, Player playerEntity) {
        if (blockPos == null) return;

        Vec3 thisVec = Vec3.atCenterOf(getBlockPos());
        Vec3 blockVec = Vec3.atCenterOf(blockPos);

        Vec3 diffVec = blockVec.subtract(thisVec);
        Vec3 diffVec2D = new Vec3(diffVec.x, diffVec.z, 0);
        Vec3 rotVec = new Vec3(0, 1, 0);
        double angle = angleBetween(rotVec, diffVec2D) / Math.PI * 180.0;

        if (blockVec.x < thisVec.x) {
            angle = -angle;
        }

        setRotationX((float) angle + 90);

        rotVec = new Vec3(diffVec.x, 0, diffVec.z);
        angle = angleBetween(diffVec, rotVec) * 180F / Math.PI;
        if (blockVec.y < thisVec.y) {
            angle = -angle;
        }
        setRotationY((float) angle);

        updateBlock();
        ParticleUtil.beam(blockPos, getBlockPos(), level);
        PortUtil.sendMessageNoSpam(playerEntity, Component.literal("Prism now aims to " + blockPos.toShortString()));
    }

    public static double angleBetween(Vec3 a, Vec3 b) {
        double projection = a.normalize().dot(b.normalize());
        return Math.acos(Mth.clamp(projection, -1, 1));
    }

    @Override
    public void onWanded(Player playerEntity) {
        if (prismLent != null) {
            playerEntity.getLevel().addFreshEntity(new ItemEntity(playerEntity.level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), prismLent));
            prismLent = ItemStack.EMPTY;
        }
        updateBlock();
    }

    @Override
    public void onFinishedConnectionFirst(@Nullable BlockPos storedPos, @Nullable LivingEntity storedEntity, Player playerEntity) {
        if (storedPos != null) this.aim(storedPos, playerEntity);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat(TAG_ROTATION_Y, rotationY);
        tag.putFloat(TAG_ROTATION_X, rotationX);
        if (prismLent != null) {
            tag.put(TAG_LENTS, prismLent.serializeNBT());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        rotationX = tag.getFloat(TAG_ROTATION_X);
        rotationY = tag.getFloat(TAG_ROTATION_Y);
        if (tag.contains(TAG_LENTS)) {
            prismLent = ItemStack.of(tag.getCompound(TAG_LENTS));
        }
    }

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationX(float rot) {
        rotationX = rot;
    }

    public void setRotationY(float rot) {
        rotationY = rot;
    }


    /**
     * @return Vector for projectile shooting. Don't ask me why it works, it was pure luck.
     */
    public Vec3 getShootAngle() {
        float f = getRotationY() * ((float) Math.PI / 180F);
        float f1 = (90 + getRotationX()) * ((float) Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3(f3 * f4, -f5, f2 * f4).reverse();
    }

    final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public ItemStack getLens() {
        return prismLent == null ? ItemStack.EMPTY : prismLent;
    }

    void setLent(ItemStack lent, Player pPlayer) {
        onWanded(pPlayer);
        this.prismLent = lent;
        updateBlock();
    }
}
