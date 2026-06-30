package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.utils.Formatter;
import net.kyori.adventure.text.Component;
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
        Player p = event.getPlayer();
        if (plugin.getConfigManager().isRenameEnabled()) {
            Component customName = plugin.renameCommandExecutor.getCustomName(p);
            p.displayName(customName);
            p.playerListName(customName);
        }
        if (plugin.getConfigManager().isJoinFormatingEnabled()) {
            event.setJoinMessage(null);

            Component full = Formatter.parseText(
                    plugin.getConfigManager().joinFormatString,
                    event.getPlayer(),
                    plugin
            );

            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.hasPermission("headschat.hidden")) continue;
                pl.sendMessage(full);
            }
        }

        if (p.isOp()) {
            if (plugin.update) {
                p.sendMessage(HeadsChat.prefix + "A new update is available! Download it here: https://modrinth.com/plugin/headschat/");
            }
        } else if (p.hasPermission("headschat.muted")) {
            p.sendMessage(HeadsChat.prefix + "You have been muted. You will not be able to send messages.");
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
