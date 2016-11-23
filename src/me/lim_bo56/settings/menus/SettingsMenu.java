package me.lim_bo56.settings.menus;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.config.MenuConfiguration;
import me.lim_bo56.settings.player.CustomPlayer;
import me.lim_bo56.settings.utils.Cache;
import me.lim_bo56.settings.utils.ItemFactory;
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

import java.util.List;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:22 AM
 */

@SuppressWarnings("unused")
public class SettingsMenu implements Listener {

    public static void openSettings(final Player p) {
        CustomPlayer cPlayer = new CustomPlayer(p);

        List<String> enabledLore = Cache.ENABLED_LORE;
        String[] array = new String[enabledLore.size()];
        enabledLore.toArray(array);

        List<String> disabledLore = Cache.DISABLED_LORE;
        String[] array2 = new String[disabledLore.size()];
        disabledLore.toArray(array2);

        Inventory Settings = Bukkit.createInventory(null, 54, MenuConfiguration.get("Menu.Name"));

        if (cPlayer.hasJump()) {

            Settings.setItem(12, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0)));
            Settings.setItem(21, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(12, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0));
            Settings.setItem(21, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasSpeed()) {

            Settings.setItem(10, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0)));
            Settings.setItem(19, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(10, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0));
            Settings.setItem(19, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasFly()) {

            Settings.setItem(14, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0)));
            Settings.setItem(23, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(14, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0));
            Settings.setItem(23, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasVanish()) {

            Settings.setItem(16, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0)));
            Settings.setItem(25, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(16, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0));
            Settings.setItem(25, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasStacker()) {

            Settings.setItem(29, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0)));
            Settings.setItem(38, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(29, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0));
            Settings.setItem(38, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasVisibility()) {

            Settings.setItem(31, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0)));
            Settings.setItem(40, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(31, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0));
            Settings.setItem(40, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        if (cPlayer.hasChat()) {

            Settings.setItem(33, Core.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0)));
            Settings.setItem(42, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));

        } else {

            Settings.setItem(33, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0));
            Settings.setItem(42, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));

        }

        p.openInventory(Settings);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        CustomPlayer cPlayer = new CustomPlayer(player);

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (event.getInventory().getTitle().equalsIgnoreCase(MenuConfiguration.get("Menu.Name"))) {
                    if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (event.isRightClick() || event.isLeftClick()) {
                            event.setCancelled(true);

                            if (event.getSlot() == 10 || event.getSlot() == 19) {

                                if (player.hasPermission(Cache.SPEED_PERMISSION)) {

                                    if (cPlayer.hasSpeed()) {

                                        cPlayer.setSpeed(false);
                                        player.removePotionEffect(PotionEffectType.SPEED);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        openSettings(player);

                                    } else if (!cPlayer.hasSpeed()) {

                                        cPlayer.setSpeed(true);
                                        player.addPotionEffect(Cache.SPEED);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        openSettings(player);

                                    }

                                } else if (!player.hasPermission(Cache.SPEED_PERMISSION)) {

                                    player.sendMessage(Cache.NO_PERMISSIONS);

                                }
                            }

                            if (event.getSlot() == 12 || event.getSlot() == 21) {

                                if (player.hasPermission(Cache.JUMP_PERMISSION)) {

                                    if (cPlayer.hasJump()) {

                                        cPlayer.setJump(false);
                                        player.removePotionEffect(PotionEffectType.JUMP);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        openSettings(player);

                                    } else if (!cPlayer.hasJump()) {

                                        cPlayer.setJump(true);
                                        player.addPotionEffect(Cache.JUMP);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        openSettings(player);

                                    }

                                } else if (!player.hasPermission(Cache.JUMP_PERMISSION)) {

                                    player.sendMessage(Cache.NO_PERMISSIONS);

                                }
                            }

                            if (event.getSlot() == 14 || event.getSlot() == 23) {

                                if (player.hasPermission(Cache.FLY_PERMISSION)) {

                                    if (cPlayer.hasFly()) {

                                        cPlayer.setFly(false);
                                        player.setAllowFlight(false);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        openSettings(player);

                                    } else if (!cPlayer.hasFly()) {

                                        cPlayer.setFly(true);
                                        player.setAllowFlight(true);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        openSettings(player);

                                    }

                                } else if (!player.hasPermission(Cache.FLY_PERMISSION)) {

                                    player.sendMessage(Cache.NO_PERMISSIONS);

                                }
                            }

                            if (event.getSlot() == 16 || event.getSlot() == 25) {

                                if (player.hasPermission(Cache.VANISH_PERMISSION)) {

                                    if (cPlayer.hasVanish()) {

                                        cPlayer.setVanish(false);
                                        for (Player players : Bukkit.getOnlinePlayers()) {
                                            players.showPlayer(player);
                                        }
                                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                        openSettings(player);

                                    } else if (!cPlayer.hasVanish()) {

                                        cPlayer.setVanish(true);
                                        for (Player players : Bukkit.getOnlinePlayers()) {
                                            players.hidePlayer(player);
                                        }
                                        player.addPotionEffect(Cache.INVISIBILITY);
                                        player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                        openSettings(player);

                                    }

                                } else if (!player.hasPermission(Cache.VANISH_PERMISSION)) {

                                    player.sendMessage(Cache.NO_PERMISSIONS);

                                }
                            }

                            if (event.getSlot() == 29 || event.getSlot() == 38) {

                                if (cPlayer.hasStacker()) {

                                    cPlayer.setStacker(false);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    openSettings(player);

                                } else if (!cPlayer.hasStacker()) {

                                    cPlayer.setStacker(true);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    openSettings(player);

                                }
                            }

                            if (event.getSlot() == 31 || event.getSlot() == 40) {

                                if (cPlayer.hasVisibility()) {

                                    cPlayer.setVisibility(false);
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        player.hidePlayer(players);
                                    }
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    openSettings(player);

                                } else if (!cPlayer.hasVisibility()) {

                                    cPlayer.setVisibility(true);
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        CustomPlayer oPlayer = new CustomPlayer(players);

                                        if (!oPlayer.hasVanish()) {
                                            player.showPlayer(players);
                                        }

                                    }
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    openSettings(player);

                                }
                            }

                            if (event.getSlot() == 33 || event.getSlot() == 42) {

                                if (cPlayer.hasChat()) {

                                    cPlayer.setChat(false);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    openSettings(player);

                                } else if (!cPlayer.hasChat()) {

                                    cPlayer.setChat(true);
                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    openSettings(player);

                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
