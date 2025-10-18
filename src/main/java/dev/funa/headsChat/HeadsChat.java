package dev.funa.headsChat;

import dev.funa.headsChat.commands.MainCommand;
import dev.funa.headsChat.listeners.ChatListener;
import dev.funa.headsChat.listeners.JoinLeaveListener;
import dev.funa.headsChat.managers.ConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public final class HeadsChat extends JavaPlugin {

    private static ConfigManager configManager;
    private BukkitAudiences adventure;
    private MiniMessage mm = MiniMessage.miniMessage();
    public static String prefix = "<#f6da71>ʜᴇᴀᴅѕᴄʜᴀᴛ</#f6da71> <gray>•</gray> ";
    public static String version = "1.1.1";

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");
        configManager = new ConfigManager(this);
        configManager.setup();
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
        getLogger().info("Listeners loaded.");
        if (this.getCommand("headschat") != null) {
            this.getCommand("headschat").setExecutor(new MainCommand(this));
        } else {
            getLogger().warning("Command 'headschat' is not defined in plugin.yml");
        }
        getLogger().info("Reload command loaded.");
        getLogger().info("HeadsChat has been enabled!");

    }

    public void reloadConfigFile() {
        reloadConfig();
        getLogger().info("Configuration reloaded!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public BukkitAudiences getMiniMessage() {
        return mm;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}
