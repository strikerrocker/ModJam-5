package com.srkw.tweakoni.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static KeyBinding d_shift = new KeyBinding("key.toggle.d_shift", Keyboard.KEY_O, "key.tweaktoni.category");
    public static KeyBinding block_below = new KeyBinding("key.toggle.bb", Keyboard.KEY_LMENU, "key.tweaktoni.category");
    public static KeyBinding item_frame = new KeyBinding("key.toggle.frame", Keyboard.KEY_B, "key.tweaktoni.category");
    public static KeyBinding decrease_gamma = new KeyBinding("key.toggle.dgamma", Keyboard.KEY_ADD, "key.tweaktoni.category");
    public static KeyBinding increase_gamma = new KeyBinding("key.toggle.igamma", Keyboard.KEY_SUBTRACT, "key.tweaktoni.category");

    public void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(d_shift);
        ClientRegistry.registerKeyBinding(block_below);
        ClientRegistry.registerKeyBinding(item_frame);
        ClientRegistry.registerKeyBinding(decrease_gamma);
        ClientRegistry.registerKeyBinding(increase_gamma);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}