package Menus.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import Extra.AllStrings;
import Menus.LobbyPreferences;
import Menus.MenuPreferences;
import PreferencesMain.MainPreferences;

/**
 * 
 * @author lim_bo56
 *
 */

public class LobbyMenuListener extends AllStrings implements Listener {

	MainPreferences plugin;
	
	public LobbyMenuListener(MainPreferences instance) {
		this.plugin = instance;
	}
	//Stacker Listener
    @EventHandler
    public void onStack(PlayerInteractAtEntityEvent e) {
    	Player p = e.getPlayer();
    	Entity n = e.getRightClicked();
    	
    	if(e.getRightClicked()== n || e.getPlayer()== p) {
    	if(MainPreferences.Stacker.contains(p)) {
    		if(MainPreferences.Stacker.contains(n) && (e.getRightClicked().getType() == EntityType.PLAYER)) {
    			p.setPassenger(n);
    		} else {
    			p.sendMessage(TargetStackerDisabled);
    		}
    	} else {
    		p.sendMessage(PlayerStackerDisabled);
    	}
    	}
    }
    
    @EventHandler
    public void Launch(PlayerInteractEvent e) {
      Player p = e.getPlayer();
      if ((e.getAction() == Action.LEFT_CLICK_AIR)) {
        if (p.getPassenger() != null)
        {
          Entity entity = p.getPassenger();

          entity.getVehicle().eject();

          Vector dir = p.getLocation().getDirection();
          entity.setVelocity(dir.multiply(1));
          entity.setFallDistance(-10000.0F);
        }
      }
    }
    
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
    	Entity d = e.getDamager();
    	
    	if(MainPreferences.Stacker.contains(d) && d.getPassenger() != null) {
    		e.setCancelled(true);
    	} else {
    		e.setCancelled(false);
    	}
    }
	
	//Back Listener
    @EventHandler
    public void BackListener(InventoryClickEvent e) {
    	Player p = (Player) e.getWhoClicked();
    	
    	if(e.getInventory().getName().equals(LobbyPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				if(e.getSlot()== 27) {
				MenuPreferences.openPreferencesMenu(p);
				e.setCancelled(true);
				}
			}
    	}
    }
    
	//Chat Listener
	 @EventHandler
	   public void ChatListener(final AsyncPlayerChatEvent e) {
		 Player p = e.getPlayer();
		 
		 String world = p.getWorld().getName();		
			
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) {  
			   if (!MainPreferences.Chat.contains(p)) {
				    e.getRecipients().remove(p);
					p.sendMessage(ChatDisabled);
			        e.setCancelled(true);
		   } else if (MainPreferences.Chat.contains(p)) {
			   e.getRecipients().add(p);
			   e.setCancelled(false);
		   }
		 }
		 }
	  }
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		
		if(e.getInventory().getName().equals(LobbyPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void StackerListener(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		//Stacker Listener
		if(e.getInventory().getName().equals(LobbyPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				if(e.getSlot()== 11 || e.getSlot()== 20) {
					if(MainPreferences.Stacker.contains(p)) {
						MainPreferences.Stacker.remove(p);
						LobbyPreferences.openLobbyPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
					} else if(!MainPreferences.Stacker.contains(p)) {
						MainPreferences.Stacker.add(p);
						LobbyPreferences.openLobbyPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void VisibilityListener(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		 String world = p.getWorld().getName();		
			
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) { 
		//Visibility Listener
		if(e.getInventory().getName().equals(LobbyPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				if(e.getSlot()== 13 || e.getSlot()== 22) {
					if(MainPreferences.Visibility.contains(p)) {
						MainPreferences.Visibility.remove(p);
						for(Player players : Bukkit.getOnlinePlayers()) {
						p.hidePlayer(players);
						}
						LobbyPreferences.openLobbyPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
					} else if(!MainPreferences.Visibility.contains(p)) {
						MainPreferences.Visibility.add(p);
						for(Player players : Bukkit.getOnlinePlayers()) {
						if(MainPreferences.Vanish.contains(players)) {
							p.hidePlayer(players);
						} else if(!MainPreferences.Vanish.contains(players)) {
							p.showPlayer(players);
						}
						}
						LobbyPreferences.openLobbyPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
					}
				}
			}
		}
			}
		}
	}
	
	@EventHandler
	public void ChatListener(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		 String world = p.getWorld().getName();		
			
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) {  
		//Chat Listener
		if(e.getInventory().getName().equals(LobbyPreferencesName)) {
			if(e.isRightClick() || e.isLeftClick()) {
				if(e.getSlot()== 15 || e.getSlot()== 24) {
					if(MainPreferences.Chat.contains(p)) {
						MainPreferences.Chat.remove(p);
						LobbyPreferences.openLobbyPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
					} else if(!MainPreferences.Chat.contains(p)) {
						MainPreferences.Chat.add(p);
						LobbyPreferences.openLobbyPreferences(p);
						p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
					}
				}
			}
		}
	}
	}
	}
}