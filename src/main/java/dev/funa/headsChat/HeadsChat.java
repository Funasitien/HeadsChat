package dev.funa.headsChat;

import dev.funa.headsChat.commands.MainCommand;
import dev.funa.headsChat.commands.MessageCommand;
import dev.funa.headsChat.listeners.ChatListener;
import dev.funa.headsChat.listeners.JoinLeaveListener;
import dev.funa.headsChat.managers.ConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

public final class HeadsChat extends JavaPlugin {

    private static ConfigManager configManager;
    private MiniMessage mm = MiniMessage.miniMessage();
    public static String prefix = "<#f6da71>ʜᴇᴀᴅѕᴄʜᴀᴛ</#f6da71> <gray>•</gray> ";
    public static String version = "1.2.0";
    public MainCommand mainCommandExecutor;

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");
        configManager = new ConfigManager(this);
        configManager.setup();
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        if (configManager.getConfig().getBoolean("join.enabled")) {
            getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
        }
        getLogger().info("Listeners loaded.");
        this.mainCommandExecutor = new MainCommand(this);
        this.getCommand("headschat").setExecutor(this.mainCommandExecutor);
        if (configManager.getConfig().getBoolean("dm.enabled")) {
            this.getCommand("dm").setExecutor(new MessageCommand(this));
        }
        getLogger().info("All commands loaded.");
        getLogger().info("HeadsChat has been enabled!");

    }

    public void reloadConfigFile() {
        reloadConfig();
        getLogger().info("Configuration reloaded!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MiniMessage getMiniMessage() {
        return mm;
    }


    @Override
    public void onDisable() {
    }
}
