package me.limbo56.playersettings.command.subcommand;

import com.cryptomorin.xseries.XSound;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class SetCommand extends CommandBase {

    public SetCommand() {
        super(3, "set", "<setting> <value>", "Sets the value of a specified setting", null);
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String rawName = args[1];
        String value = args[2];

        PlayerSettings plugin = PlayerSettings.getPlugin();
        Setting setting = plugin.getSetting(rawName);

        if (setting == null) {
            PlayerUtils.sendConfigMessage(player, "commands.settingNotFound");
            return;
        }

        if (!value.equals("on") && !value.equals("off")) {
            PlayerUtils.sendConfigMessage(player, "commands.acceptedValues");
            return;
        }

        if (!player.hasPermission("settings." + setting.getRawName())) {
            PlayerUtils.sendConfigMessage(player, "settings.noPermission");
            return;
        }

        // Set value
        SPlayer sPlayer = plugin.getSPlayer(player.getUniqueId());
        SettingWatcher settingWatcher = sPlayer.getSettingWatcher();
        settingWatcher.setValue(setting, value.equals("on"), false);

        // Play sound
        Sound pianoSound = XSound.BLOCK_NOTE_BLOCK_HARP.parseSound();
        player.playSound(player.getLocation(), pianoSound, 1, value.equals("on") ? 1 : -99);

        // Send change message if it's enabled
        if (!plugin.getConfiguration("messages").getBoolean("messages.sendMessageOnChange")) {
            return;
        }

        String settingName = setting.getItem().getItemMeta().getDisplayName();
        PlayerUtils.sendConfigMessage(player, "commands.setSetting", replaceVariables(value, settingName));
    }

    private Function<String, String> replaceVariables(String value, String settingName) {
        return message -> message
                .replaceAll("%name%", ChatColor.stripColor(settingName))
                .replaceAll("%value%", value);
    }
}
