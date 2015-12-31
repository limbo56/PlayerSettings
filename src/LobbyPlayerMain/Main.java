package LobbyPlayerMain;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import Menu.Interact;
import Menu.LobbyPreferences;
import Menu.PlayerPreferences;
import Menu.PreferencesMenu;

public class Main extends JavaPlugin implements Listener{
	
	private static Main instance;
	public static HashSet<Player> Stacker = new HashSet<Player>();
	public static HashSet<Player> Chat = new HashSet<Player>();
	public static HashSet<Player> Hide = new HashSet<Player>();
	public static HashSet<Player> Glow = new HashSet<Player>();
	public static HashSet<Player> speed = new HashSet<Player>();
	public static HashSet<Player> fly = new HashSet<Player>();
	public static HashSet<Player> jump = new HashSet<Player>();
	public static Set<String> hide = new HashSet<String>();
 	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		 pm.registerEvents(new Interact(this), this);
		 pm.registerEvents(new LobbyPreferences(this), this);
		 pm.registerEvents(new PlayerPreferences(this), this);
		 pm.registerEvents(new PreferencesMenu(this), this);
	}
	public static Main getInstance() {
	    return instance;
	  }
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Lobby&PlayerOptions has started without errors!");
		saveDefaultConfig();
		reloadConfig();
		registerListeners();
	}
	public void onDisable() {
		getLogger().info("Lobby&PlayerOptions has been disabled!");
	}
	  @EventHandler
	   public void ChatManager(final AsyncPlayerChatEvent e) {
		  Player p = e.getPlayer();
		  World world = Bukkit.getWorld(getConfig().getString("world-to-work"));
		  if(getConfig().getBoolean("per-world")== true) {
		  if(p.getWorld()== world) {
			   if(!Chat.contains(p)) {
			   e.getRecipients().remove(p);
			   }else if(Chat.contains(p)) {
				   e.getRecipients().add(p);
			   }
		  } else if(p.getWorld()!= world) {
			  e.getRecipients().add(p);
		  }
		  }
	  }
    @EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		  if(getConfig().getBoolean("per-world")== true) {
		  World world = Bukkit.getWorld(getConfig().getString("world-to-work"));
		  if(p.getWorld()== world) {
		Stacker.add(p);
		Chat.add(p);
		fly.remove(p);
		hide.remove(p.getName());
		speed.remove(p);
		jump.remove(p);
		Hide.add(p);
		  }
		  }
		  if(hide.contains(p)) {
			  for(Player players : Bukkit.getOnlinePlayers()) {
			  players.hidePlayer(p);
		  }
		}
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
    	Player p = e.getPlayer();
    	
    	p.removePotionEffect(PotionEffectType.INVISIBILITY);
    	p.removePotionEffect(PotionEffectType.JUMP);
    	p.removePotionEffect(PotionEffectType.SPEED);
    	
    }
    @EventHandler
    public void ChangeWorld(PlayerChangedWorldEvent e) {
    	final Player p = e.getPlayer();
    	
    	if(getConfig().getBoolean("per-world")== true) {
    	Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
  	      public void run() {
  	        String world = p.getWorld().getName();
            
  	          if (!world.equalsIgnoreCase(getConfig().getString("world-to-work"))) {
  	        	for(Player players : Bukkit.getOnlinePlayers()) {
  	        	p.showPlayer(players);
  	   			players.showPlayer(p);
  	        	}
  	   			p.removePotionEffect(PotionEffectType.INVISIBILITY);
  	   	    	p.removePotionEffect(PotionEffectType.JUMP);
  	   	    	p.removePotionEffect(PotionEffectType.SPEED);
  	   	    	Stacker.remove(p);
  	   			Chat.remove(p);
  	   			fly.remove(p);
  	   			hide.remove(p.getName());
  	   			speed.remove(p);
  	   			jump.remove(p);
  	   			Hide.add(p);
  	   			p.setAllowFlight(false);
  	   			p.setFlying(false);
  	          }
  	      }
    	}, 1L);
    	}
    }
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;	
		if(getConfig().getBoolean("per-world")== true) {
		World world = Bukkit.getWorld(getConfig().getString("world-to-work"));
		if(p.getWorld()== world) {
			
			if(p.hasPermission("preferences.reload")) {
				if(cmd.getName().equalsIgnoreCase("preferences-reload")) {
				reloadConfig();
				p.sendMessage("§aThe config was reloaded correctly!");
			}
			}
			
			
		if(cmd.getName().equalsIgnoreCase("preferences") || cmd.getName().equalsIgnoreCase("pref")) {
			PreferencesMenu.openPREF(p);
		}
		
		if(cmd.getName().equalsIgnoreCase("preferencesPlayer")) {
			PlayerPreferences.openPlayerPref(p);
		}
		
		if(cmd.getName().equalsIgnoreCase("preferencesLobby")) {
			LobbyPreferences.openGUI(p);
		}
		
	}
}
		return false;
}
}
