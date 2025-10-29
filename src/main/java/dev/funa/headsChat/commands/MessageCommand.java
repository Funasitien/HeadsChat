package dev.funa.headsChat.commands;

import dev.funa.headsChat.HeadsChat;
import dev.funa.headsChat.utils.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class MessageCommand implements CommandExecutor, TabCompleter {
    private final @NotNull HeadsChat plugin;
    private final HashMap<Player, Player> lastMessaged = new HashMap<>();

    public MessageCommand(@NotNull HeadsChat plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            Component reloaded = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "Configuration reloaded.");
            sender.sendMessage(reloaded);
            return true;
        }
        if (!(sender instanceof Player p)) {
            Component onlyPlayers = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>Only players can use this command.</#ff5555>");
            sender.sendMessage(onlyPlayers);
            return true;
        }
        Player target;
        String message;
        if (args.length > 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target != null) {
                message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            } else {
                target = lastMessaged.get(p);
                message = String.join(" ", args);
            }
        } else {
            target = lastMessaged.get(p);
            message = String.join(" ", args);
        }

        if (target == null) {
            Component noRecipient = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>You need to specify a player to message or have a conversation started.</#ff5555>");
            sender.sendMessage(noRecipient);
            return true;
        }

        lastMessaged.put(p, target);
        //lastMessaged.put(target, p);

        String content = plugin.getConfigManager().dmFormatString
                .replace("{message}", message)
                .replace("{target}", target.getPlayerListName())
                .replace("{target_head}", "<head:" + p.getName() + ">");
        Component full = Formatter.parseText(content, p, plugin);
        target.sendMessage(full);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
