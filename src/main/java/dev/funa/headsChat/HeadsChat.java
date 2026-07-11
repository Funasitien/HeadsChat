package dev.funa.headsChat;

import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.ErrorTracker;
import dev.faststats.core.data.Metric;

import dev.funa.headsChat.commands.MainCommand;
import dev.funa.headsChat.commands.MessageCommand;
import dev.funa.headsChat.commands.RenameCommand;
import dev.funa.headsChat.integration.SkinRestorerHook;
import dev.funa.headsChat.integration.VaultHook;
import dev.funa.headsChat.listeners.ChatListener;
import dev.funa.headsChat.listeners.JoinLeaveListener;
import dev.funa.headsChat.managers.ConfigManager;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
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
    public static boolean update = false;
    public MainCommand mainCommandExecutor;
    public RenameCommand renameCommandExecutor;
    private final AtomicInteger serverMode = new AtomicInteger();

    private final BukkitMetrics metrics = BukkitMetrics.factory()
            .token("3f2a9c0fb3d4136a6c9fb3a112e969a8")
            .addMetric(Metric.number("server_mode", serverMode::get))
            .errorTracker(ERROR_TRACKER)

            .create(this);


    @Override
    public void onEnable() {
        if (!isSupportedMinecraftVersion()) {
            getLogger().severe("HeadsChat requires Minecraft 1.21.9 or newer.");
            getLogger().severe("Current server version: " + Bukkit.getMinecraftVersion());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Loading metrics...");
        if (getServer().getOnlineMode()) {
            serverMode.set(1);
        } else if(getServer().getServerConfig().isProxyEnabled()) {
            if(getServer().getServerConfig().isProxyOnlineMode()) {
                serverMode.set(2);
            } else {
                serverMode.set(3);
            }
        } else {
            serverMode.set(0);
        }

        metrics.ready();

        checkForUpdatesModrinthAsync();

        getLogger().info("Loading config...");
        configManager = new ConfigManager(this);
        configManager.setup();
        if (configManager.getConfig().getBoolean("chat.enabled")) {
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
        if (configManager.getConfig().getBoolean("join.enabled")) {
            getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);
        }
        boolean skinRestorerEnabled =
                Bukkit.getPluginManager().getPlugin("SkinRestorer") != null;

        if (skinRestorerEnabled) {
            getLogger().info("SkinRestorer detected, enabling integration.");
            SkinRestorerHook.init(this);
        }

        boolean vaultEnabled = false;

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vaultEnabled = VaultHook.setup();

            if (vaultEnabled) {
                getLogger().info("Vault chat integration enabled.");
            }
        }

        getLogger().info("Listeners loaded.");

        this.mainCommandExecutor = new MainCommand(this);
        this.getCommand("headschat").setExecutor(this.mainCommandExecutor);
        this.getCommand("headschat").setTabCompleter(this.mainCommandExecutor);

        if (configManager.isRenameEnabled()) {
            this.renameCommandExecutor = new RenameCommand(this);
            this.getCommand("rename").setExecutor(this.renameCommandExecutor);
            this.getCommand("rename").setTabCompleter(this.renameCommandExecutor);
        }

        if (configManager.getConfig().getBoolean("dm.enabled")) {
            this.getCommand("msg").setExecutor(new MessageCommand(this));
        }

        getLogger().info("All commands loaded.");
        getLogger().info("HeadsChat has been enabled!");
    }

    private boolean isSupportedMinecraftVersion() {
        String version = Bukkit.getMinecraftVersion();
        String[] parts = version.split("\\.");

        if (parts.length < 3) {
            return false;
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts[2]);

            if (major != 1) {
                return major > 1;
            }

            if (minor != 21) {
                return minor > 21;
            }

            return patch >= 9;
        } catch (NumberFormatException ignored) {
            return false;
        }
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
                        String currentVersion = getDescription().getVersion();

                        if (!currentVersion.equals(latestVersion)) {
                            getLogger().warning("A new version of HeadsChat is available: " + latestVersion);
                            getLogger().warning("Current version: " + currentVersion);
                            getLogger().warning("Download it from: https://modrinth.com/plugin/headschat");
                            update = true;
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

    public boolean isFloodgateEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Floodgate");
    }
}
