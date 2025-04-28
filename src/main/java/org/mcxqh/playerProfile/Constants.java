package org.mcxqh.playerProfile;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final List<String> CHAT_COLOR_STRING_LIST;
    public static final String[] CHAT_COLOR_STRING_ARRAY;
    public static final Material[] CHAT_COLOR_MATERIALS_ARRAY = {
            Material.BLACK_DYE,
            Material.LAPIS_LAZULI,
            Material.GREEN_DYE,
            Material.CYAN_DYE,
            Material.REDSTONE,
            Material.PURPLE_DYE,
            Material.ORANGE_DYE,
            Material.LIGHT_GRAY_DYE,
            Material.GRAY_DYE,
            Material.BLUE_DYE,
            Material.LIME_DYE,
            Material.LIGHT_BLUE_DYE,
            Material.RED_DYE,
            Material.MAGENTA_DYE,
            Material.YELLOW_DYE,
            Material.WHITE_DYE
    };

    static {
        CHAT_COLOR_STRING_LIST = Arrays.stream(org.bukkit.ChatColor.values())
                .map(Enum::name)
                .limit(16)
                .toList();
        CHAT_COLOR_STRING_ARRAY = CHAT_COLOR_STRING_LIST.toArray(String[]::new);
    }
}
