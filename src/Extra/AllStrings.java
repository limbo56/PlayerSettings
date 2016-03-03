package Extra;

import java.util.ArrayList;
import java.util.List;


import PreferencesMain.MainPreferences;

/**
 * 
 * @author lim_bo56
 *
 */

public class AllStrings {

	//General\\
	public static String NoPermissions = MainPreferences.getInstance().getConfig().getString("Messages." + "no-permissions").replace("&", "§");
    public static String PlayerStackerDisabled = MainPreferences.getInstance().getConfig().getString("Messages." + "player-stacker-disabled").replace("&", "§");
    public static String TargetStackerDisabled = MainPreferences.getInstance().getConfig().getString("Messages." + "target-stacker-disabled").replace("&", "§");
    public static String ChatDisabled = MainPreferences.getInstance().getConfig().getString("Messages." + "chat-disabled").replace("&", "§");
    public static String BackItemName = MainPreferences.getInstance().getConfig().getString("GoBack." + "BackItemName").replace("&", "§");
    public static String BackItem = MainPreferences.getInstance().getConfig().getString("GoBack." + "BackItem");
    public static ArrayList<String> World = (ArrayList<String>) MainPreferences.getInstance().getConfig().getStringList("Worlds");
	//General\\
	
    //PlayerPreferences\\
    public static String PlayerPreferencesName = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "Name").replace("&", "§");
   
    //Speed Item\\
    public static String SpeedItemName = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "SpeedItemName").replace("&", "§");
    public static String SpeedItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "SpeedItem").toUpperCase();
    //Speed Item\\
    
    //Jump Item\\
    public static String JumpItemName = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "JumpItemName").replace("&", "§");
    public static String JumpItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "JumpItem").toUpperCase();
    //Jump Item\\
    
	//Fly Item\\
    public static String FlyItemName = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "FlyItemName").replace("&", "§");
    public static String FlyItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "FlyItem").toUpperCase();
	//Fly Item\\ 
    
    //Vanish Item\\
    public static String VanishItemName = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "VanishItemName").replace("&", "§");
    public static String VanishItem = MainPreferences.getInstance().getConfig().getString("PlayerPreferences." + "VanishItem").toUpperCase();
    //Vanish Item\\
    
    //Player Preferences\\
    
    //MenuPreferences\\
    public static String MenuPreferencesName = MainPreferences.getInstance().getConfig().getString("MainMenu." + "Name").replace("&", "§");
    public static String PlayerPreferencesMenuName = MainPreferences.getInstance().getConfig().getString("MainMenu." + "PlayerPreferencesItemName").replace("&", "§");
    public static String LobbyPreferencesMenuName = MainPreferences.getInstance().getConfig().getString("MainMenu." + "LobbyPreferencesItemName").replace("&", "§");
    public static String PlayerPreferencesMenuItem = MainPreferences.getInstance().getConfig().getString("MainMenu." + "PlayerPreferencesItem").toUpperCase();
    public static String LobbyPreferencesMenuItem = MainPreferences.getInstance().getConfig().getString("MainMenu." + "LobbyPreferencesItem").toUpperCase();
    
    //MenuPreferences\\
    
    //LobbyPreferences\\
    public static String LobbyPreferencesName = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "Name").replace("&", "§");
    
    //Stacker\\
    public static String StackerItemName = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "StackerItemName").replace("&", "§");
    public static String StackerItem = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "StackerItem").toUpperCase();
    //Stacker\\
    
    //Visibility\\
    public static String VisibilityItemName = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "VisibilityItemName").replace("&", "§");
    public static String VisibilityItem = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "VisibilityItem").toUpperCase();
    //Visibility\\
    
    //Chat\\
    public static String ChatItemName = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "ChatItemName").replace("&", "§");
    public static String ChatItem = MainPreferences.getInstance().getConfig().getString("LobbyPreferences." + "ChatItem").toUpperCase();
    //Chat\\
    
    //LobbyPreferences\\
  
    //Ink Sack\\
    public static String On = MainPreferences.getInstance().getConfig().getString("DyeItem." + "enabled").toString().replace("&", "§");
    public static String OnLore = MainPreferences.getInstance().getConfig().getString("DyeItem." + "enabled-lore").toString().replace("&", "§");
    public static String Off = MainPreferences.getInstance().getConfig().getString("DyeItem." + "disabled").toString().replace("&", "§");
    public static String OffLore = MainPreferences.getInstance().getConfig().getString("DyeItem." + "disabled-lore").toString().replace("&", "§");
    //Ink Sack\\
    
   
}
