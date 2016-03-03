package Menus.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Extra.AllStrings;
import Menus.MenuPreferences;
import Menus.PlayerPreferences;
import PreferencesMain.MainPreferences;

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
		if(e.getInventory().getName().equals(PlayerPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
			e.setCancelled(true);
			}
		}
	}
	
	
	//Back Listener
    @EventHandler
    public void BackListener(InventoryClickEvent e) {
    	Player p = (Player) e.getWhoClicked();
    	
    	if(e.getInventory().getName().equals(PlayerPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				if(e.getSlot()== 35) {
				MenuPreferences.openPreferencesMenu(p);
				e.setCancelled(true);
				}
			}
    	}
    }
    
	@EventHandler
	public void PlayerPreferencesL(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		 String world = p.getWorld().getName();		
			
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) { 
		
		//Speed Listener
		if(e.getInventory().getName().equals(PlayerPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				if(e.getSlot()== 10 || e.getSlot()== 19) {
					if(p.hasPermission("preferences.speed")) {
					if(MainPreferences.Speed.contains(p)) {
						MainPreferences.Speed.remove(p);
						p.removePotionEffect(PotionEffectType.SPEED);
						PlayerPreferences.openPlayerPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
					} else if(!MainPreferences.Speed.contains(p)) {
						MainPreferences.Speed.add(p);
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
						PlayerPreferences.openPlayerPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
					}
				} else if(!p.hasPermission("preferences.speed")) {
					p.sendMessage(NoPermissions);
				}
			}
		}
		} 
		
		//Jump Listener
			if(e.getInventory().getName().equals(PlayerPreferencesName)) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(e.getSlot()== 12 || e.getSlot()== 21) {
						if(p.hasPermission("preferences.jump")) {
						if(MainPreferences.Jump.contains(p)) {
							MainPreferences.Jump.remove(p);
							p.removePotionEffect(PotionEffectType.JUMP);
							PlayerPreferences.openPlayerPreferences(p);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
						} else if(!MainPreferences.Jump.contains(p)) {
							MainPreferences.Jump.add(p);
						    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 1));
							PlayerPreferences.openPlayerPreferences(p);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
						}
					} else if(!p.hasPermission("preferences.jump")) {
						p.sendMessage(NoPermissions);
					}
				}
			}
			}
		
		//Fly Listener
			if(e.getInventory().getName().equals(PlayerPreferencesName)) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(e.getSlot()== 14 || e.getSlot()== 23) {
						if(p.hasPermission("preferences.fly")) {
						if(MainPreferences.Fly.contains(p)) {
							MainPreferences.Fly.remove(p);
							PlayerPreferences.openPlayerPreferences(p);
							p.setAllowFlight(false);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
						} else if(!MainPreferences.Fly.contains(p)) {
							MainPreferences.Fly.add(p);
							p.setAllowFlight(true);
							PlayerPreferences.openPlayerPreferences(p);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
						}
					} else if(!p.hasPermission("preferences.fly")) {
						p.sendMessage(NoPermissions);
					}
				}
			}
			}
			
		//Vanish Listener
			if(e.getInventory().getName().equals(PlayerPreferencesName)) {
				if(e.isRightClick() || e.isLeftClick()) {
					if(e.getSlot()== 16 || e.getSlot()== 25) {
						if(p.hasPermission("preferences.vanish")) {
						if(MainPreferences.Vanish.contains(p)) {
							MainPreferences.Vanish.remove(p);
							for(Player players : Bukkit.getOnlinePlayers()) {
								players.showPlayer(p);
							}
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
							PlayerPreferences.openPlayerPreferences(p);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
						} else if(!MainPreferences.Vanish.contains(p)) {
							MainPreferences.Vanish.add(p);
							for(Player players : Bukkit.getOnlinePlayers()) {
								players.hidePlayer(p);
							}
							p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
							PlayerPreferences.openPlayerPreferences(p);
							p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
						}
					} else if(!p.hasPermission("preferences.vanish")) {
						p.sendMessage(NoPermissions);
					}
				}
			}
			}
		
		
	}
	}
	}
}
