package dev.funa.chatHeads;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final ChatHeads plugin;

    public ChatListener(ChatHeads plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        // Build the player head component (your new object)
        String json = """
            {"type":"object","object":"player","player":{"name":"%player%"},"hat":true}
            """.replace("%player%", event.getPlayer().getName());

        Component head = GsonComponentSerializer.gson().deserialize(json);

        // Build the username
        Component name = Component.text(event.getPlayer().getName())
                .color(NamedTextColor.WHITE);

        // Build the actual chat message
        Component message = Component.text(": " + event.getMessage())
                .color(NamedTextColor.GRAY);

        // Combine them → [Head][Name]: message
        Component full = Component.empty()
                .append(head)
                .append(Component.space())
                .append(name)
                .append(message);

        Bukkit.broadcast(full);
    }
}
