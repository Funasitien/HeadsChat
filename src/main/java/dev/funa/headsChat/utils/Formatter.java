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

        if (prepared.contains("§")) {
            prepared = legacyToMiniMessage(prepared);
        }

        return plugin.getMiniMessage().deserialize(prepared);
    }

    private static String legacyToMiniMessage(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);

            if (current == '§' && i + 1 < input.length()) {
                char code = Character.toLowerCase(input.charAt(i + 1));

                switch (code) {
                    case '0' -> result.append("<black>");
                    case '1' -> result.append("<dark_blue>");
                    case '2' -> result.append("<dark_green>");
                    case '3' -> result.append("<dark_aqua>");
                    case '4' -> result.append("<dark_red>");
                    case '5' -> result.append("<dark_purple>");
                    case '6' -> result.append("<gold>");
                    case '7' -> result.append("<gray>");
                    case '8' -> result.append("<dark_gray>");
                    case '9' -> result.append("<blue>");
                    case 'a' -> result.append("<green>");
                    case 'b' -> result.append("<aqua>");
                    case 'c' -> result.append("<red>");
                    case 'd' -> result.append("<light_purple>");
                    case 'e' -> result.append("<yellow>");
                    case 'f' -> result.append("<white>");
                    case 'k' -> result.append("<obfuscated>");
                    case 'l' -> result.append("<bold>");
                    case 'm' -> result.append("<strikethrough>");
                    case 'n' -> result.append("<underlined>");
                    case 'o' -> result.append("<italic>");
                    case 'r' -> result.append("<reset>");
                    default -> {
                        result.append(current);
                        continue;
                    }
                }

                i++;
                continue;
            }

            result.append(current);
        }

        return result.toString();
    }
}
