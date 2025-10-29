package dev.funa.headsChat.managers.configs;

import org.bukkit.configuration.file.FileConfiguration;

public class VXMigrator {
    public static void migrate(int version, FileConfiguration config) {
        // Migration logic for version X
        if (version == 2) {
            V2Migrator.migrate(config);
        } else if (version == 3) {
            V3Migrator.migrate(config);
        }
    }
}
