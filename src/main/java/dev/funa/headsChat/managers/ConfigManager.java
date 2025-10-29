package dev.funa.headsChat.managers;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.managers.configs.V2Migrator;
import dev.funa.headsChat.managers.configs.VXMigrator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final HeadsChat plugin;
    public static int version = 3;
    private FileConfiguration config;

    public String chatFormatString;
    public String joinFormatString;
    public String leaveFormatString;
    public String dmFormatString;

    public ConfigManager(HeadsChat plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);

        for (int i = config.getInt("config-version", 1); i < version; i++) {
            plugin.getLogger().info("Migrating config " +
                    "from v" + i + " to v" + (i + 1) + "...");
            VXMigrator.migrate(i + 1, config);
            config.set("config-version", i + 1);
            plugin.getLogger().info("Config migration to "+ (i + 1) +" complete.");
            saveConfig();
        }

        chatFormatString = config.getString("chat.format");
        joinFormatString = config.getString("join.format");
        leaveFormatString = config.getString("leave.format");
        dmFormatString  = config.getString("dm.format");
        plugin.getLogger().info("Config loaded!" + chatFormatString);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Boolean isJoinFormatingEnabled() {
        return config.getBoolean("join.enabled");
    }

    public Boolean isLeaveFormatingEnabled() {
        return config.getBoolean("leave.enabled");
    }

    public Boolean logMessages() { return config.getBoolean("chat.logging"); }

    public int getCooldownSeconds() { return config.getInt("chat.cooldown", 0);}
    public String getCooldownMessage() { return config.getString("chat.cooldown-message","<red>You are on cooldown for {seconds} more seconds.");}
    public String getCooldownPermission() { return config.getString("chat.cooldown-bypass-permission","headschat.bypass.cooldown");}

    public String getRenameDialogName() { return config.getString("nick.dialog.name", "Change your Display Name");}
    public String getRenameDialogConfirm() { return config.getString("nick.dialog.confirm", "Confirm");}
    public String getRenameDialogCancel() { return config.getString("nick.dialog.cancel", "Cancel");}
    public String getRenameDialogPrompt() { return config.getString("nick.dialog.prompt", "Choose a new name");}
    public String getRenameDialogCancelTooltip() { return config.getString("nick.dialog.cancel-tooltip", "Click to cancel changing your display name");}
    public String getRenameDialogConfirmTooltip() { return config.getString("nick.dialog.confirm-tooltip", "Click to confirm your new display name");}

    public void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    }

    public boolean isRenameEnabled() {
        return config.getBoolean("nick.enabled", true);
    }
}
