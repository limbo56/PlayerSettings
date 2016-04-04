package me.lim_bo56.lnpp.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Methods.Methods1_7_R3;
import Methods.Methods1_7_R4;
import Methods.Methods1_8_R3;
import Methods.MethodsClass;
import me.lim_bo56.lnpp.MainPreferences;

/**
 * 
 * @author lim_bo56
 *
 */

public class ItemFactory {
 
	public MethodsClass glow;
	private MainPreferences plugin = MainPreferences.getInstance();
	private static ItemFactory instance = new ItemFactory();
	
	public static ItemFactory getInstance() {
		return instance;
	}
	
	public ItemStack setItemWithLore(Material material, int amount, int shrt, String displayName, List<String> lore)
	  {
	    org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material, amount, (short)shrt);
	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(displayName);
	    meta.setLore(lore);

	    item.setItemMeta(meta);
	    return item;
	  }
	 
	 public ItemStack setItemNoLore(Material material, int amount, int shrt, String displayName)
	  {
	    org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(material, amount, (short)shrt);
	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(displayName);

	    item.setItemMeta(meta);
	    return item;
	  }
	 
	 public ItemStack setGlass(ItemStack stack, String displayName)
	  {
	    org.bukkit.inventory.ItemStack item = stack;
	    ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(displayName);

	    item.setItemMeta(meta);
	    return item;
	  }
	 
	 public boolean setupGlow() {

	        String version;

	        try {

	            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];

	        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
	            return false;
	        }

	        plugin.getLogger().info("Your server is running version " + version);

	        if (version.equals("v1_8_R3")) {
	            glow = new Methods1_8_R3();

	        } else if (version.equals("v1_7_R3")) {
	        	glow = new Methods1_7_R3();
	        	
	        } else if (version.equals("v1_7_R4")) {
  	        glow = new Methods1_7_R4();
  	        
          }
  
	        // This will return true if the server version was compatible with one of our NMS classes
	        // because if it is, our title would not be null
	        return glow != null;
	    }
	 
}
