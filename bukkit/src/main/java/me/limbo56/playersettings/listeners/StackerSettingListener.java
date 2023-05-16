package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSettings.STACKER_SETTING;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.event.SettingUpdateEvent;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import me.limbo56.playersettings.util.Version;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class StackerSettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerStack(PlayerInteractAtEntityEvent event) {
    String stackerSettingName = STACKER_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(stackerSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Fix bug in 1.9+ where this event is fired twice due to OFF_HAND
    if (!Version.getCurrentVersion().isOlderThan("1.9")
        && event.getHand() == EquipmentSlot.OFF_HAND) {
      return;
    }

    // Don't send message if stacker is disabled and the target is not a player
    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    Entity clicked = event.getRightClicked();
    boolean hasStackerDisabled = !user.hasSettingEnabled(stackerSettingName);
    boolean isTargetNotAnEntity = !(clicked instanceof LivingEntity);
    boolean isTargetNotAPlayer = !(clicked instanceof Player);
    boolean isTargetAnNPC = isMarkedAsNPC(clicked);
    if (isTargetNotAnEntity || hasStackerDisabled && (isTargetNotAPlayer || isTargetAnNPC)) {
      return;
    }

    // Notify user that they have stacker disabled
    if (hasStackerDisabled) {
      Text.fromMessages("stacker.self-disabled")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    // Notify user that the target is not a player
    if (isTargetNotAPlayer || isTargetAnNPC) {
      Text.fromMessages("stacker.target-invalid-entity")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    // Alert the user that the target has the stacker setting disabled
    SettingUser targetUser = PLUGIN.getUserManager().getUser(clicked.getUniqueId());
    if (!targetUser.hasSettingEnabled(stackerSettingName)) {
      Text.fromMessages("stacker.target-disabled")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    player.setPassenger(clicked);
  }

  @EventHandler
  public void onPlayerLaunch(EntityDamageByEntityEvent event) {
    String stackerSettingName = STACKER_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(stackerSettingName)) {
      return;
    }

    Entity damager = event.getDamager();
    Entity damaged = event.getEntity();
    if (!(damager instanceof Player) || !(damaged instanceof Player)) {
      return;
    }

    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(damager.getWorld().getName())) {
      return;
    }

    Entity target = damager.getPassenger();
    if (target == null || !target.equals(damaged) || eitherHasStackerDisabled(damager, target)) {
      return;
    }

    Vector direction = damager.getLocation().getDirection();
    target.getVehicle().eject();
    target.setVelocity(direction.multiply(1.2));
    target.setFallDistance(-10000.0F);
    event.setCancelled(true);
  }

  @EventHandler
  public void onStackerDisable(SettingUpdateEvent event) {
    if (!event.getSetting().getName().equals(STACKER_SETTING.getName())) {
      return;
    }

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
    SettingUser playerUser = PLUGIN.getUserManager().getUser(player.getUniqueId());
    SettingUser targetUser = PLUGIN.getUserManager().getUser(target.getUniqueId());
    String stackerSettingName = STACKER_SETTING.getName();
    return !playerUser.hasSettingEnabled(stackerSettingName)
        || !targetUser.hasSettingEnabled(stackerSettingName);
  }

  private boolean isMarkedAsNPC(Entity entity) {
    return PLUGIN.getPluginConfiguration().getFile().getStringList("general.npc-metadata").stream()
        .anyMatch(entity::hasMetadata);
  }
}
