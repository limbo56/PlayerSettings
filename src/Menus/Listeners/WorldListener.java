package Menus.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import Extra.AllStrings;
import PreferencesMain.MainPreferences;

public class WorldListener extends AllStrings implements Listener {

	MainPreferences plugin;
	
	public WorldListener(MainPreferences instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		
		 String world = p.getWorld().getName();		
		 
		 for(String m : AllStrings.World) {
			if(world.equalsIgnoreCase(m)) { 
 			for(Player players : Bukkit.getOnlinePlayers()) {
          	p.showPlayer(players);	
          	MainPreferences.Speed.remove(p);
    		p.removePotionEffect(PotionEffectType.SPEED);
    		MainPreferences.Jump.remove(p);
    		p.removePotionEffect(PotionEffectType.JUMP);
    		MainPreferences.Fly.remove(p);
    		p.setAllowFlight(false);
    		p.setFlying(false);
    		MainPreferences.Vanish.remove(p);
    		p.removePotionEffect(PotionEffectType.INVISIBILITY);
    		players.showPlayer(p);
    		
    		MainPreferences.Stacker.remove(p);
    		MainPreferences.Chat.remove(p);
    		MainPreferences.Fly.remove(p);
    		MainPreferences.Jump.remove(p);
    		MainPreferences.Speed.remove(p);
    		MainPreferences.Vanish.remove(p);
    		MainPreferences.Visibility.remove(p);
 			}
		}
		 }
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
        String world = p.getWorld().getName();		
		
        for(String m : AllStrings.World) {
        	if(world.equalsIgnoreCase(m)) {
        		MainPreferences.Chat.add(p);
        		MainPreferences.Visibility.add(p);
        	}
        }
        
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		MainPreferences.Speed.remove(p);
		p.removePotionEffect(PotionEffectType.SPEED);
		MainPreferences.Jump.remove(p);
		p.removePotionEffect(PotionEffectType.JUMP);
		MainPreferences.Fly.remove(p);
		p.setAllowFlight(false);
		MainPreferences.Vanish.remove(p);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
	}
}
