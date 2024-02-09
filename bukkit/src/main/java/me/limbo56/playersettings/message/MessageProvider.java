package me.limbo56.playersettings.message;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.message.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.message.text.Text;
import me.limbo56.playersettings.util.Adventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;

public class MessageProvider {
  private final MessagesConfiguration messagesConfiguration;

  public MessageProvider(MessagesConfiguration messagesConfiguration) {
    this.messagesConfiguration = messagesConfiguration;
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
    ClickEvent clickEvent =
        ClickEvent.clickEvent(
            ClickEvent.Action.RUN_COMMAND, "/settings set " + setting.getName() + " 1");
    HoverEvent<Component> hoverEvent =
        HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, enableActionTooltip);
    return Text.create(messagesConfiguration.getMessage("settings.enable-action"), modifier)
        .asTextComponent()
        .clickEvent(clickEvent)
        .hoverEvent(hoverEvent);
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
