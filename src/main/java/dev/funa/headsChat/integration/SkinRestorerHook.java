package dev.funa.headsChat.integration;

import dev.funa.headsChat.HeadsChat;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.event.SkinApplyEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkinRestorerHook {

    public static void init(HeadsChat plugin) {

        SkinsRestorerProvider.get().getEventBus().subscribe(plugin, SkinApplyEvent.class, event -> {

            Player player = event.getPlayer(Player.class);
            if (player == null) return;

            Bukkit.getScheduler().runTask(plugin, () -> {
                player.hidePlayer(plugin, player);
                player.showPlayer(plugin, player);
            });

        });
    }
}