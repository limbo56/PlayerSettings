package Menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import LobbyPlayerMain.Main;

public class PlayerPreferences implements Listener{

	static Main plugin;
	public PlayerPreferences(Main instance) {
	    this.plugin = instance;
	  }
	
	public static String blank = "";
	public static String infoPlayer = "§7Set the preferences to your liking"; 
	public static String infoPlayer2 = "§7in the player preferences";
	public static String infoPlayer3 = "§7so you can enjoy the game more!";
	
	public static String infoLobby = "§7Set the preferences to your liking"; 
	public static String infoLobby2 = "§7in the lobby preferences";
    public static String infoLobby3 = "§7so you can enjoy the game more!";
	
	public static void openPlayerPref(final Player p)
	{
		if(plugin.getConfig().getBoolean("per-world")== true) {
		World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
		if(p.getWorld()== world) {
		final Inventory PlayerPref = Bukkit.createInventory(null, 36, plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"));
		
		ItemStack decoration = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getWoolData());
		ItemStack decoration2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getWoolData());
		
		ItemStack info = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta infoM = info.getItemMeta();
		infoM.setDisplayName("§e§lInfo");
		infoM.setLore(Arrays.asList(infoPlayer, infoPlayer2, infoPlayer3));
		info.setItemMeta(infoM);
		
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta backM = back.getItemMeta();
		backM.setDisplayName(plugin.getConfig().getString("back-item-name").replace("&", "§"));
		back.setItemMeta(backM);
		
		ItemStack hide = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta hideM = hide.getItemMeta();
		hideM.setDisplayName(plugin.getConfig().getString("hide-item-name").replace("&", "§"));
		hide.setItemMeta(hideM);
		
		ItemStack fly = new ItemStack(Material.FEATHER);
		ItemMeta flyM = fly.getItemMeta();
		flyM.setDisplayName(plugin.getConfig().getString("fly-item-name").replace("&", "§"));
		fly.setItemMeta(flyM);
		
		ItemStack speed = new ItemStack(Material.SUGAR);
		ItemMeta speedM = speed.getItemMeta();
		speedM.setDisplayName(plugin.getConfig().getString("speed-item-name").replace("&", "§"));
		speed.setItemMeta(speedM);
		
		ItemStack jump = new ItemStack(Material.DIAMOND_BOOTS);
		ItemMeta jumpM = jump.getItemMeta();
		jumpM.setDisplayName(plugin.getConfig().getString("jump-item-name").replace("&", "§"));
		jump.setItemMeta(jumpM);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.jump.contains(p)) {
					ItemStack OnJump = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
				    ItemMeta OnJumpM = OnJump.getItemMeta();
				    
				    OnJumpM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
				    OnJumpM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
				    OnJump.setItemMeta(OnJumpM);
					PlayerPref.setItem(19, OnJump);	
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!Main.jump.contains(p)) {
					ItemStack OffJump = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
				    ItemMeta OffJumpM = OffJump.getItemMeta();
				    
				    OffJumpM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
				    OffJumpM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
				    OffJump.setItemMeta(OffJumpM);
					PlayerPref.setItem(19, OffJump);	
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.speed.contains(p)) {
					ItemStack OnSpeed = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
				    ItemMeta OnSpeedM = OnSpeed.getItemMeta();
				    
				    OnSpeedM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
				    OnSpeedM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
				    OnSpeed.setItemMeta(OnSpeedM);
					PlayerPref.setItem(21, OnSpeed);	
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!Main.speed.contains(p)) {
					ItemStack OffSpeed = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
				    ItemMeta OffSpeedM = OffSpeed.getItemMeta();
				    
				    OffSpeedM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
				    OffSpeedM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
				    OffSpeed.setItemMeta(OffSpeedM);
					PlayerPref.setItem(21, OffSpeed);
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.fly.contains(p)) {
					ItemStack OnFly = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
				    ItemMeta OnFlyM = OnFly.getItemMeta();
				    
				    OnFlyM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
				    OnFlyM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
				    OnFly.setItemMeta(OnFlyM);
					PlayerPref.setItem(23, OnFly);	
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!Main.fly.contains(p)) {
					ItemStack OffFly = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
				    ItemMeta OffFlyM = OffFly.getItemMeta();
				    
				    OffFlyM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
				    OffFlyM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
				    OffFly.setItemMeta(OffFlyM);
					PlayerPref.setItem(23, OffFly);
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Main.hide.contains(p.getName())) {
					ItemStack OnHide = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
				    ItemMeta OnHideM = OnHide.getItemMeta();
				    
				    OnHideM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
				    OnHideM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
				    OnHide.setItemMeta(OnHideM);
					PlayerPref.setItem(25, OnHide);	
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!Main.hide.contains(p.getName())) {
					ItemStack OffHide = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
				    ItemMeta OffHideM = OffHide.getItemMeta();
				    
				    OffHideM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
				    OffHideM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
				    OffHide.setItemMeta(OffHideM);
					PlayerPref.setItem(25, OffHide);
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		
		if(plugin.getConfig().getBoolean("player-preferences-glass")) {
		PlayerPref.setItem(0, decoration2);
		PlayerPref.setItem(1, decoration);
		PlayerPref.setItem(2, decoration);
		PlayerPref.setItem(3, decoration);
		PlayerPref.setItem(5, decoration);
		PlayerPref.setItem(6, decoration);
		PlayerPref.setItem(7, decoration);
		PlayerPref.setItem(8, decoration2);
		PlayerPref.setItem(9, decoration);
		PlayerPref.setItem(11, decoration);
		PlayerPref.setItem(13, decoration);
		PlayerPref.setItem(15, decoration);
		PlayerPref.setItem(17, decoration);
		PlayerPref.setItem(18, decoration);
		PlayerPref.setItem(20, decoration);
		PlayerPref.setItem(22, decoration);
		PlayerPref.setItem(24, decoration);
		PlayerPref.setItem(26, decoration);
		PlayerPref.setItem(27, decoration2);
		PlayerPref.setItem(28, decoration);
		PlayerPref.setItem(29, decoration);
		PlayerPref.setItem(30, decoration);
		PlayerPref.setItem(32, decoration);
		PlayerPref.setItem(33, decoration);
		PlayerPref.setItem(34, decoration);
		PlayerPref.setItem(35, decoration2);
		}
		PlayerPref.setItem(4, info);
		PlayerPref.setItem(31, back);
		PlayerPref.setItem(10, jump);
		PlayerPref.setItem(12, speed);
		PlayerPref.setItem(14, fly);
		PlayerPref.setItem(16, hide);
		
		p.openInventory(PlayerPref);
	}
		}
	}
	@EventHandler
	public void PlayerPref(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
		if(plugin.getConfig().getBoolean("per-world")== true) {
		if(p.getWorld()== world) {
		if(plugin.getConfig().getBoolean("player-preferences-glass")) {
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 0) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 1) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 2) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 3) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 5) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 6) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 7) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 8) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 9) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 11) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 13) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 15) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 17) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 18) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 20) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 22) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 24) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 26) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 27) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 28) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 29) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 30) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 32) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 33) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 34) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 35) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 31) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
					PreferencesMenu.openPREF(p);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 4) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 10) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 12) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 14) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 16) {
				if(e.isRightClick() || e.isLeftClick()) {
					e.setCancelled(true);
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 19) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(!p.hasPermission("preferences.jump")) {
						e.setCancelled(true);
						p.sendMessage(plugin.getConfig().getString("no-permissions").replace("&", "§"));
					}
				}
			}
		}  
		if(p.hasPermission("preferences.jump")) {
			if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
				if(e.getSlot()== 19 || e.getSlot() == 10) {
					if(e.isRightClick() || e.isLeftClick()) {
						if(Main.jump.contains(p)) {
							e.setCancelled(true);
						    p.removePotionEffect(PotionEffectType.JUMP);
						    p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));	
						    Main.jump.remove(p);
 						} else if(!Main.jump.contains(p)) {
 							if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
 								if(e.getSlot()== 19 || e.getSlot() == 10) {
 									if(e.isRightClick() || e.isLeftClick()) {
 										e.setCancelled(true);
 										p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 1));
 										p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
 										Main.jump.add(p);
 									}
 								}
 									}
 						}
					}
					}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 21) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(!p.hasPermission("preferences.speed")) {
						e.setCancelled(true);
						p.sendMessage(plugin.getConfig().getString("no-permissions").replace("&", "§"));
					}
				}
			}
		}
		if(p.hasPermission("preferences.speed")) {
			if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
				if(e.getSlot()== 21 || e.getSlot() == 12) {
					if(e.isRightClick() || e.isLeftClick()) {
						if(Main.speed.contains(p)) {
						e.setCancelled(true);
						p.removePotionEffect(PotionEffectType.SPEED);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));	
						Main.speed.remove(p);
						} else if(!Main.speed.contains(p)) {
							if(p.hasPermission("preferences.speed")) {
								if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
									if(e.getSlot()== 21 || e.getSlot() == 12) {
										if(e.isRightClick() || e.isLeftClick()) {
											e.setCancelled(true);
											p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
											p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
											Main.speed.add(p);
										}
									}
								}
							}
						}
					}
					}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 23) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(!p.hasPermission("preferences.fly")) {
						e.setCancelled(true);
						p.sendMessage(plugin.getConfig().getString("no-permissions").replace("&", "§"));
				}
				}
		}
		}
		if(p.hasPermission("preferences.fly")) {
			if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
				if(e.getSlot()== 23 || e.getSlot() == 14) {
					if(e.isRightClick() || e.isLeftClick()) {
						if(Main.fly.contains(p)) {
							e.setCancelled(true);
							p.setAllowFlight(false);
							p.setFlying(false);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));	
							Main.fly.remove(p);
						} else if(!Main.fly.contains(p)) {
							if(p.hasPermission("preferences.fly")) {
								if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
									if(e.getSlot()== 23 || e.getSlot() == 14) {
										if(e.isRightClick() || e.isLeftClick()) {
											e.setCancelled(true);
											p.setAllowFlight(true);
											p.setFlying(true);
											p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
											Main.fly.add(p);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
			if(e.getSlot()== 25) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(!p.hasPermission("preferences.vanish")) {
						e.setCancelled(true);
						p.sendMessage(plugin.getConfig().getString("no-permissions").replace("&", "§"));
					}
				}
			}
		}	
		if(p.hasPermission("preferences.vanish")) {
			if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
				if(e.getSlot()== 25 || e.getSlot() == 16) {
					if(e.isRightClick() || e.isLeftClick()) {
						if(Main.hide.contains(p.getName())) {
							e.setCancelled(true);
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
							for(Player ps : Bukkit.getOnlinePlayers()) 
							ps.showPlayer(p);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));	
							Main.hide.remove(p.getName());
						} else if(!Main.hide.contains(p.getName())) {
							if(p.hasPermission("preferences.vanish")) {
								if(e.getInventory().getName().equals(plugin.getConfig().getString("player-preferences-menu-name").replace("&", "§"))) {
									if(e.getSlot()== 25 || e.getSlot() == 16) {
										if(e.isRightClick() || e.isLeftClick()) {
											e.setCancelled(true);
											p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 0));
											for(Player ps : Bukkit.getOnlinePlayers())
											ps.hidePlayer(p);
											Main.hide.add(p.getName());
											p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		}
		}
	}
}
