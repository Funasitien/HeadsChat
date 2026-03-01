package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.enums.ChatColorPermission;
import dev.funa.headsChat.utils.Formatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private final HeadsChat plugin;
    private static final Pattern COLOR_PATTERN = Pattern.compile("&([0-9a-fk-or])", Pattern.CASE_INSENSITIVE);

    private final Map<UUID, Long> lastMessageTimestamps = new ConcurrentHashMap<>();

    public ChatListener(HeadsChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();
        UUID uuid = sender.getUniqueId();

        long now = System.currentTimeMillis();
        long cooldownSeconds = plugin.getConfigManager().getCooldownSeconds();
        long cooldownMs = cooldownSeconds * 1000L;

        Long last = lastMessageTimestamps.get(uuid);
        if (last != null
                && now - last < cooldownMs
                && !sender.hasPermission(plugin.getConfigManager().getCooldownPermission())) {

            event.setCancelled(true);

            long timeLeft = (cooldownMs - (now - last)) / 1000;
            String msg = plugin.getConfigManager().getCooldownMessage().replace("{time}", String.valueOf(timeLeft));
            Component cooldownMessage = Formatter.parseText(msg, sender, plugin);
            sender.sendMessage(cooldownMessage);
            return;
        }

        lastMessageTimestamps.put(uuid, now);

        event.renderer((source, sourceDisplayName, message, viewer) -> {
            // {message} as plain text (what player typed)
            String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

            Component rendered = Formatter.parseText(
                    plugin.getConfigManager().chatFormatString,
                    sender,
                    plugin,
                    plainMessage
            );

            // Optional ping sound (per-viewer). Renderer can be called async -> schedule sync for sounds.
            if (viewer instanceof Player target) {
                if (!target.hasPermission("headschat.muted") || target.isOp()) {
                    if (plainMessage.contains(target.getName())) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                                target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1)
                        );
                    }
                }
            }

            return rendered;
        });

        if (plugin.getConfigManager().logMessages()) {
            plugin.getLogger().info(sender.getName() + ": " + PlainTextComponentSerializer.plainText().serialize(event.message()));
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
                matcher.appendReplacement(sb, "");
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
