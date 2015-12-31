package Menu;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import LobbyPlayerMain.Main;

public class Interact implements Listener{

	Main plugin;
	public Interact(Main instance) {
	    this.plugin = instance;
	  }
	  @EventHandler
	  public void Launch(PlayerInteractEvent e)
	  {
	    Player p = e.getPlayer();
	    World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
	    if(plugin.getConfig().getBoolean("per-world")== true) {
	    if(p.getWorld()== world) {
	    if (e.getAction() == Action.LEFT_CLICK_AIR)
	    {
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
	    }
	  }
	  @EventHandler
	public void EntityInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Entity entity = e.getRightClicked();
		
		World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
		if(plugin.getConfig().getBoolean("per-world")== true) {
		if(p.getWorld()== world) {
		if(Main.Stacker.contains(p)) {
			if(Main.Stacker.contains(entity)) {
		if(entity.getType()== EntityType.PLAYER) {
			e.setCancelled(true);
			p.setPassenger(entity);	
		}
			}
	} else if(!Main.Stacker.contains(entity)) {
		p.sendMessage(plugin.getConfig().getString("target-stacker-disabled").replace("&", "§").replace("%player%", p.getName()));
	} else if(!Main.Stacker.contains(p)) {
		p.sendMessage(plugin.getConfig().getString("player-stacker-disabled").replace("&", "§").replace("%player%", p.getName()));
	}
		}
		}
	}
	  @EventHandler
	  public void HitEntity(EntityDamageByEntityEvent e) {
		  World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
		  if(plugin.getConfig().getBoolean("per-world")== true) {
		  if(e.getDamager().getWorld()== world) {
	    if (e.getDamager().getType() == EntityType.PLAYER)
	    {

	      if(Main.Stacker.contains(e.getDamager())) 
	      {
	        e.setCancelled(true);
	      }
	    }
	  }
	  }
	  }
}
