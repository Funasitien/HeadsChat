package dev.funa.headsChat.managers.configs;

import dev.funa.headsChat.utils.YAML;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class V4Migrator {
    public static void migrate(FileConfiguration config) {
        YAML.createKeyIfNotExists(config, "motd.enabled", true);
        YAML.createKeyIfNotExists(config, "motd.message", List.of("<white>Welcome to the server {head} {player}!", "<gray>Have fun!"));

        YAML.createKeyIfNotExists(config, "bedrock.fallback", "");

    }
}
