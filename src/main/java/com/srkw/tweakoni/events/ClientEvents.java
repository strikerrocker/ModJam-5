package com.srkw.tweakoni.events;

import org.lwjgl.input.Keyboard;

import com.srkw.tweakoni.network.PacketHandler;
import com.srkw.tweakoni.network.PacketItemRotate;
import com.srkw.tweakoni.network.PacketSendLoc;
import com.srkw.tweakoni.proxy.ClientProxy;
import com.srkw.tweakoni.utils.RayTrace;
import com.srkw.tweakoni.utils.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@SuppressWarnings("all")
@Mod.EventBusSubscriber
public class ClientEvents
{

    private static Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
    	
    	EntityPlayer p = mc.player;
    	
    	if(ClientProxy.d_shift.isKeyDown() && p.onGround && !p.isElytraFlying() && !p.isDead && !p.isRiding() && !p.isPlayerSleeping()) {
    		mc.gameSettings.keyBindSneak.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), !mc.player.isSneaking());
    	}
    	
    }
	
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getWorld().isRemote)
        {
            Item item = event.getItemStack().getItem();
            if (item instanceof ItemBlock && ClientProxy.block_below.isKeyDown())
            {
                RayTraceResult result = RayTrace.rayTrace(event.getWorld(), event.getEntityPlayer(), false);
                if (result.sideHit == EnumFacing.UP)
                {
                    PacketHandler.INSTANCE.sendToServer(new PacketSendLoc());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        if (event.getWorld().isRemote && event.getTarget() instanceof EntityItemFrame)
        {
            event.setCanceled(true);
            if (ClientProxy.item_frame.isKeyDown())
            {
                EntityItemFrame frame = (EntityItemFrame) event.getTarget();
                PacketHandler.INSTANCE.sendToServer(new PacketItemRotate());
            }
        }
    }

    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent.Post event)
    {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT))
        {
            ScaledResolution resolution = event.getResolution();
            int x = resolution.getScaledWidth() / 100;
            int y = resolution.getScaledHeight() / 100;
            for (ItemStack stack : mc.player.inventory.armorInventory)
            {
                if (stack.getItem() instanceof ItemElytra)
                {
                    int i = stack.getMaxDamage() - stack.getItemDamage();
                    String text = "Elytra : " + i + " / " + stack.getMaxDamage();
                    int x1;
                    int y1;
                    switch (ConfigHandler.elytraPos)
                    {
                        case 1:
                            x1 = x / 95;
                            y1 = y / 100;
                            mc.fontRenderer.drawStringWithShadow(text, x1, y1, 0xffffff);
                            break;
                        case 2:
                            x1 = x / 100;
                            y1 = y * 100;
                            mc.fontRenderer.drawStringWithShadow(text, x1, y1, 0xffffff);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
