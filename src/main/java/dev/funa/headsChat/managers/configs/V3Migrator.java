package dev.funa.headsChat.managers.configs;

import dev.funa.headsChat.utils.YAML;
import org.bukkit.configuration.file.FileConfiguration;

public class V3Migrator {
    public static void migrate(FileConfiguration config) {
        // Migration logic for version 3

        // Cooldown settings
        YAML.createKeyIfNotExists(config, "chat.cooldown", 0);
        YAML.createKeyIfNotExists(config, "chat.cooldown-message",
                "<red>You are sending messages too quickly! Please wait {time} seconds.");
        YAML.createKeyIfNotExists(config, "chat.cooldown-bypass-permission", "headschat.bypass.cooldown");

        // Name display settings
        YAML.createKeyIfNotExists(config, "nick.enabled",true);
        YAML.createKeyIfNotExists(config, "nick.dialog.name", "Change your Display Name");
        YAML.createKeyIfNotExists(config, "nick.dialog.confirm", "<green>Confirm");
        YAML.createKeyIfNotExists(config, "nick.dialog.cancel", "<red>Cancel");
        YAML.createKeyIfNotExists(config, "nick.dialog.confirm-tooltip", "Click to confirm your new display name");
        YAML.createKeyIfNotExists(config, "nick.dialog.cancel-tooltip", "Click to cancel changing your display name");

        YAML.createKeyIfNotExists(config, "dm.enabled", true);
        YAML.createKeyIfNotExists(config, "dm.format",  "[<white>{head} {player} -> {target_head} {target}]: <gray>{message}");
    }
}
