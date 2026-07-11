package dev.funa.headsChat.listeners;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.enums.ChatColorPermission;
import dev.funa.headsChat.utils.Formatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
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
        if (!plugin.getConfigManager().getConfig().getBoolean("chat.enabled")) {
            return;
        }
        Player sender = event.getPlayer();
        if (sender.hasPermission("headschat.muted") && !sender.isOp()) {
            event.setCancelled(true);
            sender.sendMessage(HeadsChat.prefix + "You have been muted. You will not be able to send messages.");
            return;
        }
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
            String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

            // Cast viewer to Player safely. If it's the console, it becomes null.
            Player viewerPlayer = viewer instanceof Player target ? target : null;

            // Pass the viewerPlayer into your formatter
            Component rendered = Formatter.parseText(
                    plugin.getConfigManager().chatFormatString,
                    source,
                    viewerPlayer,
                    plugin,
                    plainMessage
            );

            if (viewerPlayer != null && plainMessage.contains(viewerPlayer.getName())) {
                viewerPlayer.playSound(
                        viewerPlayer.getLocation(),
                        plugin.getConfigManager().getMentionSound(),
                        SoundCategory.MASTER, // or SoundCategory.PLAYERS, SoundCategory.RECORDS, etc.
                        1.0f,                 // Volume
                        1.0f                  // Pitch
                );
            }

            // Because the renderer is called for each person, this 'rendered' component
            // will only have the yellow highlight for the person who was mentioned!
            return rendered;
        });

        if (plugin.getConfigManager().logMessages()) {
            plugin.getLogger().info(sender.getName() + ": " + PlainTextComponentSerializer.plainText().serialize(event.message()));
        }
    }

}
