package dev.funa.headsChat;

import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.ErrorTracker;
import dev.faststats.core.data.Metric;

import dev.funa.headsChat.commands.MainCommand;
import dev.funa.headsChat.commands.MessageCommand;
import dev.funa.headsChat.listeners.ChatListener;
import dev.funa.headsChat.listeners.JoinLeaveListener;
import dev.funa.headsChat.managers.ConfigManager;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HeadsChat extends JavaPlugin {

    private static final URI MODRINTH_VERSION_URL =
            URI.create("https://api.modrinth.com/v2/project/headschat/version");

    private static final Pattern MODRINTH_FIRST_VERSION_NUMBER =
            Pattern.compile("\"version_number\"\\s*:\\s*\"([^\"]+)\"");

    private static ConfigManager configManager;
    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();
    private MiniMessage mm = MiniMessage.miniMessage();
    public static String prefix = "<#f6da71>ʜᴇᴀᴅѕᴄʜᴀᴛ</#f6da71> <gray>•</gray> ";
    public static String version = "1.3.4";
    public MainCommand mainCommandExecutor;

    private final BukkitMetrics metrics = BukkitMetrics.factory()
            .token("3f2a9c0fb3d4136a6c9fb3a112e969a8")

            .errorTracker(ERROR_TRACKER)

            .create(this);


    @Override
    public void onEnable() {
        metrics.ready();

        checkForUpdatesModrinthAsync();

        getLogger().info("Loading config...");
        configManager = new ConfigManager(this);
        configManager.setup();
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        if (configManager.getConfig().getBoolean("join.enabled")) {
            getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
        }
        getLogger().info("Listeners loaded.");
        this.mainCommandExecutor = new MainCommand(this);
        this.getCommand("headschat").setExecutor(this.mainCommandExecutor);
        if (configManager.getConfig().getBoolean("dm.enabled")) {
            this.getCommand("dm").setExecutor(new MessageCommand(this));
        }
        getLogger().info("All commands loaded.");
        getLogger().info("HeadsChat has been enabled!");

    }

    public void reloadConfigFile() {
        reloadConfig();
        getLogger().info("Configuration reloaded!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MiniMessage getMiniMessage() {
        return mm;
    }


    @Override
    public void onDisable() {
        metrics.shutdown();
    }

    private void checkForUpdatesModrinthAsync() {
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(MODRINTH_VERSION_URL)
                        .timeout(Duration.ofSeconds(5))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String responseBody = response.body();

                    Matcher matcher = MODRINTH_FIRST_VERSION_NUMBER.matcher(responseBody);
                    if (matcher.find()) {
                        String latestVersion = matcher.group(1);
                        if (!version.equals(latestVersion)) {
                            getLogger().warning("A new version of HeadsChat is available: " + latestVersion);
                            getLogger().warning("Download it from: https://modrinth.com/plugin/headschat");
                        } else {
                            getLogger().info("You are running the latest version of HeadsChat.");
                        }
                    } else {
                        getLogger().warning("Could not parse version number from Modrinth response.");
                    }
                } else {
                    getLogger().warning("Failed to check for updates from Modrinth. Status code: " + response.statusCode());
                }
            } catch (Exception e) {
                ERROR_TRACKER.trackError("Failed to check for updates from Modrinth");
                getLogger().warning("Failed to check for updates from Modrinth: " + e.getMessage());
            }
        });
    }
}
