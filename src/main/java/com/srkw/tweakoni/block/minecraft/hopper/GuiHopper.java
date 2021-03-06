package com.srkw.tweakoni.block.minecraft.hopper;

import com.srkw.tweakoni.network.PacketHandler;
import com.srkw.tweakoni.network.PacketUpdatePriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHopper extends GuiContainer {
    /**
     * The ResourceLocation containing the gui texture for the hopper
     */
    private static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
    /**
     * The player inventory currently bound to this GUI instance
     */
    private final IInventory playerInventory;
    /**
     * The hopper inventory bound to this GUI instance
     */
    private final IInventory hopperInventory;

    private TileEntityHopper TE;
    private String version;

    public GuiHopper(InventoryPlayer playerInv, IInventory hopperInv) {
        super(new ContainerHopper(playerInv, hopperInv, Minecraft.getMinecraft().player));
        this.playerInventory = playerInv;
        this.hopperInventory = hopperInv;
        this.TE = (TileEntityHopper) hopperInv;
        this.allowUserInput = false;
        this.ySize = 133;
    }

    public void initGui() {

        buttonList.add(new GuiButton(0, ((width + 100) / 2) - 100, ((height + 20) / 2) + 60, 100, 20, "Change Priority"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketUpdatePriority(TE.getPos()));
        }

    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (TE.getIsNew()) {
            version = "Push/Pull Hopper";
            this.fontRenderer.drawString(version, 8, 6, 4210752);
        } else {
            version = "Pull/Push Hopper (Vanilla)";
            this.fontRenderer.drawString(version, 8, 6, 4210752);
        }
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(HOPPER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

}