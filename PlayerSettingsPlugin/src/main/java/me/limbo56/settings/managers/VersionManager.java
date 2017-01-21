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

    public boolean load() {
       try {
           Class<?> itemGlower = Class.forName("me.limbo56.settings.nms." + serverVersion + ".ItemGlower");
           Class<?> mount = Class.forName("me.limbo56.settings.nms." + serverVersion + ".Mount");

           if (IItemGlower.class.isAssignableFrom(itemGlower) && IMount.class.isAssignableFrom(mount)) {
                this.itemGlower = (IItemGlower) itemGlower.getConstructor().newInstance();
                this.mount = (IMount) mount.getConstructor().newInstance();
                return true;
           }
       } catch (Exception exception) {
           exception.printStackTrace();
       }

        return false;
    }

    public IItemGlower getItemGlower() {
        return itemGlower;
    }

    public IMount getMount() {
        return mount;
    }
}
