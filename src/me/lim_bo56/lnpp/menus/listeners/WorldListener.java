package me.lim_bo56.lnpp.menus.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.lim_bo56.lnpp.MainPreferences;
import me.lim_bo56.lnpp.utils.AllStrings;
import me.lim_bo56.lnpp.utils.UpdateChecker;

public class WorldListener extends AllStrings implements Listener {

	MainPreferences plugin;
	
	public WorldListener(MainPreferences instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player p = e.getPlayer();
		
		 String world = p.getWorld().getName();		
		 
		 for(String m : AllStrings.getInstance().World) {
			if(world.equalsIgnoreCase(m)) { 
 			for(Player players : Bukkit.getOnlinePlayers()) {
          	p.showPlayer(players);	
          	MainPreferences.getInstance().Speed.remove(p);
    		p.removePotionEffect(PotionEffectType.SPEED);
    		MainPreferences.getInstance().Jump.remove(p);
    		p.removePotionEffect(PotionEffectType.JUMP);
    		MainPreferences.getInstance().Fly.remove(p);
    		p.setAllowFlight(false);
    		p.setFlying(false);
    		MainPreferences.getInstance().Vanish.remove(p);
    		p.removePotionEffect(PotionEffectType.INVISIBILITY);
    		players.showPlayer(p);
    		
    		MainPreferences.getInstance().Stacker.remove(p);
    		MainPreferences.getInstance().Chat.remove(p);
    		MainPreferences.getInstance().Fly.remove(p);
    		MainPreferences.getInstance().Jump.remove(p);
    		MainPreferences.getInstance().Speed.remove(p);
    		MainPreferences.getInstance().Vanish.remove(p);
    		MainPreferences.getInstance().Visibility.remove(p);
 			}
		}
		 }
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		
        String world = p.getWorld().getName();	        
		
        for(String m : AllStrings.getInstance().World) {
        	if(world.equalsIgnoreCase(m)) {
        		MainPreferences.getInstance().Chat.add(p);
        		MainPreferences.getInstance().Visibility.add(p);
        	}
        }
        
        new BukkitRunnable() {
			
			@Override
			public void run() {
				if(p.isOp()) {
				 p.sendMessage("§f[§c§lPREFERENCES§f] " + UpdateChecker.getInstance().checkForUpdate());
				}
			}
		}.runTaskLater(plugin, 21);
       
        
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		MainPreferences.getInstance().Speed.remove(p);
		p.removePotionEffect(PotionEffectType.SPEED);
		MainPreferences.getInstance().Jump.remove(p);
		p.removePotionEffect(PotionEffectType.JUMP);
		MainPreferences.getInstance().Fly.remove(p);
		p.setAllowFlight(false);
		MainPreferences.getInstance().Vanish.remove(p);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
	}
}
