package dev.funa.chatHeads;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChatHeads extends JavaPlugin {

    public static String chatMessage;

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");
        ConfigManager configManager = new ConfigManager(this);
        configManager.setup();
        chatMessage = configManager.getConfig().getString("message", "§f<%head% %player%> §7%message%");
        getLogger().info("Custom chat loaded: " + chatMessage);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getLogger().info("ChatHeads has been enabled!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
