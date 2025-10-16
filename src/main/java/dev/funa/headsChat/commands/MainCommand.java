package dev.funa.headsChat.commands;

import dev.funa.headsChat.HeadsChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final @NotNull HeadsChat plugin;

    public MainCommand(@NotNull HeadsChat plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            // Hardcoded help message using MiniMessage with hex colors
            Component help = MiniMessage.miniMessage().deserialize(
                    "<#1abc9c>[HeadsChat]</#1abc9c> Plugin by <#ffcc00>FunaDev</#ffcc00>. Use <#00ffcc>/headschat reload</#00ffcc> to reload the configuration."
            );
            plugin.getAdventure().sender(sender).sendMessage(help);
            return true;
        }
        // Fix: if the first argument is 'reload', perform reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("headschat.reload") || sender.isOp()) {
                plugin.reloadConfigFile();
                Component reloaded = MiniMessage.miniMessage().deserialize("<#00ff00>[HeadsChat]</#00ff00> Configuration reloaded.");
                plugin.getAdventure().sender(sender).sendMessage(reloaded);
            } else {
                Component noPerm = MiniMessage.miniMessage().deserialize("<#ff0000>You do not have permission to execute this command.</#ff0000>");
                plugin.getAdventure().sender(sender).sendMessage(noPerm);
            }
            return true;
        }

        // Unknown subcommand
        Component unknown = MiniMessage.miniMessage().deserialize("<#ff5555>Unknown subcommand. Use /headschat for help.</#ff5555>");
        plugin.getAdventure().sender(sender).sendMessage(unknown);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of(
                "reload"
        );
    }
}
