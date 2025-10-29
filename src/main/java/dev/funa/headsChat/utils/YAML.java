package dev.funa.headsChat.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class YAML {
    /**
     * Moves a value from one path to another in a Bukkit YAML file, then deletes the old one.
     *
     * @param config      The YAML file
     * @param fromPath  Original key path (e.g. "old.value")
     * @param toPath    New key path (e.g. "new.section.value")
     */
    public static void moveYamlKey(FileConfiguration config, String fromPath, String toPath) {

        // Get the value
        Object value = config.get(fromPath);
        if (value == null) {
            System.out.println("Key not found: " + fromPath);
            return;
        }

        // Set new key and remove old one
        config.set(toPath, value);
        config.set(fromPath, null);
    }

    public static void createKeyIfNotExists(FileConfiguration config, String path, Object defaultValue) {
        if (config.get(path) == null) {
            config.set(path, defaultValue);
        }
    }

    public static void removeYamlKey(FileConfiguration config, String format) {
        config.set(format, null);
    }
}
