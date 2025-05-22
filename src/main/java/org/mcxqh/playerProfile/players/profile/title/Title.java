package org.mcxqh.playerProfile.players.profile.title;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.mcxqh.playerProfile.players.profile.identity.AuthLevel;
import org.mcxqh.playerProfile.players.profile.identity.Identity;

public class Title {
    private final String name;
    private final ChatColor color;
    private final String description;
    private final Identity issuerIdentity;

    public Title(String name, String description, Identity issuerIdentity) {
        this.name = name;
        this.color = issuerIdentity.getAuthLevel().getColor();
        this.description = description;
        this.issuerIdentity = issuerIdentity;
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

    public Identity getIssuerIdentity() {
        return issuerIdentity;
    }

    public String toSimpleString() {
        AuthLevel authLevel = issuerIdentity.getAuthLevel();
        if (authLevel == AuthLevel.PERSONAL || authLevel == AuthLevel.SERVER) {
            return String.format("%s.%s", authLevel, name);
        }
        return String.format("%s.%s.%s", authLevel, name, issuerIdentity.getCollective().getName());
    }

    @Override
    public String toString() {
        AuthLevel authLevel = issuerIdentity.getAuthLevel();
        if (authLevel == AuthLevel.PERSONAL || authLevel == AuthLevel.SERVER) {
            return String.format("%s[%s]%s", color, authLevel, name);
        }
        return String.format("%s[%s]%s", color, issuerIdentity.getCollective().getName(), name);
    }

    public BaseComponent toBaseComponent() {
        if (this.name != null) {
            TextComponent textComponent = new TextComponent("[" + this.name + "]");
            ChatColor titleColor;

            // Set Color
            if (this.color != null) titleColor = this.color;
            else titleColor = issuerIdentity.getAuthLevel().getColor();

            textComponent.setColor(titleColor.asBungee());

            // Build HoverEvent
            ComponentBuilder componentBuilder = new ComponentBuilder();
            componentBuilder
                    .append("[" + this.issuerIdentity.getAuthLevel().toString() + "]" + this.name)
                    .color(titleColor.asBungee())
                    .append("\n" + this.description)
                    .color(ChatColor.WHITE.asBungee())
                    .append("\n颁发者：")
                    .color(ChatColor.YELLOW.asBungee())
                    .append(this.issuerIdentity.toBaseComponent());

            // End
            Text hoverEventText = new Text(componentBuilder.create());
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverEventText);
            textComponent.setHoverEvent(hoverEvent);
            return textComponent;
        }
        return null;
    }

    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }
}
