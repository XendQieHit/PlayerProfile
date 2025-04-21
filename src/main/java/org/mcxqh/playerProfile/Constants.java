package org.mcxqh.playerProfile;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final List<String> CHAT_COLOR_STRING_LIST;
    public static final String[] CHAT_COLOR_STRING_ARRAY;
    
    static {
        CHAT_COLOR_STRING_LIST = Arrays.stream(org.bukkit.ChatColor.values())
                .map(Enum::name)
                .limit(16)
                .toList();
        CHAT_COLOR_STRING_ARRAY = CHAT_COLOR_STRING_LIST.toArray(String[]::new);
    }
}
