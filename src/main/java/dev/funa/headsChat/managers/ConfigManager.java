package dev.funa.headsChat.managers;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.managers.configs.V2Migrator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final HeadsChat plugin;
    public static int version = 2;
    private FileConfiguration config;

    public String chatFormatString;
    public String joinFormatString;
    public String leaveFormatString;

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

        if (config.getInt("config-version", version) < 2) {
            plugin.getLogger().info("Old config version detected, migrating to v2...");
            // Perform migration steps here if needed
            V2Migrator.migrate(config);
            config.set("config-version", version);
            saveConfig();
            plugin.getLogger().info("Config migration complete.");
        }

        chatFormatString = config.getString("format.chat");
        joinFormatString = config.getString("format.join");
        leaveFormatString = config.getString("format.leave");
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
}
