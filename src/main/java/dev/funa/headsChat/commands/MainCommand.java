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

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            // Hardcoded help message using MiniMessage with hex colors
            Component help = MiniMessage.miniMessage().deserialize(
                    """
                            <#f6da71>ʜᴇᴀᴅѕᴄʜᴀᴛ</#f6da71> <gray>•</gray> <#f6da71>v{version}</#f6da71>
                          
                            <#f6da71>/headschat reload</#f6da71><gray>:</gray> <white>Reload the config file.</white>
                          
                            <#f6da71> </#f6da71>
                            <#718df6>ᴄʀᴇᴀᴛᴇᴅ ʙʏ ғᴜɴᴀѕɪᴛɪᴇɴ - @ғᴜɴᴀѕɪᴛɪᴇɴ</#718df6>
                          """.replace("{version}", HeadsChat.version)
            );
            plugin.getAdventure().sender(sender).sendMessage(help);
            return true;
        }
        // Fix: if the first argument is 'reload', perform reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("headschat.reload") || sender.isOp()) {
                plugin.reloadConfigFile();
                Component reloaded = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "Configuration reloaded.");
                plugin.getAdventure().sender(sender).sendMessage(reloaded);
            } else {
                Component noPerm = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>You do not have permission to execute this command.</#ff5555>");
                plugin.getAdventure().sender(sender).sendMessage(noPerm);
            }
            return true;
        }

        // Unknown subcommand
        Component unknown = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>Unknown subcommand. Use /headschat for help.</#ff5555>");
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
