package alexthw.ars_elemental.bag;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

import static alexthw.ars_elemental.datagen.Datagen.prefix;

public class CurioHolderScreen extends ContainerScreen<CurioHolderContainer> {

    public static final ResourceLocation BACKGROUND = prefix("textures/gui/curio_bag.png");

    public CurioHolderScreen(CurioHolderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {

    }

    @Override
    public void renderBg(MatrixStack matrixStack,  float partialTicks, int x, int y) {
        if (minecraft == null) return;
        minecraft.getTextureManager().bind(BACKGROUND);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
