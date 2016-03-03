package PreferencesMain;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Extra.AllStrings;
import Menus.LobbyPreferences;
import Menus.MenuPreferences;
import Menus.PlayerPreferences;
import Menus.Listeners.LobbyMenuListener;
import Menus.Listeners.MainMenuListener;
import Menus.Listeners.PlayerMenuListener;
import Menus.Listeners.WorldListener;
import Methods.Methods1_7_R4;
import Methods.Methods1_8_R3;
import Methods.MethodsClass;

/**
 * 
 * @author lim_bo56
 *
 */

public class MainPreferences extends JavaPlugin {

	public static MethodsClass glow;
	public static MainPreferences instance;
	public static ArrayList<Player>Visibility = new ArrayList<Player>();
    public static ArrayList<Player>Speed = new ArrayList<Player>();
    public static ArrayList<Player>Jump = new ArrayList<Player>();
    public static ArrayList<Player>Fly = new ArrayList<Player>();
    public static ArrayList<Player>Chat = new ArrayList<Player>();
    public static ArrayList<Player>Stacker = new ArrayList<Player>();
    public static ArrayList<Player>Vanish = new ArrayList<Player>();
	
	  public static MainPreferences getInstance() {
		    return instance;
		  }
	  
   public void registerListeners() {
	   PluginManager pm = Bukkit.getServer().getPluginManager();
	   pm.registerEvents(new PlayerMenuListener(this), this);
	   pm.registerEvents(new MainMenuListener(this), this);
	   pm.registerEvents(new LobbyMenuListener(this), this);
	   pm.registerEvents(new WorldListener(this), this);
   }
	  
	public void onEnable() {
		instance = this;
		registerListeners();
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + 
		"*********************************");	
		getServer().getConsoleSender().sendMessage( ChatColor.AQUA + "*" + 
		ChatColor.GREEN + "Lobby&PlayerPreferences enabled" + ChatColor.AQUA + "*");
		getServer().getConsoleSender().sendMessage( ChatColor.AQUA + "*     "
				+ "                          *" );
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "*" +
		ChatColor.RED  + "         by: lim_bo56          " + ChatColor.AQUA + "*");	
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + 
		"*********************************");	
		
		saveDefaultConfig();
		
		if (setupGlow()) {

            getLogger().info("Setup was successful!");
            getLogger().info("The plugin setup process is complete!");

        } else {

            getLogger().severe("Failed to setup!");
            getLogger().severe("Your server version is not compatible with this plugin!");

            Bukkit.getPluginManager().disablePlugin(this);
        }
		}
	 
	 private boolean setupGlow() {

	        String version;

	        try {

	            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];

	        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
	            return false;
	        }

	        getLogger().info("Your server is running version " + version);

	        if (version.equals("v1_8_R3")) {
	            //server is running 1.8-1.8.1 so we need to use the 1.8 R1 NMS class
	            glow = new Methods1_8_R3();

	        } else if (version.equals("v1_7_R4")) {
	            //server is running 1.7.4 so we need to use the 1.8 R2 NMS class
	        	glow = new Methods1_7_R4();
	        }
	        // This will return true if the server version was compatible with one of our NMS classes
	        // because if it is, our title would not be null
	        return glow != null;
	    }
	 
	 public static ItemStack siLore(Material material, int amount, int shrt, String displayName, List<String> lore)
	  {
	    org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material, amount, (short)shrt);
	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(displayName);
	    meta.setLore(lore);

	    item.setItemMeta(meta);
	    return item;
	  }
	 
	 public static ItemStack nolore(Material material, int amount, int shrt, String displayName)
	  {
	    org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material, amount, (short)shrt);
	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(displayName);

	    item.setItemMeta(meta);
	    return item;
	  }
	 
	 public static ItemStack Glass(ItemStack stack, String displayName)
	  {
	    org.bukkit.inventory.ItemStack item = stack;
	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(displayName);

	    item.setItemMeta(meta);
	    return item;
	  }
	 
	 
	 @Override
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		 
		 String world = p.getWorld().getName();		
			
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) { 
		 if(label.equalsIgnoreCase("preferences")) {
			 if(args.length == 0) {
			MenuPreferences.openPreferencesMenu(p);
			 } else if(args.length == 1 ) {
				 if(args[0].equals("reload")) {
					 this.reloadConfig();
				 }
			 }
		}
		}
		 }
		 
		 for(String m : AllStrings.World) {
				if(world.equalsIgnoreCase(m)) { 
			 if(label.equalsIgnoreCase("pref")) {
				MenuPreferences.openPreferencesMenu(p);
			}
			}
			 }
		 
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) { 
		 if(label.equalsIgnoreCase("preferencesLobby")) {
			 LobbyPreferences.openLobbyPreferences(p);
		 }
		}
		 }
		 
		 for(String m : AllStrings.World) {
		if(world.equalsIgnoreCase(m)) { 
		 if(label.equalsIgnoreCase("preferencesPlayer")) {
			 PlayerPreferences.openPlayerPreferences(p);
		 }
		} 
		 }
		 
		 return false;	 
	 }
}
