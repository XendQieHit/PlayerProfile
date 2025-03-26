package org.mcxqh.playerProfile;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class Utils {
    public static ChatColor valueOfChatColor(String s) {
        String[] codeCast = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§k", "§l", "§m", "§n", "§o", "§r"};
        for (int i = 0; i < codeCast.length; i++) {
            if (s.equals(codeCast[i])) {
                return ChatColor.values()[i];
            }
        }
        try {
            return ChatColor.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.getLogger("PlayerProfile").severe("加载颜色代码失败：" + e);
            return null;
        }
    }
}
