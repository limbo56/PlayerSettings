package me.limbo56.playersettings.listeners;

import static me.limbo56.playersettings.settings.DefaultSettings.CHAT_SETTING;

import java.util.Arrays;
import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Colors;
import me.limbo56.playersettings.util.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChatSettingListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    String chatSettingName = CHAT_SETTING.getName();
    if (!PLUGIN.getSettingsManager().isSettingRegistered(chatSettingName)) {
      return;
    }

    Player player = event.getPlayer();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(player.getWorld().getName())) {
      return;
    }

    // Remove players with chat disabled from recipients list
    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (user.hasSettingEnabled(chatSettingName)) {
      for (SettingUser settingUser : getPlayersWithChatDisabled()) {
        event.getRecipients().remove(settingUser.getPlayer());
      }
      return;
    }

    // Cancel chat event if player has chat disabled
    event.setCancelled(true);
    PlayerSettingsProvider.adventure().player(player).sendMessage(getDisabledMessage(player));
  }

  @NotNull
  private static Component getDisabledMessage(Player player) {
    String settingDisplayName =
        ChatColor.stripColor(
            Colors.translateColorCodes(CHAT_SETTING.getSetting().getDisplayName()));
    TextComponent enableActionTooltip =
        Text.fromMessages(
                "settings.enable-action-tooltip",
                Arrays.asList("&6%setting%", "&7Click to &aenable"))
            .usePlaceholder("%setting%", settingDisplayName)
            .usePlaceholderApi(player)
            .text();
    ClickEvent clickEvent =
        ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/settings set chat-setting 1");
    HoverEvent<Component> hoverEvent =
        HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, enableActionTooltip);
    TextComponent enableAction =
        Text.fromMessages("settings.enable-action", "&e(Click to enable)")
            .text()
            .clickEvent(clickEvent)
            .hoverEvent(hoverEvent);

    return LegacyComponentSerializer.legacyAmpersand()
        .deserialize(PLUGIN.getMessagesConfiguration().getMessagePrefix())
        .append(
            Text.fromMessages(
                    "settings.self-disabled",
                    "&cYou have the '&6%setting%&c' setting disabled! %enableAction%")
                .usePlaceholder("%setting%", settingDisplayName)
                .usePlaceholderApi(player)
                .text())
        .replaceText(
            TextReplacementConfig.builder()
                .match("%enableAction%")
                .replacement(enableAction)
                .build());
  }

  private Collection<SettingUser> getPlayersWithChatDisabled() {
    return PLUGIN.getUserManager().getUsersWithSettingValue(CHAT_SETTING.getName(), false);
  }
}
