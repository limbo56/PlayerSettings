package me.lim_bo56.lnpp.utils;

import java.util.ArrayList;

import me.lim_bo56.lnpp.MainPreferences;

/**
 * 
 * @author lim_bo56
 *
 */

public class AllStrings {

	private static AllStrings instance = new AllStrings();

	public static AllStrings getInstance() {
		return instance;
	}

	// General\\
	public String NoPermissions = MainPreferences.getInstance().getConfig().getString("Messages." + "no-permissions")
			.replace("&", "§");
	public String PlayerStackerDisabled = MainPreferences.getInstance().getConfig()
			.getString("Messages." + "player-stacker-disabled").replace("&", "§");
	public String TargetStackerDisabled = MainPreferences.getInstance().getConfig()
			.getString("Messages." + "target-stacker-disabled").replace("&", "§");
	public String ChatDisabled = MainPreferences.getInstance().getConfig().getString("Messages." + "chat-disabled")
			.replace("&", "§");
	public boolean UpdateMSG = MainPreferences.getInstance().getConfig().getBoolean("Messages." + "update-msg");
	// General\\

	// PlayerPreferences\\
	public String PlayerPreferencesName = MainPreferences.getInstance().getConfig()
			.getString("PlayerPreferences." + "Name").replace("&", "§");

	// Speed Item\\
	public String SpeedItemName = MainPreferences.getInstance().getConfig()
			.getString("PlayerPreferences." + "SpeedItemName").replace("&", "§");
	public String SpeedItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "SpeedItem")
			.toUpperCase();
			// Speed Item\\

	// Jump Item\\
	public String JumpItemName = MainPreferences.getInstance().getConfig()
			.getString("PlayerPreferences." + "JumpItemName").replace("&", "§");
	public String JumpItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "JumpItem")
			.toUpperCase();
			// Jump Item\\

	// Fly Item\\
	public String FlyItemName = MainPreferences.getInstance().getConfig()
			.getString("PlayerPreferences." + "FlyItemName").replace("&", "§");
	public String FlyItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "FlyItem")
			.toUpperCase();
			// Fly Item\\

	// Vanish Item\\
	public String VanishItemName = MainPreferences.getInstance().getConfig()
			.getString("PlayerPreferences." + "VanishItemName").replace("&", "§");
	public String VanishItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "VanishItem")
			.toUpperCase();
			// Vanish Item\\

	// Player Preferences\\

	// MenuPreferences\\
	public String MenuPreferencesName = MainPreferences.getInstance().getConfig().getString("MainMenu." + "Name")
			.replace("&", "§");
	public String PlayerPreferencesMenuName = MainPreferences.getInstance().getConfig()
			.getString("MainMenu." + "PlayerPreferencesItemName").replace("&", "§");
	public String LobbyPreferencesMenuName = MainPreferences.getInstance().getConfig()
			.getString("MainMenu." + "LobbyPreferencesItemName").replace("&", "§");
	public String PlayerPreferencesMenuItem = MainPreferences.getInstance().getConfig()
			.getString("MainMenu." + "PlayerPreferencesItem").toUpperCase();
	public String LobbyPreferencesMenuItem = MainPreferences.getInstance().getConfig()
			.getString("MainMenu." + "LobbyPreferencesItem").toUpperCase();

	// MenuPreferences\\

	// LobbyPreferences\\
	public String LobbyPreferencesName = MainPreferences.getInstance().getConfig()
			.getString("LobbyPreferences." + "Name").replace("&", "§");

	// Stacker\\
	public String StackerItemName = MainPreferences.getInstance().getConfig()
			.getString("LobbyPreferences." + "StackerItemName").replace("&", "§");
	public String StackerItem = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "StackerItem")
			.toUpperCase();
			// Stacker\\

	// Visibility\\
	public String VisibilityItemName = MainPreferences.getInstance().getConfig()
			.getString("LobbyPreferences." + "VisibilityItemName").replace("&", "§");
	public String VisibilityItem = MainPreferences.getInstance().getConfig()
			.getString("LobbyPreferences." + "VisibilityItem").toUpperCase();
			// Visibility\\

	// Chat\\
	public String ChatItemName = MainPreferences.getInstance().getConfig()
			.getString("LobbyPreferences." + "ChatItemName").replace("&", "§");
	public String ChatItem = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "ChatItem")
			.toUpperCase();
			// Chat\\

	// LobbyPreferences\\

	// Ink Sack\\
	public String On = MainPreferences.getInstance().getConfig().getString("DyeItem." + "enabled").toString()
			.replace("&", "§");
	public String OnLore = MainPreferences.getInstance().getConfig().getString("DyeItem." + "enabled-lore").toString()
			.replace("&", "§");
	public String Off = MainPreferences.getInstance().getConfig().getString("DyeItem." + "disabled").toString()
			.replace("&", "§");
	public String OffLore = MainPreferences.getInstance().getConfig().getString("DyeItem." + "disabled-lore").toString()
			.replace("&", "§");
			// Ink Sack\\

	// Back Item\\
	public String BackItemName = MainPreferences.getInstance().getConfig().getString("GoBack." + "BackItemName")
			.replace("&", "§");
	public String BackItem = MainPreferences.getInstance().getConfig().getString("GoBack." + "BackItem");
	public boolean PreformCMD = MainPreferences.getInstance().getConfig().getBoolean("GoBack." + "prefrom-cmd");
	public String LobbyPreferencesCMD = MainPreferences.getInstance().getConfig()
			.getString("GoBack." + "LobbyPreferences-cmd");
	public String PlayerPreferencesCMD = MainPreferences.getInstance().getConfig()
			.getString("GoBack." + "PlayerPreferences-cmd");
			// Back Item\\

	// Worlds\\
	public ArrayList<String> World = (ArrayList<String>) MainPreferences.getInstance().getConfig()
			.getStringList("Worlds");
	// Worlds\\

}
