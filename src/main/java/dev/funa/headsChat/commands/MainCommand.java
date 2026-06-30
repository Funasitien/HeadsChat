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
            Component help = MiniMessage.miniMessage().deserialize("""
                      <#f6da71>ʜᴇᴀᴅѕᴄʜᴀᴛ</#f6da71> <gray>•</gray> <#f6da71>v{version}</#f6da71>
                    
                      <#f6da71>/headschat reload</#f6da71><gray>:</gray> <white>Reload the config file.</white>
                      <#f6da71>/rename</#f6da71><gray>:</gray> <white>Change your display name.</white>
                    
                      <#f6da71> </#f6da71>
                      <#718df6>ᴄʀᴇᴀᴛᴇᴅ ʙʏ ғᴜɴᴀѕɪᴛɪᴇɴ - @ғᴜɴᴀѕɪᴛɪᴇɴ</#718df6>
                    """.replace("{version}", this.plugin.getPluginMeta().getVersion()));
            sender.sendMessage(help);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("headschat.reload") || sender.isOp()) {
                plugin.getConfigManager().reloadConfig();
                Component reloaded = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "Configuration reloaded.");
                sender.sendMessage(reloaded);
            } else {
                Component noPerm = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>You do not have permission to execute this command.</#ff5555>");
                sender.sendMessage(noPerm);
            }
            return true;
        }

        Component unknown = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>Unknown subcommand. Use /headschat for help.</#ff5555>");
        sender.sendMessage(unknown);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 1) {
            return List.of("reload");
        }

        return List.of();
    }
}
