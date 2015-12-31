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
import org.bukkit.scheduler.BukkitRunnable;

import LobbyPlayerMain.Main;
public class LobbyPreferences implements Listener{

  static Main plugin;
	
	public LobbyPreferences(Main instance) {
	    this.plugin = instance;
	  }
	public static String blank = "";
	public static String infoPlayer = "§7Set the preferences to your liking"; 
	public static String infoPlayer2 = "§7in the player preferences";
	public static String infoPlayer3 = "§7so you can enjoy the game more!";
	
	public static String infoLobby = "§7Set the preferences to your liking"; 
	public static String infoLobby2 = "§7in the lobby preferences";
    public static String infoLobby3 = "§7so you can enjoy the game more!";
    
    public static void openGUI(final Player p)
    {
    	World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
      if ((plugin.getConfig().getBoolean("per-world")== true)) {
    	  if(p.getWorld()== world) {
        final Inventory LobbyOptions = Bukkit.createInventory(null, 36, plugin.getConfig().getString("lobby-preferences-menu-name").replace("&", "§"));

        ItemStack Stacker = new ItemStack(Material.FIREBALL);
        ItemMeta StackerM = Stacker.getItemMeta();
        StackerM.setDisplayName(plugin.getConfig().getString("Stacker-Item-Name").replace("&", "§"));
        Stacker.setItemMeta(StackerM);

        ItemStack decoration = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getWoolData());
        ItemStack decoration2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getWoolData());

        ItemStack Chat = new ItemStack(Material.PAPER);
        ItemMeta ChatM = Chat.getItemMeta();

        ChatM.setDisplayName(plugin.getConfig().getString("Chat-Item-Name").replace("&", "§"));
        Chat.setItemMeta(ChatM);

        ItemStack Show = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta ShowM = Show.getItemMeta();

        ShowM.setDisplayName(plugin.getConfig().getString("Visibility-Item-Name").replace("&", "§"));
        Show.setItemMeta(ShowM);

        ItemStack info = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta infoM = info.getItemMeta();
        infoM.setDisplayName("§e§lInfo");
        infoM.setLore(Arrays.asList(new String[] { infoLobby, infoLobby2, infoLobby3 }));
        info.setItemMeta(infoM);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backM = back.getItemMeta();
        backM.setDisplayName(plugin.getConfig().getString("back-item-name").replace("&", "§"));
        back.setItemMeta(backM);

        new BukkitRunnable()
        {
          public void run()
          {
            if (Main.Stacker.contains(p)) {
              ItemStack OnStacker = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
              ItemMeta OnStackerM = OnStacker.getItemMeta();

              OnStackerM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
              OnStackerM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
              OnStacker.setItemMeta(OnStackerM);
              LobbyOptions.setItem(20, OnStacker);
            }
          }
        }
        .runTaskTimer(plugin, 0L, 5L);

        new BukkitRunnable() {
        
          public void run()
          {
            if (!Main.Stacker.contains(p)) {
              ItemStack OffStacker = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
              ItemMeta OffStackerM = OffStacker.getItemMeta();

              OffStackerM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
              OffStackerM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
              OffStacker.setItemMeta(OffStackerM);
              LobbyOptions.setItem(20, OffStacker);
            }
          }
        }
        .runTaskTimer(plugin, 0L, 5L);

        new BukkitRunnable() {
          public void run()
          {
            if (Main.Chat.contains(p)) {
              ItemStack OnChat = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
              ItemMeta OnChatM = OnChat.getItemMeta();

              OnChatM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
              OnChatM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
              OnChat.setItemMeta(OnChatM);
              LobbyOptions.setItem(22, OnChat);
            }
          }
        }
        .runTaskTimer(plugin, 0L, 5L);

        new BukkitRunnable() {
        	
          public void run()
          {
            if (!Main.Chat.contains(p)) {
              ItemStack OffChat = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
              ItemMeta OffChatM = OffChat.getItemMeta();

              OffChatM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
              OffChatM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
              OffChat.setItemMeta(OffChatM);
              LobbyOptions.setItem(22, OffChat);
            }
          }
        }
        .runTaskTimer(plugin, 0L, 5L);

        new BukkitRunnable()
        {
          public void run()
          {
        	  for(Player players : Bukkit.getOnlinePlayers()) {
            if (Main.Hide.contains(p)) {
              ItemStack OnVisibility = new ItemStack(Material.INK_SACK, 1, DyeColor.PURPLE.getWoolData());
              ItemMeta OnVisibilityM = OnVisibility.getItemMeta();

              OnVisibilityM.setDisplayName(plugin.getConfig().getString("item-on").replace("&", "§"));
              OnVisibilityM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-disable-item-lore").replace("&", "§")));
              OnVisibility.setItemMeta(OnVisibilityM);
              p.showPlayer(players);
              LobbyOptions.setItem(24, OnVisibility);
            }
          }
        }
        }
        .runTaskTimer(plugin, 0L, 5L);

        new BukkitRunnable()
        {
          public void run()
          {
        	  for(Player players : Bukkit.getOnlinePlayers()) {
            if (!Main.Hide.contains(p)) {
              ItemStack OffVisibility = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getWoolData());
              ItemMeta OffVisibilityM = OffVisibility.getItemMeta();

              OffVisibilityM.setDisplayName(plugin.getConfig().getString("item-off").replace("&", "§"));
              OffVisibilityM.setLore(Arrays.asList(blank, plugin.getConfig().getString("click-to-enable-item-lore").replace("&", "§")));
              OffVisibility.setItemMeta(OffVisibilityM);
              p.hidePlayer(players);
              LobbyOptions.setItem(24, OffVisibility);
            }
          }
        }
        }
        .runTaskTimer(plugin, 0L, 5L);

