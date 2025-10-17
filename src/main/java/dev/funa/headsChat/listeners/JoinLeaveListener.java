package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.utils.StringOps;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
    private final HeadsChat plugin;

    public JoinLeaveListener(HeadsChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.getConfigManager().isJoinFormatingEnabled()) {
            event.setJoinMessage(null);
            String[] s = StringOps.splitAround(plugin.getConfigManager().joinFormatString, "{head}");

            String before = s[0];
            String after = s[1];
            before = before.replace("{player}", event.getPlayer().getName()).replace("&", "§");
            after = after.replace("{player}", event.getPlayer().getName()).replace("&", "§");

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                before = PlaceholderAPI.setPlaceholders(event.getPlayer(), before).replace("&", "§");
                after = PlaceholderAPI.setPlaceholders(event.getPlayer(), after).replace("&", "§");
            }

            // Build the JSON string for the chat message
            String json = """
                    [{"text":"{before}"},{"type":"object","object":"player","player":{"name":"{player}"},"hat":true},{"text":"{after}"}]
                    """
                    .replace("{player}", event.getPlayer().getName())
                    .replace("{before}", before)
                    .replace("{after}", after);
            Component full = GsonComponentSerializer.gson().deserialize(json);

            Bukkit.broadcast(full);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (plugin.getConfigManager().isLeaveFormatingEnabled()) {
            event.setQuitMessage(null);
            String[] s = StringOps.splitAround(plugin.getConfigManager().leaveFormatString, "{head}");

            String before = s[0];
            String after = s[1];
            before = before.replace("{player}", event.getPlayer().getName()).replace("&", "§");
            after = after.replace("{player}", event.getPlayer().getName()).replace("&", "§");

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                before = PlaceholderAPI.setPlaceholders(event.getPlayer(), before).replace("&", "§");
                after = PlaceholderAPI.setPlaceholders(event.getPlayer(), after).replace("&", "§");
            }

            // Build the JSON string for the chat message
            String json = """
                    [{"text":"{before}"},{"type":"object","object":"player","player":{"name":"{player}"},"hat":true},{"text":"{after}"}]
                    """
                    .replace("{player}", event.getPlayer().getName())
                    .replace("{before}", before)
                    .replace("{after}", after);
            Component full = GsonComponentSerializer.gson().deserialize(json);

            Bukkit.broadcast(full);
        }
    }

}
