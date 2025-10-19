package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.enums.ChatColorPermission;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private final HeadsChat plugin;
    private static final Pattern COLOR_PATTERN = Pattern.compile("&([0-9a-fk-or])", Pattern.CASE_INSENSITIVE);

    public ChatListener(HeadsChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        String content = plugin.getConfigManager().chatFormatString
                .replace("{message}", event.getMessage());

        Component full = Formatter.parseText(content, player, plugin);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("headschat.muted")) {
                p.sendMessage(full);
            }
        }
        if (plugin.getConfigManager().logMessages()) {
            plugin.getLogger().info(player.getName() + ": " + event.getMessage());
        }
    }

    private String applyColorPermissions(Player player, String input) {
        Matcher matcher = COLOR_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

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
