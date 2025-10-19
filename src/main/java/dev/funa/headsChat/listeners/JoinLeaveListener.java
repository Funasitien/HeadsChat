package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.utils.Formatter;
import dev.funa.headsChat.utils.StringOps;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

            Component full = Formatter.parseText(
                    plugin.getConfigManager().joinFormatString,
                    event.getPlayer(),
                    plugin
            );

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("headschat.muted")) {
                    p.sendMessage(full);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (plugin.getConfigManager().isLeaveFormatingEnabled()) {
            event.setQuitMessage(null);
            Component full = Formatter.parseText(
                    plugin.getConfigManager().leaveFormatString,
                    event.getPlayer(),
                    plugin
            );

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.hasPermission("headschat.muted")) {
                    p.sendMessage(full);
                }
            }
        }
    }

}
