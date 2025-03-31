package org.mcxqh.playerProfile.players.profile.identity;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

public enum AuthLevel {
    SERVER("Server", "GRAY"),
    OFFICIAL("Official", "RED"),
    GUILD("Guild", "GREEN"),
    GROUP("Group", "ORANGE"),
    TEAM("Team", "AQUA"),
    PERSONAL("Personal", "LIGHT_GRAY");

    private final String name;
    private final String color;

    AuthLevel(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public ChatColor getColor() {
        return ChatColor.valueOf(this.color);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", this.name);
        jsonObject.addProperty("Color", this.color);
        return jsonObject;
    }
}
