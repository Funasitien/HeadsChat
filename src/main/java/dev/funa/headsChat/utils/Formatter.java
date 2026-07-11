package dev.funa.headsChat.utils;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.integration.FloodgateHook;
import dev.funa.headsChat.integration.VaultHook;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.regex.Pattern;

public class Formatter {

    // Regex to match legacy colors (&0-&9, &a-&f, &k-&o, &r) case-insensitively
    private static final Pattern LEGACY_PATTERN = Pattern.compile("&(?=[0-9a-fk-orA-FK-OR])");

    public static Component parseText(String text, Player sender, Player viewer, HeadsChat plugin) {
        return parseText(text, sender, viewer, plugin, null);
    }

    public static Component parseText(String text, Player sender, Player viewer, HeadsChat plugin, String message) {
        // ONLY replace & if it precedes a valid legacy color character
        String prepared = LEGACY_PATTERN.matcher(text).replaceAll("§")
                .replace("{player}", sender.getPlayerListName())
                .replace("{prefix}", VaultHook.getPrefix(sender))
                .replace("{suffix}", VaultHook.getSuffix(sender));

        String fallbackString = "";
        prepared = FloodgateHook.parse(prepared, sender, viewer, fallbackString);

        if (message != null) {
            prepared = prepared.replace("{message}", message);
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            prepared = PlaceholderAPI.setPlaceholders(sender, prepared);
        }

        if (prepared.contains("§")) {
            prepared = legacyToMiniMessage(prepared);
        }

        return plugin.getMiniMessage().deserialize(prepared);
    }

    public static Component parseText(String text, Player player, HeadsChat plugin, String message) {
        String prepared = LEGACY_PATTERN.matcher(text).replaceAll("§")
                .replace("{player}", player.getPlayerListName())
                .replace("{prefix}", VaultHook.getPrefix(player))
                .replace("{suffix}", VaultHook.getSuffix(player));

        prepared = prepared.replace("{head}", "<head:" + player.getName() + ">");

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

    public static Component parseText(String msg, Player sender, HeadsChat plugin) {
        return parseText(msg, sender, sender, plugin);
    }
}