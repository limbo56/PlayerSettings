package me.limbo56.playersettings.message;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.message.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.message.text.Text;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.util.Adventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

public class MessageProvider {
  private final MessagesConfiguration messagesConfiguration;

  public MessageProvider(PlayerSettings plugin) {
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
  }

  public Text getInvalidSettingLevelMessage(Player player, InternalSetting setting) {
    Collection<Integer> settingValues = setting.getAllowedValues(player);
    String acceptedValues =
        Stream.concat(
                settingValues.stream().map(String::valueOf),
                setting.getAliases(settingValues).stream())
            .collect(Collectors.joining(", "));
    return Text.create(
        messagesConfiguration.getMessage("commands.setting-invalid-level"),
        ReplaceModifier.of("%values%", acceptedValues));
  }

  public String getCommandHelpMessage(Player player, List<SubCommand> commands) {
    StringJoiner message = new StringJoiner("\n");
    message.add(getCommandHelpHeader() + "\n");
    for (SubCommand command : commands) {
      message.add(buildCommandHelpMessage(player, command));
    }
    return message.toString();
  }

  public String getCommandHelpMessage(Player player, SubCommand command) {
    return getCommandHelpMessage(player, Collections.singletonList(command));
  }

  private String getCommandHelpHeader() {
    return Text.create(
            messagesConfiguration.getMessage(
                "commands.help-header", "&e------- &6Settings Help &e---------"))
        .getFirstTextLine();
  }

  private String buildCommandHelpMessage(Player player, SubCommand command) {
    String usage = command.getUsage();
    usage = usage.isEmpty() ? "" : " " + usage;

    return Text.create(
            messagesConfiguration.getMessage(
                "commands.help-command-format", "&f/settings %command% &8: &7%description%"),
            new PlaceholderAPIModifier(player),
            ReplaceModifier.of("%command%", command.getName() + usage),
            ReplaceModifier.of("%description%", command.getDescription()))
        .getFirstTextLine();
  }

  public Component getSettingDisabledMessage(Player player, Setting setting) {
    PlaceholderAPIModifier modifier = new PlaceholderAPIModifier(player);
    Component enableActionTooltip = createEnableActionTooltip(setting, modifier);
    TextComponent enableActionMessage =
        createEnableActionMessage(setting, modifier, enableActionTooltip);
    return createDisabledMessage(setting, modifier, enableActionMessage);
  }

  private TextComponent createEnableActionMessage(
      Setting setting, PlaceholderAPIModifier modifier, Component enableActionTooltip) {
    HoverEvent<Component> hoverEvent =
        HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, enableActionTooltip);
    ClickEvent clickEvent =
        ClickEvent.clickEvent(
            ClickEvent.Action.RUN_COMMAND, "/settings set " + setting.getName() + " 1");
    return Text.create(messagesConfiguration.getMessage("settings.enable-action"), modifier)
        .asTextComponent()
        .hoverEvent(hoverEvent)
        .clickEvent(clickEvent);
  }

  private Component createEnableActionTooltip(Setting setting, PlaceholderAPIModifier modifier) {
    return Text.create(
            messagesConfiguration.getMessageList("settings.enable-action-tooltip"), modifier)
        .asTextComponent()
        .replaceText(createSettingReplacement(setting));
  }

  private Component createDisabledMessage(
      Setting setting, PlaceholderAPIModifier modifier, TextComponent enableActionMessage) {
    return Text.create(messagesConfiguration.getMessage("settings.self-disabled"), modifier)
        .asTextComponent()
        .replaceText(createSettingReplacement(setting))
        .replaceText(Adventure.createReplacement("%enableAction%", enableActionMessage));
  }

  private TextReplacementConfig createSettingReplacement(Setting setting) {
    return Adventure.createReplacement(
        "%setting%", Adventure.fromLegacyAmpersand(setting.getDisplayName()));
  }

  public Text getMessagePrefix() {
    return Text.create(messagesConfiguration.getMessage("prefix"));
  }
}
