package dev.funa.headsChat.integration;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private static Chat chat;

    public static boolean setup() {
        RegisteredServiceProvider<Chat> rsp =
                Bukkit.getServicesManager().getRegistration(Chat.class);

        if (rsp == null) return false;

        chat = rsp.getProvider();
        return chat != null;
    }

    public static String getPrefix(Player player) {
        if (chat == null) return "";
        return chat.getPlayerPrefix(player).replace("&", "§");
    }

    public static String getSuffix(Player player) {
        if (chat == null) return "";
        return chat.getPlayerSuffix(player).replace("&", "§");
    }

}
