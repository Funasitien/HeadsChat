package dev.funa.headsChat.managers.configs;

import dev.funa.headsChat.utils.YAML;
import org.bukkit.configuration.file.FileConfiguration;

public class V2Migrator {
    public static void migrate(FileConfiguration config) {
        // Migration logic for version 2
        YAML.moveYamlKey(config, "format.chat", "chat.format");
        YAML.moveYamlKey(config, "format.join", "join.format");
        YAML.moveYamlKey(config, "format.leave", "leave.format");
        YAML.moveYamlKey(config, "format.enable-join-formatting", "join.enabled");
        YAML.moveYamlKey(config, "format.enable-leave-formatting", "leave.enabled");

        YAML.createKeyIfNotExists(config, "chat.enabled", true);
        YAML.createKeyIfNotExists(config, "chat.logging", true);

        YAML.removeYamlKey(config, "format");
    }
}
