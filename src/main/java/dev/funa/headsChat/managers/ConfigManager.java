package dev.funa.headsChat.managers;

import dev.funa.headsChat.HeadsChat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final HeadsChat plugin;
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
        chatFormatString = config.getString("format.chat");
        joinFormatString = config.getString("format.join");
        leaveFormatString = config.getString("format.leave");
        plugin.getLogger().info("Config loaded!" + config.getString("message"));
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Boolean isJoinFormatingEnabled() {
        return config.getBoolean("format.enable-join-formatting");
    }

    public Boolean isLeaveFormatingEnabled() {
        return config.getBoolean("format.enable-leave-formatting");
    }

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
