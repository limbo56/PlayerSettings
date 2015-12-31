package Menu;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import LobbyPlayerMain.Main;

public class PreferencesMenu implements Listener{

static Main plugin;
	
	public PreferencesMenu(Main instance) {
	    this.plugin = instance;
	  }
	public static String blank = "";
	public static String infoPlayer = "§7Set the preferences to your liking"; 
	public static String infoPlayer2 = "§7in the player preferences";
	public static String infoPlayer3 = "§7so you can enjoy the game more!";
	
	public static String infoLobby = "§7Set the preferences to your liking"; 
	public static String infoLobby2 = "§7in the lobby preferences";
    public static String infoLobby3 = "§7so you can enjoy the game more!";
	
	
	public static void openPREF(Player p) 
	{
		if(plugin.getConfig().getBoolean("per-world")== true) {
		World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
		if(p.getWorld()== world) {
		Inventory Preferences = Bukkit.createInventory(null, 27, plugin.getConfig().getString("preferences-menu-name").replace("&", "§"));
		
		ItemStack decoration = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getWoolData());
		ItemStack decoration2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getWoolData());
		ItemStack decoration3 = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getWoolData());
		ItemStack decoration4 = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getWoolData());
		
		ItemStack player = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta playerM = (SkullMeta) player.getItemMeta();
		playerM.setOwner(p.getName());
		playerM.setDisplayName(plugin.getConfig().getString("player-options-name").replace("&", "§"));
		player.setItemMeta(playerM);
		
		ItemStack lobby = new ItemStack(Material.BEACON);
		ItemMeta lobbyM = lobby.getItemMeta();
		lobbyM.setDisplayName(plugin.getConfig().getString("lobby-options-name").replace("&", "§"));
		lobby.setItemMeta(lobbyM);
		
		Preferences.setItem(11, player);
		Preferences.setItem(15, lobby);
		
		if(plugin.getConfig().getBoolean("preferences-menu-glass")) {
		Preferences.setItem(0, decoration);
		Preferences.setItem(1, decoration);
		Preferences.setItem(2, decoration);
		Preferences.setItem(3, decoration);
		Preferences.setItem(4, decoration4);
		Preferences.setItem(5, decoration3);
		Preferences.setItem(6, decoration3);
		Preferences.setItem(7, decoration3);
		Preferences.setItem(8, decoration3);
		Preferences.setItem(9, decoration);
		
		Preferences.setItem(10, decoration2);
		Preferences.setItem(12, decoration2);
		
		Preferences.setItem(13, decoration4);
		
		Preferences.setItem(14, decoration2);
		Preferences.setItem(16, decoration2);
		
		Preferences.setItem(17, decoration3);
		Preferences.setItem(18, decoration);
		Preferences.setItem(19, decoration);
		Preferences.setItem(20, decoration);
		Preferences.setItem(21, decoration);
		Preferences.setItem(22, decoration4);
		Preferences.setItem(23, decoration3);
		Preferences.setItem(24, decoration3);
		Preferences.setItem(25, decoration3);
		Preferences.setItem(26, decoration3);
		}
		p.openInventory(Preferences);
		}
		}
	}
	@EventHandler
	public void Preferences(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
		if(plugin.getConfig().getBoolean("per-world")== true) {
		if(p.getWorld()== world) {
		
		if(e.getInventory().getName().equals(plugin.getConfig().getString("preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 15) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
					LobbyPreferences.openGUI(p);
					p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 3);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 11) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
					PlayerPreferences.openPlayerPref(p);
					p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1, 3);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("preferences-menu-name").replace("&", "§"))) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		}
	}
	}
