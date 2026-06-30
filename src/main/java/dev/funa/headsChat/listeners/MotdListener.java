package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.utils.Formatter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MotdListener implements Listener {
    private final HeadsChat plugin;

    public MotdListener(HeadsChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerJoin(PlayerJoinEvent event) {
        if (!plugin.getConfigManager().isMotdEnabled()) {
            return;
        }

        Player p =  event.getPlayer();

        for (String l : plugin.getConfigManager().getMotdFormat()) {
            Component rendered = Formatter.parseText(
                    plugin.getConfigManager().chatFormatString,
                    p,
                    plugin,
                    l
            );
            p.sendMessage(rendered);
        }
    }
}
