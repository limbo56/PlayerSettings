package me.limbo56.settings.menu;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
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
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Speed.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Speed.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasSpeed();
        }
    },
    JUMP {
        @Override
        void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Jump.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Jump.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasJump();
        }
    },
    FLY {
        @Override
        void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasFly();
        }
    },
    VANISH {
        @Override
        void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasVanish();
        }
    },
    STACKER {
        @Override
        void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasStacker();
        }
    },
    VISIBILITY {
        @Override
        void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Visibility.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Visibility.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasVisibility();
        }
    },
    CHAT {
        @Override
        void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot") + 9);
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot") + 9);
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasChat();
        }
    },
    RADIO {
        @Override
        void setEnabled(Inventory inventory) {
            if (radio) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Radio.Enabled")) {
                    inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot"), PlayerSettings.getItemGlower().glow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Radio.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Radio.Material")), 1, 0)));
                    setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot") + 9);
                }
            }
        }

        @Override
        void setDisabled(Inventory inventory) {
            if (radio) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Radio.Enabled")) {
                    inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Radio.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Radio.Material")), 1, 0));
                    setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot") + 9);
                }
            }
        }

        @Override
        boolean getCondition(Player player) {
            CustomPlayer cPlayer = Utilities.getOrCreateCustomPlayer(player);
            return cPlayer.hasRadio();
        }
    };

    public List<String> enabledLore = Cache.ENABLED_LORE;
    public List<String> disabledLore = Cache.DISABLED_LORE;

    public String[] enabledLoreArray = new String[enabledLore.size()];
    public String[] disabledLoreArray = new String[disabledLore.size()];

    public boolean radio = Utilities.hasRadioPlugin();

    public void setTrue(Inventory inventory, int slot) {
        enabledLore.toArray(enabledLoreArray);
        inventory.setItem(slot, ItemFactory.createItem(Cache.ENABLED_NAME, true, Cache.ENABLED_MATERIAL, 1, 10, enabledLoreArray));
    }

    public void setFalse(Inventory inventory, int slot) {
        disabledLore.toArray(disabledLoreArray);
        inventory.setItem(slot, ItemFactory.createItem(Cache.DISABLED_NAME, true, Cache.DISABLED_MATERIAL, 1, 8, disabledLoreArray));
    }

    abstract void setEnabled(Inventory inventory);

    abstract void setDisabled(Inventory inventory);

    abstract boolean getCondition(Player player);

}