package dev.funa.chatHeads;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import dev.funa.chatHeads.ChatListener;

public final class ChatHeads extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
