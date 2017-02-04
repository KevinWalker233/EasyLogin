package me.kevinwalker.common;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigLoader {
	private static Configuration config;
	public static String logcommand;
	public static String regcommand;
	public static String tile;

	public ConfigLoader(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		load();
	}

	public static void load() {
		logcommand = config.getString("指令", "登陆", "/login", "登陆的指令");
		regcommand = config.getString("指令", "注册", "/register", "注册的指令");
		tile = config.getString("标题", "窗口标题", "§a便捷登录系统：§c请在下方输入密码", "窗口标题");
		config.save();
	}
}
