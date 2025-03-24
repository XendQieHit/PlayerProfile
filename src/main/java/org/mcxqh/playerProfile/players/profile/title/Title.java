package org.mcxqh.playerProfile.players.profile.title;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.mcxqh.playerProfile.players.profile.identity.Identity;

public class Title {
    private String name;
    private ChatColor color;
    private String description;
    private final Identity issuerIdentity;

    public Title(String name, ChatColor color, String description, Identity issuerIdentity) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.issuerIdentity = issuerIdentity;
    }

    public BaseComponent getTitle() {
        if (this.name != null) {
            TextComponent textComponent = new TextComponent("[" + this.name + "]");
            if (this.color != null) {
                textComponent.setColor(this.color.asBungee());
            }
            return textComponent;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }
}
