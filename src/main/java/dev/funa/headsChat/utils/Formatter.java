package dev.funa.headsChat.utils;

public class Formatter {
  public Component parseText(String text, Player player, HeadsChat plugin) {
    return plugin.getMiniMessage().deserialize(
      text
        .replace("{player}", player.getName())
        .replace("{head}", "<head:" + player.getName() + ">")
    )
  }
}
