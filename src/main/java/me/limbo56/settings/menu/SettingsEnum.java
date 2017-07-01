package me.limbo56.settings.menu;

import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Cache;
import me.limbo56.settings.utils.ItemFactory;
import me.limbo56.settings.utils.PlayerUtils;
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
public enum SettingsEnum implements Togglable {

    SPEED {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Speed.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Speed.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Speed.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Speed.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Speed.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getSpeed();
        }
    },
    JUMP {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Jump.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Jump.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Jump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Jump.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Jump.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getJump();
        }
    },
    FLY {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Fly.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Fly.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Fly.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getFly();
        }
    },
    VANISH {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Vanish.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Vanish.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Vanish.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Vanish.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getVanish();
        }
    },
    DOUBLEJUMP {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.DoubleJump.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.DoubleJump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.DoubleJump.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.DoubleJump.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.DoubleJump.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.DoubleJump.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.DoubleJump.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.DoubleJump.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getDoubleJump();
        }
    },
    STACKER {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Stacker.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Stacker.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Stacker.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Stacker.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getStacker();
        }
    },
    VISIBILITY {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Visibility.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Visibility.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Visibility.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Visibility.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Visibility.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getVisibility();
        }
    },
    CHAT {
        @Override
        public void setEnabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0)));
                setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot") + 9);
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Chat.Enabled")) {
                inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Chat.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Chat.Material")), 1, 0));
                setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Chat.Slot") + 9);
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getChat();
        }
    },
    RADIO {
        @Override
        public void setEnabled(Inventory inventory) {
            if (radio) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Radio.Enabled")) {
                    inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot"), ItemFactory.addGlow(ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Radio.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Radio.Material")), 1, 0)));
                    setTrue(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot") + 9);
                }
            }
        }

        @Override
        public void setDisabled(Inventory inventory) {
            if (radio) {
                if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Radio.Enabled")) {
                    inventory.setItem(ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot"), ItemFactory.createItem(MenuConfiguration.get("Menu.Items.Radio.Name"), false, Material.getMaterial(MenuConfiguration.get("Menu.Items.Radio.Material")), 1, 0));
                    setFalse(inventory, ConfigurationManager.getMenu().getInt("Menu.Items.Radio.Slot") + 9);
                }
            }
        }

        @Override
        public boolean getCondition(Player player) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);
            return cPlayer.getRadio();
        }
    };

    public List<String> enabledLore = Cache.ENABLED_LORE;
    public List<String> disabledLore = Cache.DISABLED_LORE;

    public String[] enabledLoreArray = new String[enabledLore.size()];
    public String[] disabledLoreArray = new String[disabledLore.size()];

    public boolean radio = Utilities.hasRadioPlugin();

    public void setTrue(Inventory inventory, int slot) {
        enabledLore.toArray(enabledLoreArray);
        if (Cache.ENABLED_MATERIAL.contains(":"))
            inventory.setItem(slot, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.getMaterial(Cache.ENABLED_MATERIAL.split(":")[0]), 1, Integer.valueOf(Cache.ENABLED_MATERIAL.split(":")[1]), enabledLoreArray));
        else
            inventory.setItem(slot, ItemFactory.createItem(Cache.ENABLED_NAME, true, Material.getMaterial(Cache.ENABLED_MATERIAL), 1, 0, enabledLoreArray));
    }

    public void setFalse(Inventory inventory, int slot) {
        disabledLore.toArray(disabledLoreArray);
        if (Cache.DISABLED_MATERIAL.contains(":"))
            inventory.setItem(slot, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.getMaterial(Cache.DISABLED_MATERIAL.split(":")[0]), 1, Integer.valueOf(Cache.DISABLED_MATERIAL.split(":")[1]), disabledLoreArray));
        else
            inventory.setItem(slot, ItemFactory.createItem(Cache.DISABLED_NAME, true, Material.getMaterial(Cache.DISABLED_MATERIAL), 1, 0, disabledLoreArray));
    }

}