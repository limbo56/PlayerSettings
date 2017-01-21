package me.limbo56.settings.menus;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.ItemFactory;
import me.limbo56.settings.utils.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Created by limbo56(David)
 * On 1/21/2017
 * At 12:24 AM
 */
public enum SettingsEnum {

    SPEED {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(10, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0)));
            setTrue(inventory, 19);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(10, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0));
            setFalse(inventory, 19);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasSpeed();
        }
    },
    JUMP {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(12, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0)));
            setTrue(inventory, 21);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(12, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0));
            setFalse(inventory, 21);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasJump();
        }
    },
    FLY {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(14, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0)));
            setTrue(inventory, 23);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(14, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0));
            setFalse(inventory, 23);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasFly();
        }
    },
    VANISH {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(16, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0)));
            setTrue(inventory, 25);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(16, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0));
            setFalse(inventory, 25);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasVanish();
        }
    },
    CHAT {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(radio ? 32 : 33, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0)));
            setTrue(inventory, radio ? 41 : 42);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(radio ? 32 : 33, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0));
            setFalse(inventory, radio ? 41 : 42);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasChat();
        }
    },
    VISIBILITY {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(radio ? 30 : 31, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0)));
            setTrue(inventory, radio ? 39 : 40);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(radio ? 30 : 31, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0));
            setFalse(inventory, radio ? 39 : 40);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasVisibility();
        }
    },
    STACKER {
        @Override
        void setEnabled(Inventory inventory) {
            enabledLore.toArray(array);
            inventory.setItem(radio ? 28 : 29, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0)));
            setTrue(inventory, radio ? 37 : 38);
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            inventory.setItem(radio ? 28 : 29, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0));
            setFalse(inventory, radio ? 37 : 38);
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasStacker();
        }
    },
    RADIO {
        @Override
        void setEnabled(Inventory inventory) {
            if (radio) {
                inventory.setItem(34, PlayerSettings.getInstance().getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Radio.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Radio.Material")), 1, 0)));
                setTrue(inventory, 43);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            disabledLore.toArray(array2);
            if (radio) {
                inventory.setItem(34, ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Radio.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Radio.Material")), 1, 0));
                setFalse(inventory, 43);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = new CustomPlayer(player);
            return cPlayer.hasRadio();
        }
    };

    public void setTrue(Inventory inventory, int slot) {
        enabledLore.toArray(array);
        inventory.setItem(slot, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.INK_SACK, 1, 10, array2));
    }

    public void setFalse(Inventory inventory, int slot) {
        disabledLore.toArray(array2);
        inventory.setItem(slot, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.INK_SACK, 1, 8, array));
    }

    public List<String> enabledLore = Cache.ENABLED_LORE;
    public String[] array = new String[enabledLore.size()];

    public List<String> disabledLore = Cache.DISABLED_LORE;
    public String[] array2 = new String[disabledLore.size()];

    public boolean radio = Utilities.hasRadioPlugin();

    abstract void setEnabled(Inventory inventory);

    abstract void setDisabled(Inventory inventory);

    abstract boolean getCondition(Player player);

}