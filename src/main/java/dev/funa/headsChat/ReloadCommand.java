package dev.funa.headsChat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final HeadsChat plugin;
    
    public ReloadCommand(HeadsChat plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("headschat.reload") || sender.isOp()) {
            plugin.reloadConfigFile();
            sender.sendMessage("§a[HeadsChat] Configuration reloaded.");
        } else {
            sender.sendMessage("§cYou do not have permission to execute this command.");
        }
        return true;
    }
}
