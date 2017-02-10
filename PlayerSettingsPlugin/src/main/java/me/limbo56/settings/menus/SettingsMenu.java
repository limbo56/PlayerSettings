package me.limbo56.settings.menus;

import com.statiocraft.jukebox.Shuffle;
import com.statiocraft.jukebox.scJukeBox;
import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:22 AM
 */

public class SettingsMenu implements Listener {

    public static void openSettings(final Player p) {
        Inventory inventory = Bukkit.createInventory(null, 54, MenuConfiguration.get("Menu.Name"));

        for (SettingsEnum settings : SettingsEnum.values()) {
            if (settings.getCondition(p)) {
                settings.setEnabled(inventory);
            } else {
                settings.setDisabled(inventory);
            }
        }

        p.openInventory(inventory);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (player.getOpenInventory().getType() == InventoryType.CHEST)
            if (player.getOpenInventory().getTitle().equalsIgnoreCase(MenuConfiguration.get("Menu.Name"))) {
                event.getItemDrop().remove();
                openSettings(player);
            }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);

        boolean radio = Utilities.hasRadioPlugin();

        if (Cache.WORLDS_ALLOWED.contains(player.getWorld().getName())) {

            if (event.getInventory().getType() == InventoryType.CHEST) {
                if (event.getInventory().getTitle().equalsIgnoreCase(MenuConfiguration.get("Menu.Name"))) {
                	event.setCancelled(true);
                    if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {

                    	// Speed listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Speed.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot") + 9)) {
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

                        // Jump listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Jump.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot") + 9)) {
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

                        // Fly listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot") + 9)) {
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

                        // Vanish listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot") + 9)) {
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

                        // Stacker listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot") + 9)) {
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

                        // Visibility listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Visibility.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot") + 9)) {
                                if (cPlayer.hasVisibility()) {
                                    cPlayer.setVisibility(false);

                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        player.hidePlayer(players);
                                    }

                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                    openSettings(player);
                                } else if (!cPlayer.hasVisibility()) {
                                    cPlayer.setVisibility(true);

                                    for (Player online : Bukkit.getOnlinePlayers()) {
                                        CustomPlayer oPlayer = Utilities.getOrCreateCustomPlayer(online);
                                        if (!oPlayer.hasVanish()) {
                                            player.showPlayer(online);
                                        }
                                    }

                                    player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                    openSettings(player);
                                }
                            }

                        // Chat listener
                        if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled"))
                            if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot") ||
                                    event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot") + 9)) {
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

                        // Radio listener
                        if (radio) {
                            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Radio.Enabled"))
                                if (event.getSlot() == ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot") ||
                                        event.getSlot() == (ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot") + 9)) {
                                    if (player.hasPermission(Cache.RADIO_PERMISSION)) {
                                        if (cPlayer.hasRadio()) {
                                            if (scJukeBox.getCurrentJukebox(player) != null)
                                                scJukeBox.getCurrentJukebox(player).removePlayer(player);

                                            cPlayer.setRadio(false);
                                            player.playNote(player.getLocation(), Instrument.PIANO, new Note(0));
                                            openSettings(player);
                                        } else if (!cPlayer.hasRadio()) {
                                            if (ConfigurationManager.getDefault().getInt("Radio.type") == 1)
                                                new Shuffle().addPlayer(player);
                                            else if (ConfigurationManager.getDefault().getInt("Radio.type") == 3)
                                                scJukeBox.getRadio().addPlayer(player);

                                            cPlayer.setRadio(true);
                                            player.playNote(player.getLocation(), Instrument.PIANO, new Note(15));
                                            openSettings(player);
                                        }
                                    } else if (!player.hasPermission(Cache.RADIO_PERMISSION)) {
                                        player.sendMessage(Cache.NO_PERMISSIONS);
                                    }
                                }
                        }
                    }
                }
            }
        }
    }


}
