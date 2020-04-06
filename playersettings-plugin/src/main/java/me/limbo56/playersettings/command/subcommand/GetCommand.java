package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetCommand extends CommandBase {
    public GetCommand() {
        super(2, "get", "<setting>", "Gets the value of a specified setting", null);
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String rawName = args[1];

        PlayerSettings plugin = PlayerSettings.getPlugin();
        Setting setting = plugin.getSetting(rawName);

        if (setting == null) {
            PlayerUtils.sendConfigMessage(player, "commands.settingNotFound");
            return;
        }

        SPlayer sPlayer = plugin.getSPlayer(player.getUniqueId());
        int settingValue = sPlayer.getSettingWatcher().getValue(setting);

        PlayerUtils.sendConfigMessage(player, "commands.getSetting", message ->
                fillPlaceholders(message, setting, settingValue)
        );
    }

    private String fillPlaceholders(String message, Setting setting, int settingValue) {
        String value = String.valueOf(settingValue);
        if (setting.getMaxValue() == 1)
            value = settingValue == 1 ? "on" : "off";
        String settingName = setting.getItem().getItemMeta().getDisplayName();
        return message.replaceAll("%name%", ChatColor.stripColor(settingName))
                .replaceAll("%value%", value);
    }
}