        if (plugin.getConfig().getBoolean("lobby-preferences-glass")) {
          LobbyOptions.setItem(0, decoration2);
          LobbyOptions.setItem(1, decoration);
          LobbyOptions.setItem(2, decoration);
          LobbyOptions.setItem(3, decoration);

          LobbyOptions.setItem(5, decoration);
          LobbyOptions.setItem(6, decoration);
          LobbyOptions.setItem(7, decoration);
          LobbyOptions.setItem(8, decoration2);
          LobbyOptions.setItem(9, decoration);
          LobbyOptions.setItem(10, decoration);
          LobbyOptions.setItem(12, decoration);
          LobbyOptions.setItem(14, decoration);
          LobbyOptions.setItem(16, decoration);
          LobbyOptions.setItem(17, decoration);
          LobbyOptions.setItem(18, decoration);
          LobbyOptions.setItem(19, decoration);
          LobbyOptions.setItem(21, decoration);
          LobbyOptions.setItem(23, decoration);
          LobbyOptions.setItem(25, decoration);
          LobbyOptions.setItem(26, decoration);
          LobbyOptions.setItem(27, decoration2);
          LobbyOptions.setItem(28, decoration);
          LobbyOptions.setItem(29, decoration);
          LobbyOptions.setItem(30, decoration);

          LobbyOptions.setItem(32, decoration);
          LobbyOptions.setItem(33, decoration);
          LobbyOptions.setItem(34, decoration);
          LobbyOptions.setItem(35, decoration2);
        }
        LobbyOptions.setItem(4, info);
        LobbyOptions.setItem(31, back);
        LobbyOptions.setItem(11, Stacker);
        LobbyOptions.setItem(13, Chat);
        LobbyOptions.setItem(15, Show);

        p.openInventory(LobbyOptions);
      }
      }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
      Player p = (Player)e.getWhoClicked();
      World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
      if ((plugin.getConfig().getBoolean("per-world")) && 
        (p.getWorld() == world)) {
        if ((e.getInventory().getName().equals(plugin.getConfig().getString("lobby-preferences-menu-name").replace("&", "§"))) && 
          (e.isRightClick()) || (e.isLeftClick())) {
          e.setCancelled(true);
        }

        if ((e.getInventory().getName().equals(plugin.getConfig().getString("lobby-preferences-menu-name").replace("&", "§"))) && 
          (e.getSlot() == 31) && (
          (e.isRightClick()) || (e.isLeftClick()))) {
          e.setCancelled(true);
          PreferencesMenu.openPREF(p);
        }
      }
    }
        @EventHandler
        public void Visibility(InventoryClickEvent e) {
        	Player p = (Player)e.getWhoClicked();
        	 World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
             if ((plugin.getConfig().getBoolean("per-world")) && 
               (p.getWorld() == world) && 
               (e.getInventory().getName().equals(plugin.getConfig().getString("lobby-preferences-menu-name").replace("&", "§"))) && 
               (e.getSlot() == 24 || e.getSlot() == 15) && (
               (e.isRightClick()) || (e.isLeftClick())))
               if (Main.Hide.contains(p)) {
                 e.setCancelled(true);
                 p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
                 Main.Hide.remove(p);
               } else if (((e.isRightClick()) || (e.isLeftClick())) && (!Main.Hide.contains(p))) {
                 e.setCancelled(true);
                 p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
                 Main.Hide.add(p);
               }
        }
    
    @EventHandler
    public void Chat(InventoryClickEvent e)
    {
      Player p = (Player)e.getWhoClicked();
      World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
      if ((plugin.getConfig().getBoolean("per-world")) && 
        (p.getWorld() == world) &&
        (e.getInventory().getName().equals(plugin.getConfig().getString("lobby-preferences-menu-name").replace("&", "§"))) && 
        (e.getSlot() == 22 || e.getSlot() == 13) && (
        (e.isRightClick()) || (e.isLeftClick())))
        if (Main.Chat.contains(p)) {
          e.setCancelled(true);
          p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
          Main.Chat.remove(p);
        } else if (((e.isRightClick()) || (e.isLeftClick())) && 
          (!Main.Chat.contains(p))) {
          e.setCancelled(true);
          p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
          Main.Chat.add(p);
        }
    }

    @EventHandler
    public void Stacker(InventoryClickEvent e)
    {
      Player p = (Player)e.getWhoClicked();
      World world = Bukkit.getWorld(plugin.getConfig().getString("world-to-work"));
      if ((plugin.getConfig().getBoolean("per-world")) && 
        (p.getWorld() == world) && 
        (e.getInventory().getName().equals(plugin.getConfig().getString("lobby-preferences-menu-name").replace("&", "§"))) && 
        (e.getSlot() == 20 || e.getSlot() == 11) && (
        (e.isRightClick()) || (e.isLeftClick())))
        if (!Main.Stacker.contains(p)) {
          e.setCancelled(true);
          p.playNote(p.getLocation(), Instrument.PIANO, new Note(15));
          Main.Stacker.add(p);
        } else if (((e.isRightClick()) || (e.isLeftClick())) && 
          (Main.Stacker.contains(p))) {
          e.setCancelled(true);
          p.playNote(p.getLocation(), Instrument.PIANO, new Note(0));
          Main.Stacker.remove(p);
        }
    }
}