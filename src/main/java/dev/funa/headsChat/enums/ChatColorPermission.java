package dev.funa.headsChat.enums;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public enum ChatColorPermission {
    // Colors
    BLACK('0', "headschat.color.black"),
    DARK_BLUE('1', "headschat.color.dark_blue"),
    DARK_GREEN('2', "headschat.color.dark_green"),
    DARK_AQUA('3', "headschat.color.dark_aqua"),
    DARK_RED('4', "headschat.color.dark_red"),
    DARK_PURPLE('5', "headschat.color.dark_purple"),
    GOLD('6', "headschat.color.gold"),
    GRAY('7', "headschat.color.gray"),
    DARK_GRAY('8', "headschat.color.dark_gray"),
    BLUE('9', "headschat.color.blue"),
    GREEN('a', "headschat.color.green"),
    AQUA('b', "headschat.color.aqua"),
    RED('c', "headschat.color.red"),
    LIGHT_PURPLE('d', "headschat.color.light_purple"),
    YELLOW('e', "headschat.color.yellow"),
    WHITE('f', "headschat.color.white"),

    // Formats
    OBFUSCATED('k', "headschat.format.obfuscated"),
    BOLD('l', "headschat.format.bold"),
    STRIKETHROUGH('m', "headschat.format.strikethrough"),
    UNDERLINE('n', "headschat.format.underline"),
    ITALIC('o', "headschat.format.italic"),
    RESET('r', "headschat.format.reset");

    private final char code;
    private final String permission;
    private static final Map<Character, ChatColorPermission> BY_CODE = new HashMap<>();

    static {
        for (ChatColorPermission value : values()) {
            BY_CODE.put(value.code, value);
        }
    }

    ChatColorPermission(char code, String permission) {
        this.code = code;
        this.permission = permission;
    }

    public char getCode() {
        return code;
    }

    public String getPermission() {
        return permission;
    }

    public static ChatColorPermission fromCode(char code) {
        return BY_CODE.get(Character.toLowerCase(code));
    }

    public static boolean canUse(Player player, char code) {
        ChatColorPermission entry = fromCode(code);
        if (entry == null) return false;

        // Allow wildcard permission for all colors or all formats
        if (code >= '0' && code <= 'f' && player.hasPermission("headschat.color.*")) return true;
        if (code >= 'k' && code <= 'r' && player.hasPermission("headschat.format.*")) return true;

        // Allow everything if player has global permission
        if (player.hasPermission("headschat.*")) return true;

        return player.hasPermission(entry.getPermission());
    }
}
