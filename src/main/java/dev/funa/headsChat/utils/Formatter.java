package dev.funa.headsChat.utils;

import dev.funa.headsChat.HeadsChat;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Formatter {
    public static Component parseText(String text, Player player, HeadsChat plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return plugin.getMiniMessage().deserialize(
                    PlaceholderAPI.setPlaceholders(player,
                            text
                                    .replace("&", "§")
                                    .replace("{player}",
                                            player.getPlayerListName())
                                    .replace("{head}", "<head:" + player.getName() + ">")
                    )
            );
        } else {
            return plugin.getMiniMessage().deserialize(
                    text
                            .replace("&", "§")
                            .replace("{player}",
                                    player.getPlayerListName())
                            .replace("{head}", "<head:" + player.getName() + ">")
            );
        }
    }
}
