package dev.funa.headsChat.commands;

import dev.funa.headsChat.HeadsChat;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RenameCommand implements CommandExecutor, TabCompleter {
    private final @NotNull HeadsChat plugin;
    private final HashMap<UUID, Component> customNames = new HashMap<>();

    public RenameCommand(@NotNull HeadsChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player p)) {
            Component onlyPlayers = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>Only players can execute this command.</#ff5555>");
            sender.sendMessage(onlyPlayers);
            return true;
        }

        if (!(p.hasPermission("headschat.rename") || p.isOp())) {
            Component noPerm = MiniMessage.miniMessage().deserialize(HeadsChat.prefix + "<#ff5555>You do not have permission to execute this command.</#ff5555>");
            p.sendMessage(noPerm);
            return true;
        }

        if (args.length < 1) {
            Dialog dialog = Dialog.create(builder -> {
                String nameInputText = plugin.getConfigManager().getRenameDialogPrompt();

                Component nameInputComponent;
                try {
                    nameInputComponent = MiniMessage.miniMessage().deserialize(nameInputText);
                } catch (Exception e) {
                    nameInputComponent = Component.text("Enter your new display name");
                }

                DialogAction confirmAction = DialogAction.customClick((view, audience) -> {
                    String newname = view.getText("name");
                    if (newname == null) return;

                    if (audience instanceof Player player) {
                        Component cname = MiniMessage.miniMessage().deserialize(newname);
                        player.displayName(cname);
                        player.playerListName(cname);
                        customNames.put(player.getUniqueId(), cname);

                        Component renamed = MiniMessage.miniMessage().deserialize(
                                HeadsChat.prefix + "Your display name has been changed to <#71f678>" + newname + "</#71f678>."
                        );
                        player.sendMessage(renamed);
                    }
                }, ClickCallback.Options.builder().uses(1).build());

                ActionButton confirmButton = ActionButton.create(
                        Component.text(plugin.getConfigManager().getRenameDialogConfirm()),
                        Component.text(plugin.getConfigManager().getRenameDialogConfirmTooltip()),
                        100,
                        confirmAction
                );

                ActionButton discardButton = ActionButton.create(
                        Component.text(plugin.getConfigManager().getRenameDialogCancel()),
                        Component.text(plugin.getConfigManager().getRenameDialogCancelTooltip()),
                        100,
                        null
                );

                builder.empty()
                        .base(DialogBase.builder(Component.text(plugin.getConfigManager().getRenameDialogName()))
                                .inputs(List.of(DialogInput.text("name", nameInputComponent)
                                        .initial(MiniMessage.miniMessage().serialize(Objects.requireNonNull(p.displayName())))
                                        .width(300)
                                        .build()))
                                .build())
                        .type(DialogType.confirmation(confirmButton, discardButton));
            });

            p.showDialog(dialog);
            return true;
        }

        String name = args[0];
        Component cname = MiniMessage.miniMessage().deserialize(name);
        p.displayName(cname);
        p.playerListName(cname);
        customNames.put(p.getUniqueId(), cname);

        Component renamed = MiniMessage.miniMessage().deserialize(
                HeadsChat.prefix + "Your display name has been changed to <#71f678>" + name + "</#71f678>."
        );
        p.sendMessage(renamed);
        return true;
    }

    public @Nullable Component getCustomName(@NotNull Player player) {
        return customNames.get(player.getUniqueId());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
