package dev.funa.chatHeads;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChatHeads extends JavaPlugin {

    private ConfigManager configManager;
    public static String chatMessage;

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");
        this.configManager = new ConfigManager(this);
        configManager.setup();
        chatMessage = configManager.getConfig().getString("message", "§f<%head% %player%> §7%message%");
        getLogger().info("Custom chat loaded: " + chatMessage);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getLogger().info("ChatHeads has been enabled!");

    }

    public ConfigManager getConfigManager() { return configManager; }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
