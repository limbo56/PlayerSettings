package me.limbo56.playersettings.api;

/**
 * Tracks and notifies when a {@link Setting} is enabled/disabled
 *
 * @author lim_bo56
 * @since 3/6/2018
 */
public class SettingWatcher {
    private String permission;
    private boolean enabled;

    public SettingWatcher(String permission, boolean enabled) {
        this.permission = permission;
        this.enabled = enabled;
    }

    /**
     * Changes the state of the setting to enabled/disabled
     *
     * @param enabled Whether or not the setting is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return Permission required to enable and disable setting
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @return If the setting is enabled or disabled
     */
    public boolean isEnabled() {
        return enabled;
    }
}
