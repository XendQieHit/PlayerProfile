package org.mcxqh.playerProfile.players.profile;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.mcxqh.playerProfile.files.FileHandler;

import java.util.Optional;

public class Title {
    private String name;
    private ChatColor color;
    private String description;

    private final IssuerClass issuerClass;
    private final String issuerName;

    public Title(String name, ChatColor color, String description, String issuerName, IssuerClass issuerClass) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.issuerName = issuerName;
        this.issuerClass = issuerClass;
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
        // {
        //     "name": this.getRawTitle(),
        //     "chatColor": this.getChatColor(),
        //     "description": this.getDescription(),
        //     "owner": {
        //         "name": this.owner.getName(),
        //         "UUID": this.owner.getUniqueId().toString()
        //     },
        //     "issuer": {
        //         "name": this.getIssuerName(),
        //         "class": this.issuerClass.getName()
        //     }
        // }
        JsonObject json = new JsonObject();
        json.addProperty("name", this.getName());
        json.addProperty("chatColor", this.getColor().asBungee().getName().toUpperCase());
        json.addProperty("description", this.getDescription());

        JsonObject issuer = new JsonObject();
        issuer.addProperty("name", this.getIssuerName());
        issuer.addProperty("class", this.issuerClass.getName().toUpperCase());
        json.add("issuer", issuer);

        return json;
    }

    public String getIssuerName() {
        return issuerName;
    }
}
