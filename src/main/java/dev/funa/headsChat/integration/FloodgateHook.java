package dev.funa.headsChat.integration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class FloodgateHook {

    public static String parse(String prepared, Player sender, Player viewer, String fallbackChar) {
        boolean isViewerBedrock = false;

        // Safely check if Floodgate is enabled and if the viewer is actually a player
        if (viewer != null && Bukkit.getPluginManager().isPluginEnabled("floodgate")) {
            isViewerBedrock = FloodgateApi.getInstance().isFloodgatePlayer(viewer.getUniqueId());
        }

        // If the viewer is Bedrock OR if the viewer is the Console (null)
        if (isViewerBedrock || viewer == null) {
            return prepared.replace("{head}", fallbackChar);
        } else {
            // Java viewer: Keep the head tag of the SENDER
            return prepared.replace("{head}", "<head:" + sender.getName() + ">");
        }
    }
}