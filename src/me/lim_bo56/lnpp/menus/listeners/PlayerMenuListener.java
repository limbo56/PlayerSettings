package me.lim_bo56.lnpp.menus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.lim_bo56.lnpp.MainPreferences;
import me.lim_bo56.lnpp.menus.MenuPreferences;
import me.lim_bo56.lnpp.menus.PlayerPreferences;
import me.lim_bo56.lnpp.utils.AllStrings;

/**
 * 
 * @author lim_bo56
 *
 */

public class PlayerMenuListener extends AllStrings implements Listener {

	MainPreferences plugin;

	public PlayerMenuListener(MainPreferences instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void InventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName().equals(PlayerPreferencesName)) {
			if (e.isRightClick() || e.isLeftClick()) {
				e.setCancelled(true);
			}
		}
	}

	// Back Listener
	@EventHandler
	public void BackListener(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (e.getInventory().getName().equals(PlayerPreferencesName)) {
			if (e.isRightClick() || e.isLeftClick()) {
				if (e.getSlot() == 35) {
					if (PreformCMD == true) {
						p.performCommand(PlayerPreferencesCMD);
					} else {
						MenuPreferences.openPreferencesMenu(p);
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	//Cancel Inventory drop.
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
	    Player player = event.getPlayer();
	   
	    if (player.getOpenInventory() != null) {
	        if (player.getOpenInventory().getTitle().equals(PlayerPreferencesName)) {
	        	PlayerPreferences.openPlayerPreferences(player);
	            event.getItemDrop().remove();
	        } 
	    }
	}

	@EventHandler
	public void PlayerPreferencesL(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		String world = p.getWorld().getName();

		for (String m : AllStrings.getInstance().World) {
			if (world.equalsIgnoreCase(m)) {

				// Speed Listener
				if (e.getInventory().getName().equals(PlayerPreferencesName)) {
					if (e.isRightClick() || e.isLeftClick()) {
						if (e.getSlot() == 10 || e.getSlot() == 19) {
							if (p.hasPermission("preferences.speed")) {
								if (MainPreferences.getInstance().Speed.contains(p)) {
									MainPreferences.getInstance().Speed.remove(p);
									p.removePotionEffect(PotionEffectType.SPEED);
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
								} else if (!MainPreferences.getInstance().Speed.contains(p)) {
									MainPreferences.getInstance().Speed.add(p);
									p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
								}
							} else if (!p.hasPermission("preferences.speed")) {
								p.sendMessage(NoPermissions);
							}
						}
					}
				}

				// Jump Listener
				if (e.getInventory().getName().equals(PlayerPreferencesName)) {
					if (e.isRightClick() || e.isLeftClick()) {
						if (e.getSlot() == 12 || e.getSlot() == 21) {
							if (p.hasPermission("preferences.jump")) {
								if (MainPreferences.getInstance().Jump.contains(p)) {
									MainPreferences.getInstance().Jump.remove(p);
									p.removePotionEffect(PotionEffectType.JUMP);
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
								} else if (!MainPreferences.getInstance().Jump.contains(p)) {
									MainPreferences.getInstance().Jump.add(p);
									p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 1));
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
								}
							} else if (!p.hasPermission("preferences.jump")) {
								p.sendMessage(NoPermissions);
							}
						}
					}
				}

				// Fly Listener
				if (e.getInventory().getName().equals(PlayerPreferencesName)) {
					if (e.isRightClick() || e.isLeftClick()) {
						if (e.getSlot() == 14 || e.getSlot() == 23) {
							if (p.hasPermission("preferences.fly")) {
								if (MainPreferences.getInstance().Fly.contains(p)) {
									MainPreferences.getInstance().Fly.remove(p);
									PlayerPreferences.openPlayerPreferences(p);
									p.setAllowFlight(false);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
								} else if (!MainPreferences.getInstance().Fly.contains(p)) {
									MainPreferences.getInstance().Fly.add(p);
									p.setAllowFlight(true);
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
								}
							} else if (!p.hasPermission("preferences.fly")) {
								p.sendMessage(NoPermissions);
							}
						}
					}
				}

				// Vanish Listener
				if (e.getInventory().getName().equals(PlayerPreferencesName)) {
					if (e.isRightClick() || e.isLeftClick()) {
						if (e.getSlot() == 16 || e.getSlot() == 25) {
							if (p.hasPermission("preferences.vanish")) {
								if (MainPreferences.getInstance().Vanish.contains(p)) {
									MainPreferences.getInstance().Vanish.remove(p);
									for (Player players : Bukkit.getOnlinePlayers()) {
										players.showPlayer(p);
									}
									p.removePotionEffect(PotionEffectType.INVISIBILITY);
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
								} else if (!MainPreferences.getInstance().Vanish.contains(p)) {
									MainPreferences.getInstance().Vanish.add(p);
									for (Player players : Bukkit.getOnlinePlayers()) {
										players.hidePlayer(p);
									}
									p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
									PlayerPreferences.openPlayerPreferences(p);
									p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
								}
							} else if (!p.hasPermission("preferences.vanish")) {
								p.sendMessage(NoPermissions);
							}
						}
					}
				}

			}
		}
	}
}
