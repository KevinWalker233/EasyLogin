package me.kevinwalker.common.event;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.kevinwalker.client.gui.MainGui;
import me.kevinwalker.common.EasyLogin;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

public class JoinEvent {
	public static boolean guiopendnumber = true;
	public static EntityPlayer player;
	Properties prop = new Properties();

	public JoinEvent() {
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void PlayerJoinGame(RenderGameOverlayEvent.Pre event) throws IOException, Exception {
		Minecraft mc = Minecraft.getMinecraft();
		File file = new File("config/easylogin.properties");
		if (!file.exists()) {
			try {
				file.createNewFile();
				String str = "PassWord=";
				FileWriter writer;
				try {
					writer = new FileWriter("config/easylogin.properties");
					writer.write(str);
					writer.flush();
					writer.close();
					MainGui.tip = "密码未保存！";
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (guiopendnumber) {
			mc.displayGuiScreen(new MainGui(mc.currentScreen));
			// 密码读取
			InputStream in = new BufferedInputStream(new FileInputStream("config/easylogin.properties"));
			prop.load(in);
			// 解密密码
			byte[] passwordByte = prop.getProperty("PassWord").getBytes();
			byte key = 124;
			for (int i = 0; i < passwordByte.length; i++) {
				passwordByte[i] ^= key;
			}
			MainGui.settext(new String(passwordByte));
		}
		this.guiopendnumber = false;
	}

	@SubscribeEvent
	public void keyListener(KeyInputEvent event) throws IOException {
		if (EasyLogin.logingui.isPressed()) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(new MainGui(mc.currentScreen));
			// 密码读取
			InputStream in = new BufferedInputStream(new FileInputStream("config/easylogin.properties"));
			prop.load(in);
			// 解密密码
			byte[] passwordByte = prop.getProperty("PassWord").getBytes();
			byte key = 124;
			for (int i = 0; i < passwordByte.length; i++) {
				passwordByte[i] ^= key;
			}
			MainGui.settext(new String(passwordByte));
			System.out.println(new String(passwordByte));
		}
	}
}
