package dev.funa.headsChat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class HeadExtension extends PlaceholderExpansion {

    private final HeadsChat plugin; //

    public HeadExtension(HeadsChat plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors()); //
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "hc";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getPluginMeta().getVersion(); //
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("head")) {
            return plugin.getConfig().getString("<head:" + player.getName() + ">", "<head:MHF_Steve>"); //
        }

        return "<head:MHF_Steve>"; //
    }
}
