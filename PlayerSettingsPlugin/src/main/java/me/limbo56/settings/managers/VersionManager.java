package me.limbo56.settings.managers;

import me.limbo56.settings.version.IItemGlower;
import me.limbo56.settings.version.IMount;

public class VersionManager {

    private String serverVersion;

    private IItemGlower itemGlower;
    private IMount mount;

    public VersionManager(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public void load() {
        try {
            Class<?> itemGlowerClass = Class.forName("me.limbo56.settings.nms." + serverVersion + ".ItemGlower");

            if (IItemGlower.class.isAssignableFrom(itemGlowerClass)) {
                this.itemGlower = (IItemGlower) itemGlowerClass.getConstructor().newInstance();
            }

            if (serverVersion.contains("1_9")) {
                Class<?> mountClass = Class.forName("me.limbo56.settings.nms." + serverVersion + ".Mount");

                if (IMount.class.isAssignableFrom(mountClass)) {
                    this.mount = (IMount) mountClass.getConstructor().newInstance();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public IItemGlower getItemGlower() {
        return itemGlower;
    }

    public IMount getMount() {
        return mount;
    }
}
