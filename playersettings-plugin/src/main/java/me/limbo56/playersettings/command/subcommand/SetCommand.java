package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.command.CommandBase;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.utils.PlayerUtils;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetCommand extends CommandBase {
    public SetCommand() {
        super(3, "set", "<setting> <value>", "Sets the value of a specified setting", null);
    }

    @Override
    protected void executeCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String settingName = args[1];
        String value = args[2];

        PlayerSettings plugin = PlayerSettings.getPlugin();
        Setting setting = plugin.getSetting(settingName);

        if (setting == null) {
            PlayerUtils.sendConfigMessage(player, "commands.settingNotFound");
            return;
        }

        if (!value.equals("on") && !value.equals("off")) {
            PlayerUtils.sendConfigMessage(player, "commands.acceptedValues");
            return;
        }

        SPlayer sPlayer = plugin.getSPlayer(player.getUniqueId());
        SettingWatcher settingWatcher = sPlayer.getSettingWatcher();
        settingWatcher.setValue(setting, value.equals("on"), false);

        try{
            Sound pianoSound;

            // Check if they have new sound system
            if (Arrays.stream(Sound.values()).anyMatch(sound -> sound.toString().equals("BLOCK_NOTE_HARP")))
                pianoSound = Sound.valueOf("BLOCK_NOTE_HARP");
            else if (Arrays.stream(Sound.values()).anyMatch(sound -> sound.toString().equals("BLOCK_NOTE_BLOCK_HARP")))
                pianoSound = Sound.valueOf("BLOCK_NOTE_BLOCK_HARP");
            else
                pianoSound = Sound.NOTE_PIANO;

            player.playSound(player.getLocation(), pianoSound, 1, value.equals("on") ? 1 : -99);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        PlayerUtils.sendConfigMessage(player, "commands.setSetting", message ->
                messageModifier(message, settingName, value)
        );
    }

    private String messageModifier(String message, String settingName, String settingValue) {
        return message.replaceAll("%name%", settingName)
                .replaceAll("%value%", settingValue);
    }
}
