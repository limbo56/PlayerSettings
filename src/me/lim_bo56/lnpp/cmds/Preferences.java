package me.lim_bo56.lnpp.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lim_bo56.lnpp.MainPreferences;
import me.lim_bo56.lnpp.menus.MenuPreferences;
import me.lim_bo56.lnpp.utils.AllStrings;

/**
 * 
 * @author lim_bo56
 *
 */

public class Preferences implements CommandExecutor {

	private MainPreferences plugin = MainPreferences.getInstance();
	
	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		 
		 String world = p.getWorld().getName();	
		 
		 for(String m : AllStrings.getInstance().World) {
			if(world.equalsIgnoreCase(m)) { 
		 if(label.equalsIgnoreCase("preferences") || label.equalsIgnoreCase("pref")) {
			 if(args.length == 0) {
			MenuPreferences.openPreferencesMenu(p);
			 } else if(args.length == 1 ) {
				 if(args[0].equals("reload")) {
					 if(p.hasPermission("preferences.reload")) {
					 plugin.reloadConfig();
					 p.sendMessage("§aThe configuration file was successfully reloaded!");			
					 } else if(!p.hasPermission("preferences.reload")) {
						 p.sendMessage(AllStrings.getInstance().NoPermissions);
					 }
				 }
			 }
		}
		}
		 }
		return false;
	}
}
