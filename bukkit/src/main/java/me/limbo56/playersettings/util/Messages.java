package me.limbo56.playersettings.util;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.util.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.util.text.TextMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.RegExp;

public class Messages {
  private static final PlayerSettings PLUGIN = PlayerSettings.getInstance();
  public static final String INTERNAL_PREFIX = "&f&l(&6PlayerSettings&f&l) &f";

  private Messages() {}

  public static CompletableFuture<List<String>> getVersionMessage() {
    return PluginUpdater.getLatestVersion()
        .thenApplyAsync(Messages::composeVersionMessage)
        .exceptionally(
            exception -> {
              PluginLogger.severe("An error occurred while checking for updates", exception);
              return Collections.singletonList("&cAn error occurred while checking for updates!");
            });
  }

  public static Component getSettingDisabledMessage(Player player, Setting setting) {
    TextReplacementConfig settingReplacement =
        createReplacement("%setting%", Text.fromLegacyAmpersand(setting.getDisplayName()));
    Component enableActionTooltip =
        fromMessagesList(player, "settings.enable-action-tooltip").replaceText(settingReplacement);
    ClickEvent clickEvent =
        ClickEvent.clickEvent(
            ClickEvent.Action.RUN_COMMAND, "/settings set " + setting.getName() + " 1");
    TextComponent enableActionMessage =
        fromMessages(player, "settings.enable-action")
            .clickEvent(clickEvent)
            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, enableActionTooltip));

    return getMessagePrefix(player)
        .append(fromMessages(player, "settings.self-disabled"))
        .replaceText(settingReplacement)
        .replaceText(createReplacement("%enableAction%", enableActionMessage));
  }

  private static List<String> composeVersionMessage(String version) {
    String currentVersionNumber = PLUGIN.getDescription().getVersion();
    Version latestVersion = Version.from(version);
    Version currentVersion = Version.from(currentVersionNumber);

    if (version.equals("ERROR")) {
      return Collections.singletonList("&cAn error occurred while checking for updates!");
    } else if (currentVersion.isOlderThan(latestVersion)) {
      return Lists.newArrayList(
          "New version available &av" + version + " &r(Using &av" + currentVersionNumber + "&r)",
          "Download the latest update at:",
          "&6https://www.spigotmc.org/resources/player-settings.14622/");
    } else if (latestVersion.isOlderThan(currentVersion)) {
      return Collections.singletonList("Using unknown version &cv" + currentVersionNumber);
    } else {
      return Collections.singletonList("Using latest version &6v" + currentVersionNumber);
    }
  }

  private static TextComponent getMessagePrefix(Player player) {
    return fromMessages(player, "prefix");
  }

  private static TextComponent fromMessages(Player player, String path) {
    return createTextComponent(player, (config, builder) -> builder.from(config.getMessage(path)));
  }

  private static TextComponent fromMessagesList(Player player, String path) {
    return createTextComponent(
        player, (config, builder) -> builder.from(config.getMessageList(path)));
  }

  private static TextComponent createTextComponent(
      Player player,
      BiFunction<MessagesConfiguration, TextMessage.Builder, TextMessage> messageRetriever) {
    MessagesConfiguration messagesConfiguration = getMessagesConfiguration();
    TextMessage.Builder defaultBuilder =
        TextMessage.builder().addModifier(new PlaceholderAPIModifier(player));
    return messageRetriever.apply(messagesConfiguration, defaultBuilder).asTextComponent();
  }

  private static TextReplacementConfig createReplacement(
      @RegExp String pattern, TextComponent replacement) {
    return TextReplacementConfig.builder().match(pattern).replacement(replacement).build();
  }

  private static MessagesConfiguration getMessagesConfiguration() {
    return PLUGIN.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
  }
}
