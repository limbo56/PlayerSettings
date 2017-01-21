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
        CustomPlayer cPlayer = new CustomPlayer(player);

        boolean radio = Utilities.hasRadioPlugin();

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

                            if (radio ? (event.getSlot() == 28 || event.getSlot() == 37) : (event.getSlot() == 29 || event.getSlot() == 38)) {

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

                            if (radio ? (event.getSlot() == 30 || event.getSlot() == 39) : (event.getSlot() == 31 || event.getSlot() == 40)) {

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

                            if (radio ? (event.getSlot() == 32 || event.getSlot() == 41) : (event.getSlot() == 33 || event.getSlot() == 42)) {

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

                            if (radio) {

                                if (event.getSlot() == 34 || event.getSlot() == 43) {

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


}
