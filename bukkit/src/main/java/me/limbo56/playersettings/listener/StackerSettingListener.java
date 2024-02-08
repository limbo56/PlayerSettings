package me.limbo56.playersettings.listener;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.event.SettingUpdateEvent;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.setting.Settings;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.Messages;
import me.limbo56.playersettings.util.Messenger;
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
  private final SettingsManager settingsManager;
  private final UserManager userManager;
  private final PluginConfiguration pluginConfiguration;
  private final MessagesConfiguration messagesConfiguration;
  private final Messenger messenger;

  public StackerSettingListener() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.userManager = plugin.getUserManager();
    this.pluginConfiguration = plugin.getConfiguration();
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
    this.messenger = plugin.getMessenger();
  }

  @EventHandler
  public void onPlayerStack(PlayerInteractAtEntityEvent event) {
    String stackerSettingName = Settings.stacker().getName();
    if (!settingsManager.isRegistered(stackerSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!pluginConfiguration.isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Fix bug in 1.9+ where this event is fired twice due to OFF_HAND
    if (!Version.getServerVersion().isOlderThan("1.9")
        && event.getHand() == EquipmentSlot.OFF_HAND) {
      return;
    }

    // Don't send message if stacker is disabled and the target is not a player
    SettingUser user = userManager.getUser(player.getUniqueId());
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
      Setting setting = settingsManager.getSetting(stackerSettingName);
      messenger.sendMessage(player, Messages.getSettingDisabledMessage(player, setting));
      return;
    }

    // Notify user that the target is not a player
    if (isTargetNotAPlayer || isTargetAnNPC) {
      messenger.sendMessage(
          player, messagesConfiguration.getMessage("stacker.target-invalid-entity"));
      return;
    }

    // Alert the user that the target has the stacker setting disabled
    SettingUser targetUser = userManager.getUser(clicked.getUniqueId());
    if (!targetUser.hasSettingEnabled(stackerSettingName)) {
      messenger.sendMessage(player, messagesConfiguration.getMessage("stacker.target-disabled"));
      return;
    }

    player.setPassenger(clicked);
  }

  @EventHandler
  public void onPlayerLaunch(EntityDamageByEntityEvent event) {
    String stackerSettingName = Settings.stacker().getName();
    if (!settingsManager.isRegistered(stackerSettingName)) {
      return;
    }

    Entity damager = event.getDamager();
    Entity damaged = event.getEntity();
    if (!(damager instanceof Player) || !(damaged instanceof Player)) {
      return;
    }

    if (!pluginConfiguration.isAllowedWorld(damager.getWorld().getName())) {
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
    if (!event.getSetting().getName().equals(Settings.stacker().getName())) {
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
    SettingUser playerUser = userManager.getUser(player.getUniqueId());
    SettingUser targetUser = userManager.getUser(target.getUniqueId());
    String stackerSettingName = Settings.stacker().getName();
    return !playerUser.hasSettingEnabled(stackerSettingName)
        || !targetUser.hasSettingEnabled(stackerSettingName);
  }

  private boolean isMarkedAsNPC(Entity entity) {
    for (String metadata : pluginConfiguration.getNPCMetadata()) {
      if (entity.hasMetadata(metadata)) {
        return true;
      }
    }
    return false;
  }
}
