package alexthw.ars_elemental.client;

import alexthw.ars_elemental.common.CurioHolderContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class CurioHolderScreen<T extends CurioHolderContainer> extends AbstractContainerScreen<T> {

    public final ResourceLocation BACKGROUND;

    public CurioHolderScreen(T screenContainer, Inventory inv, Component titleIn, ResourceLocation background, int xSize, int ySize) {
        super(screenContainer, inv, titleIn);
        BACKGROUND = background;
        imageWidth = xSize;
        imageHeight = ySize;
    }

    public CurioHolderScreen(T screenContainer, Inventory inv, Component titleIn, ResourceLocation background) {
        super(screenContainer, inv, titleIn);
        BACKGROUND = background;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int x, int y) {
        gui.blit(BACKGROUND, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {

    }


    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        renderBackground(gui, mouseX, mouseY, partialTicks);
        super.render(gui, mouseX, mouseY, partialTicks);
        renderTooltip(gui, mouseX, mouseY);
    }


    @Override
    protected void renderTooltip(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            gui.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, mouseX, mouseY);
        }
    }

}
