package me.kevinwalker.client.gui;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.lwjgl.input.Keyboard;

import me.kevinwalker.common.ConfigLoader;
import me.kevinwalker.common.EasyLogin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.common.config.Configuration;

public class MainGui extends GuiScreen {
	private GuiScreen parentScreen;
	private static Configuration config;
	public static String tip;
	Properties prop = new Properties();

	public MainGui(GuiScreen parent) {
		parentScreen = parent; // 记下是哪个界面打开了它,以便以后返回那个界 player=this.player;
	}

	public void initGui() {
		buttonList.add(new GuiButton(0, (int) (width * 0.15), (int) (height * 0.6), 40, 20, "登陆"));
		buttonList.add(new GuiButton(1, (int) (width * 0.35), (int) (height * 0.6), 40, 20, "注册"));
		buttonList.add(new GuiButton(2, (int) (width * 0.55), (int) (height * 0.6), 40, 20, "保存"));
		buttonList.add(new GuiButton(3, (int) (width * 0.75), (int) (height * 0.6), 40, 20, "关闭"));

		playerpassword = new GuiTextField(fontRendererObj, (int) (width * 0.5) - 150, (int) (height * 0.4), 300, 20);
		playerpassword.setMaxStringLength(64); // 设置最大长度,可省略
		playerpassword.setFocused(false); // 设置是否为焦点
		playerpassword.setCanLoseFocus(true); // 设置为可以被取消焦点
	}

	public void drawScreen(int par1, int par2, float par3) {
		drawRect((int) (width * 0.1), (int) (height * 0.2), (int) (width * 0.9), (int) (height * 0.7), 0x80FFFFFF);
		super.drawScreen(par1, par2, par3);
		Keyboard.enableRepeatEvents(true);
		playerpassword.drawTextBox();
		drawCenteredString(fontRendererObj, ConfigLoader.tile, width / 2, (int) (height * 0.25), 0xFF0000);
		drawCenteredString(fontRendererObj, "§4禁止输入空格，汉字等其他奇怪的字符！！", width / 2, (int) (height * 0.35), 0x00FF00);
		drawCenteredString(fontRendererObj, "§d状态：" + this.tip, width / 2, (int) (height * 0.55), 0x00FF00);
		drawCenteredString(fontRendererObj, "§b欢迎您,玩家:§e" + Minecraft.getMinecraft().thePlayer.getCommandSenderName(),
				width / 2, (int) (height * 0.50), 0x00FF00);
	}

	@Override
	public boolean doesGuiPauseGame() {

		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		// 关闭按钮被按下
		if (button.id == 3) {
			mc.displayGuiScreen(parentScreen);
		}
		// 保存按钮被按下
		if (button.id == 2) {
			// 加密密码
			String password = playerpassword.getText();
			byte[] passwordByte = password.getBytes();
			byte passwordkey = 124;
			for (int i = 0; i < passwordByte.length; i++) {
				passwordByte[i] ^= passwordkey;
			}
			// 吸入加密后的密码
			try {
				InputStream in = new BufferedInputStream(new FileInputStream("config/easylogin.properties"));
				prop.load(in);
				Iterator<String> it = prop.stringPropertyNames().iterator();
				while (it.hasNext()) {
					String key = it.next();
					System.out.println(key + ":" + prop.getProperty(key));
				}
				FileOutputStream oFile = new FileOutputStream("config/easylogin.properties", true);
				@SuppressWarnings("resource")
				FileWriter writer = new FileWriter("config/easylogin.properties", false);
				prop.setProperty("PassWord", new String(passwordByte));
				prop.store(oFile, "A EasyLogin MOD (BY KevinWalker)");
				oFile.close();
				this.tip = "§6密码已保存!请点击登录或注册";
			} catch (Exception e) {
				System.out.println(e);
			}
			System.out.println(new String(passwordByte));
		}
		// 注册按钮被按下
		if (button.id == 1) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage(
					ConfigLoader.regcommand + " " + playerpassword.getText() + " " + playerpassword.getText());
			System.out.println("这里是框框里的内容哦" + playerpassword.getText());
			mc.displayGuiScreen(parentScreen);
		}
		// 登陆按钮被按下
		if (button.id == 0) {
			Minecraft.getMinecraft().thePlayer
					.sendChatMessage(ConfigLoader.logcommand + " " + playerpassword.getText());
			System.out.println("这里是框框里的内容哦" + playerpassword.getText());
			mc.displayGuiScreen(parentScreen);
		}
	}

	private static GuiTextField playerpassword;

	@Override
	protected void keyTyped(char par1, int par2) {
		if (playerpassword.textboxKeyTyped(par1, par2)) // 向文本框传入输入的内容
			return;
		if (mc.theWorld != null && par2 == EasyLogin.logingui.getKeyCode()) // mc.theWorld!=null是判断当前是否在游戏中
			mc.displayGuiScreen(parentScreen);
		super.keyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		playerpassword.mouseClicked(par1, par2, par3); // 调用文本框的鼠标点击检查
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false); // 关闭键盘连续输入
	}

	public static void settext(String text) {
		playerpassword.setText(text);
	}
}
