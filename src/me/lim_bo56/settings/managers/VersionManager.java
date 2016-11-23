package me.lim_bo56.settings.managers;

import me.lim_bo56.settings.version.IItemGlower;
import me.lim_bo56.settings.version.IMount;

public class VersionManager {

    private String serverVersion;

    private IItemGlower itemGlower;
    private IMount mount;

    public VersionManager(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public boolean load() {

        switch (serverVersion) {
            case "v1_8_R1":
                itemGlower = new src.me.lim_bo56.settings.v1_8_R1.ItemGlower();
                return true;
            case "v1_8_R2":
                itemGlower = new src.me.lim_bo56.settings.v1_8_R2.ItemGlower();
                return true;
            case "v1_8_R3":
                itemGlower = new src.me.lim_bo56.settings.v1_8_R3.ItemGlower();
                return true;
            case "v1_9_R1":
                itemGlower = new src.me.lim_bo56.settings.v1_9_R1.ItemGlower();
                mount = new src.me.lim_bo56.settings.v1_9_R1.Mount();
                return true;

            case "v1_9_R2":
                itemGlower = new src.me.lim_bo56.settings.v1_9_R2.ItemGlower();
                mount = new src.me.lim_bo56.settings.v1_9_R2.Mount();
                return true;

            case "v1_10_R1":
                itemGlower = new src.me.lim_bo56.settings.v1_10_R1.ItemGlower();
                mount = new src.me.lim_bo56.settings.v1_10_R1.Mount();
                return true;
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
