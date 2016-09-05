package me.lim_bo56.settings.menus;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.managers.MenuManager;
import me.lim_bo56.settings.managers.MessageManager;
import me.lim_bo56.settings.objects.CustomPlayer;
import me.lim_bo56.settings.runnable.OpenMenu;
import me.lim_bo56.settings.utils.ColorUtils;
import me.lim_bo56.settings.utils.ItemFactory;
import me.lim_bo56.settings.utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:22 AM
 */

// FIXME: 9/3/2016
public class SettingsMenu implements Listener {

    private static ConfigurationManager menu = ConfigurationManager.getMenu();
    private static String invName = MenuManager.get("Menu.Name");
    private Core plugin = Core.getInstance();

    public static void openSettings(final Player p) {
        CustomPlayer cPlayer = new CustomPlayer(p);

        List<String> enabledLore = ConfigurationManager.getMenu().getStringList("Menu.Items.Enabled.Lore");
        String[] array = new String[enabledLore.size()];
        enabledLore.toArray(array);

        List<String> disabledLore = ConfigurationManager.getMenu().getStringList("Menu.Items.Disabled.Lore");
        String[] array2 = new String[disabledLore.size()];
        disabledLore.toArray(array2);

        Inventory Settings = Bukkit.createInventory(null, 54, invName);

        // TODO: Finish fill with glass option.

        if (cPlayer.hasJump()) {

            Settings.setItem(12, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Jump.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Jump.Material")), 1, 0)));
            Settings.setItem(21, ItemFactory.createItem(MenuManager.get("Menu.Items.Enabled.Name"), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(12, ItemFactory.createItem(MenuManager.get("Menu.Items.Jump.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Jump.Material")), 1, 0));
            Settings.setItem(21, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasSpeed()) {

            Settings.setItem(10, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuManager.get("Menu.Items.Speed.Material")), 1, 0)));
            Settings.setItem(19, ItemFactory.createItem(ColorUtils.Color(MenuManager.get("Menu.Items.Enabled.Name")), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(10, ItemFactory.createItem(MenuManager.get("Menu.Items.Speed.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Speed.Material")), 1, 0));
            Settings.setItem(19, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasFly()) {

            Settings.setItem(14, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Fly.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Fly.Material")), 1, 0)));
            Settings.setItem(23, ItemFactory.createItem(MenuManager.get("Menu.Items.Enabled.Name"), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(14, ItemFactory.createItem(MenuManager.get("Menu.Items.Fly.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Fly.Material")), 1, 0));
            Settings.setItem(23, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasVanish()) {

            Settings.setItem(16, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Vanish.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Vanish.Material")), 1, 0)));
            Settings.setItem(25, ItemFactory.createItem(MenuManager.get("Menu.Items.Enabled.Name"), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(16, ItemFactory.createItem(MenuManager.get("Menu.Items.Vanish.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Vanish.Material")), 1, 0));
            Settings.setItem(25, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasStacker()) {

            Settings.setItem(29, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Stacker.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Stacker.Material")), 1, 0)));
            Settings.setItem(38, ItemFactory.createItem(MenuManager.get("Menu.Items.Enabled.Name"), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(29, ItemFactory.createItem(MenuManager.get("Menu.Items.Stacker.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Stacker.Material")), 1, 0));
            Settings.setItem(38, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasVisibility()) {

            Settings.setItem(31, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Visibility.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Visibility.Material")), 1, 0)));
            Settings.setItem(40, ItemFactory.createItem(MenuManager.get("Menu.Items.Enabled.Name"), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(31, ItemFactory.createItem(MenuManager.get("Menu.Items.Visibility.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Visibility.Material")), 1, 0));
            Settings.setItem(40, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasChat()) {

            Settings.setItem(33, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuManager.get("Menu.Items.Chat.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Chat.Material")), 1, 0)));
            Settings.setItem(42, ItemFactory.createItem(MenuManager.get("Menu.Items.Enabled.Name"), true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(33, ItemFactory.createItem(MenuManager.get("Menu.Items.Chat.Name"), false, Material.getMaterial((String) menu.get("Menu.Items.Chat.Material")), 1, 0));
            Settings.setItem(42, ItemFactory.createItem(MenuManager.get("Menu.Items.Disabled.Name"), true, Material.INK_SACK, 1, 8, array));

        }

        p.openInventory(Settings);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        CustomPlayer cPlayer = new CustomPlayer(player);
        BukkitScheduler scheduler = Bukkit.getScheduler();

        if (menu.getStringList("worlds-allowed").contains(player.getWorld().getName())) {

            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (event.getInventory().getTitle().equalsIgnoreCase(invName)) {
                    if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (event.isRightClick() || event.isLeftClick()) {
                            event.setCancelled(true);

                            if (event.getSlot() == 10 || event.getSlot() == 19) {

                                if (player.hasPermission("settings.speed")) {

                                    if (cPlayer.hasSpeed()) {

                                        cPlayer.setSpeed(false);
                                        player.removePotionEffect(PotionEffectType.SPEED);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                    } else if (!cPlayer.hasSpeed()) {

                                        cPlayer.setSpeed(true);
                                        player.addPotionEffect(Variables.SPEED);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                    }

                                } else if (!player.hasPermission("settings.speed")) {

                                    player.sendMessage(MessageManager.getMessage("No-Permissions"));

                                }
                            }

                            if (event.getSlot() == 12 || event.getSlot() == 21) {

                                if (player.hasPermission("settings.jump")) {

                                    if (cPlayer.hasJump()) {

                                        cPlayer.setJump(false);
                                        player.removePotionEffect(PotionEffectType.JUMP);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 2);

                                    } else if (!cPlayer.hasJump()) {

                                        cPlayer.setJump(true);
                                        player.addPotionEffect(Variables.JUMP);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 2);

                                    }

                                } else if (!player.hasPermission("settings.jump")) {

                                    player.sendMessage(MessageManager.getMessage("No-Permissions"));

                                }
                            }

                            if (event.getSlot() == 14 || event.getSlot() == 23) {

                                if (player.hasPermission("settings.fly")) {

                                    if (cPlayer.hasFly()) {

                                        cPlayer.setFly(false);
                                        player.setAllowFlight(false);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                    } else if (!cPlayer.hasFly()) {

                                        cPlayer.setFly(true);
                                        player.setAllowFlight(true);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                    }

                                } else if (!player.hasPermission("settings.fly")) {

                                    player.sendMessage(MessageManager.getMessage("No-Permissions"));

                                }
                            }

                            if (event.getSlot() == 16 || event.getSlot() == 25) {

                                if (player.hasPermission("settings.vanish")) {

                                    if (cPlayer.hasVanish()) {

                                        cPlayer.setVanish(false);
                                        for (Player players : Bukkit.getOnlinePlayers()) {
                                            players.showPlayer(player);
                                        }
                                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                    } else if (!cPlayer.hasVanish()) {

                                        cPlayer.setVanish(true);
                                        for (Player players : Bukkit.getOnlinePlayers()) {
                                            players.hidePlayer(player);
                                        }
                                        player.addPotionEffect(Variables.INVISIBILITY);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                    }

                                } else if (!player.hasPermission("settings.vanish")) {

                                    player.sendMessage(MessageManager.getMessage("No-Permissions"));

                                }
                            }

                            if (event.getSlot() == 29 || event.getSlot() == 38) {

                                if (cPlayer.hasStacker()) {

                                    cPlayer.setStacker(false);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                } else if (!cPlayer.hasStacker()) {

                                    cPlayer.setStacker(true);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                }
                            }

                            if (event.getSlot() == 31 || event.getSlot() == 40) {

                                if (cPlayer.hasVisibility()) {

                                    cPlayer.setVisibility(false);
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        player.hidePlayer(players);
                                    }
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                } else if (!cPlayer.hasVisibility()) {

                                    cPlayer.setVisibility(true);
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        CustomPlayer oPlayer = new CustomPlayer(players);

                                        if (!oPlayer.hasVanish()) {
                                            player.showPlayer(players);
                                        }

                                    }
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                }
                            }

                            if (event.getSlot() == 33 || event.getSlot() == 42) {

                                if (cPlayer.hasChat()) {

                                    cPlayer.setChat(false);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                } else if (!cPlayer.hasChat()) {

                                    cPlayer.setChat(true);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    scheduler.runTaskLater(plugin, new OpenMenu(player), 1);

                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
