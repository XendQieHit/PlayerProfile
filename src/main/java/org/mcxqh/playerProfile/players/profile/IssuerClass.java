package org.mcxqh.playerProfile.players.profile;

import org.bukkit.ChatColor;

public enum IssuerClass {
    SERVER(0, "Server", "GRAY"),
    OFFICIAL(1, "Official", "RED"),
    GUILD(2, "Guild", "GREEN"),
    GROUP(3, "Group", "ORANGE"),
    TEAM(4, "Team", "AQUA"),
    PERSONAL(5, "Personal", "LIGHT_GRAY");

    private final int ordinal;
    private final String name;
    private final String color;

    private IssuerClass(int ordinal, String name, String color) {
        this.name = name;
        this.ordinal = ordinal;
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public ChatColor getColor() {
        return ChatColor.valueOf(this.color);
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getName() {
        return name;
    }
}
