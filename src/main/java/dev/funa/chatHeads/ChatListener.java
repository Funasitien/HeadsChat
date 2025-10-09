package dev.funa.chatHeads;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        // Get the message format from config
        String s = ChatHeads.chatMessage;
        String[] parts = s.split("%head%");
        // Format before and after the head placeholder
        String before;
        String after;
        if (parts.length != 2) {
            before = event.getPlayer().getName();
            after = event.getMessage();
        } else {
            before = parts[0].replace("%player%", event.getPlayer().getName()).replace("%message%", event.getMessage());
            after =  parts[1].replace("%player%", event.getPlayer().getName()).replace("%message%", event.getMessage());
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            before = PlaceholderAPI.setPlaceholders(event.getPlayer(), before);
            after = PlaceholderAPI.setPlaceholders(event.getPlayer(), after);
        }

        // Build the JSON string for the chat message
        String json = """
                [{"text":"%before%"},{"type":"object","object":"player","player":{"name":"%player%"},"hat":true},{"text":"%after%"}]
                """
            .replace("%player%", event.getPlayer().getName())
            .replace("%before%", before)
            .replace("%after%", after);
        Component full = GsonComponentSerializer.gson().deserialize(json);

        Bukkit.broadcast(full);
    }
}
