package dev.funa.headsChat.integration;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class FloodgateHook {

    private static final FloodgateApi API = FloodgateApi.getInstance();

    public static String parse(String prepared, Player player, String fallbackChar) {
        if (API.isFloodgatePlayer(player.getUniqueId())) {
            return prepared.replace("{head}", fallbackChar);
        } else {
            return prepared.replace("{head}", "<head:" + player.getUniqueId() + ">");
        }
    }
}
