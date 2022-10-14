package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSetting.STACKER_SETTING;

import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.event.SettingUpdateEvent;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import me.limbo56.playersettings.util.Version;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class StackerSettingListener implements Listener {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerStack(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Fix bug in 1.9+ where this event is fired twice due to OFF_HAND being added
    if (!Version.getCurrentVersion().isOlderThan("1.9")
        && event.getHand() == EquipmentSlot.OFF_HAND) {
      return;
    }

    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    String stackerSettingName = STACKER_SETTING.getSetting().getName();
    Entity clicked = event.getRightClicked();
    boolean hasStackerDisabled = !user.hasSettingEnabled(stackerSettingName);
    boolean isTargetNotAPlayer = !(clicked instanceof Player);
    boolean isTargetAnNPC = isMarkedAsNPC(clicked);
    // Don't send message if stacker is disabled and the target is not a player
    if (hasStackerDisabled && (isTargetNotAPlayer || isTargetAnNPC)) {
      return;
    }
    // Notify user that they have stacker disabled
    if (hasStackerDisabled) {
      Text.fromMessages("stacker.self-disabled")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }
    // Notify user that the target is not a player
    if (isTargetNotAPlayer || isTargetAnNPC) {
      Text.fromMessages("stacker.target-invalid-entity")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Alert the user that the target has the stacker setting disabled
    SettingUser targetUser = plugin.getUserManager().getUser(clicked.getUniqueId());
    if (!targetUser.hasSettingEnabled(stackerSettingName)) {
      Text.fromMessages("stacker.target-disabled")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    player.setPassenger(clicked);
  }

  @EventHandler
  public void onPlayerLaunch(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (!PlayerSettingsProvider.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    Entity target = player.getPassenger();
    boolean isTargetNotAPlayer = !(target instanceof Player);
    boolean actionIsNotLeftClick = event.getAction() != Action.LEFT_CLICK_AIR;
    boolean playerHasNoPassenger = player.getPassenger() == null;
    if (isTargetNotAPlayer
        || actionIsNotLeftClick
        || playerHasNoPassenger
        || eitherHasStackerDisabled(player, target)) {
      return;
    }

    Vector direction = player.getLocation().getDirection();
    target.getVehicle().eject();
    target.setVelocity(direction.multiply(new Vector(1, 2, 1)));
    target.setFallDistance(-10000.0F);
  }

  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    boolean isDamagerNotAPlayer = !(damager instanceof Player);
    if (isDamagerNotAPlayer
        || !PlayerSettingsProvider.isAllowedWorld(damager.getWorld().getName())) {
      return;
    }

    Entity target = event.getEntity();
    boolean isTargetNotAPlayer = !(target instanceof Player);
    boolean vehicleIsNotDamager = target.getVehicle() != damager;
    if (isTargetNotAPlayer
        || isMarkedAsNPC(target)
        || vehicleIsNotDamager
        || eitherHasStackerDisabled(damager, target)) {
      return;
    }

    event.setCancelled(true);
  }

  @EventHandler
  public void onStackerDisable(SettingUpdateEvent event) {
    Player player = event.getPlayer();
    if (event.getNewValue() == 1) {
      return;
    }

    Entity vehicle = player.getVehicle();
    if (vehicle != null) {
      vehicle.removePassenger(player);
    }

    Entity passenger = player.getPassenger();
    if (passenger != null) {
      player.eject();
    }
  }

  private boolean eitherHasStackerDisabled(Entity player, Entity target) {
    SettingUser playerUser = plugin.getUserManager().getUser(player.getUniqueId());
    SettingUser targetUser = plugin.getUserManager().getUser(target.getUniqueId());
    String stackerSettingName = STACKER_SETTING.getSetting().getName();
    return !playerUser.hasSettingEnabled(stackerSettingName)
        || !targetUser.hasSettingEnabled(stackerSettingName);
  }

  private boolean isMarkedAsNPC(Entity entity) {
    return plugin.getPluginConfiguration().getStringList("general.npc-metadata").stream()
        .anyMatch(entity::hasMetadata);
  }
}
