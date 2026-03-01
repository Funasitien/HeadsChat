package dev.funa.headsChat.utils;

import dev.funa.headsChat.HeadsChat;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Formatter {

    public static Component parseText(String text, Player player, HeadsChat plugin) {
        return parseText(text, player, plugin, null);
    }

    public static Component parseText(String text, Player player, HeadsChat plugin, String message) {
        String prepared = text
                .replace("&", "§")
                .replace("{player}", player.getPlayerListName())
                .replace("{head}", "<head:" + player.getName() + ">");

        if (message != null) {
            prepared = prepared.replace("{message}", message);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            prepared = PlaceholderAPI.setPlaceholders(player, prepared);
        }

        return plugin.getMiniMessage().deserialize(prepared);
    }
}
