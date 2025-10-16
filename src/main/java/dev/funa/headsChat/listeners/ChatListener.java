package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final HeadsChat plugin;

    public ChatListener(HeadsChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        // Get the message format from config
        String s = plugin.getConfigManager().chatFormatString;
        String[] parts = s.split("%head%");
        // Format before and after the head placeholder
        String before;
        String after;
        if (parts.length != 2) {
            before = event.getPlayer().getName();
            after = event.getMessage();
        } else {
            before = parts[0].replace("%player%", event.getPlayer().getName()).replace("%message%", event.getMessage()).replace("&", "§");
            after =  parts[1].replace("%player%", event.getPlayer().getName()).replace("%message%", event.getMessage()).replace("&", "§");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            before = PlaceholderAPI.setPlaceholders(event.getPlayer(), before).replace("&", "§");
            after = PlaceholderAPI.setPlaceholders(event.getPlayer(), after).replace("&", "§");
        }

        before = applyColorPermissions(player, before);
        after = applyColorPermissions(player, after);

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

    private String applyColorPermissions(Player player, String input) {
        Matcher matcher = COLOR_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            char code = Character.toLowerCase(matcher.group(1).charAt(0));
            if (ChatColorPermission.canUse(player, code)) {
                matcher.appendReplacement(sb, "§" + code);
            } else {
                matcher.appendReplacement(sb, ""); // remove unauthorized color
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
