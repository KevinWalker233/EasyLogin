package me.kevinwalker.common;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.kevinwalker.common.event.JoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = EasyLogin.MODID, name = EasyLogin.NAME, version = EasyLogin.VERSION, acceptedMinecraftVersions = "1.7.10")
public class EasyLogin {
	public static final String MODID = "easylogin";
	public static final String NAME = "easylogin";
	public static final String VERSION = "1.0.0";
	@Instance(EasyLogin.MODID)
	public static EasyLogin instance;

	Minecraft mc = Minecraft.getMinecraft();
	public static final KeyBinding logingui = new KeyBinding("easylogin.key.name", Keyboard.KEY_L, "keyname");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		new ConfigLoader(event);
		ClientRegistry.registerKeyBinding(logingui);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		new JoinEvent();
		MinecraftForge.EVENT_BUS.register(this);
	}
}
