package org.mcxqh.playerProfile.players;

import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.profile.IdentityManager;
import org.mcxqh.playerProfile.players.profile.title.Title;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.TitleManager;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;


public class Profile {
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));
    private final Player player;
    private final UUID uniqueId;
    private final String name;
    private final TitleManager titleManager;
    private final StatusManager statusManager;
    private final IdentityManager identityManager;


    public Profile(Player player) {
        this.player = player;
        uniqueId = player.getUniqueId();
        name = player.getName();

        titleManager = new TitleManager(player);
        statusManager = new StatusManager(player, this);
        identityManager = new IdentityManager(player);

        // Register on Plugin
        Data.PROFILE_MAP_WITH_UUID.put(uniqueId, this);
        Data.PROFILE_MAP_WITH_NAME.put(name, this);

        Data.PLAYER_MAP_WITH_NAME.put(name, player);
        Data.PLAYER_MAP_WITH_UUID.put(player.getUniqueId(), player);

        Data.PLAYER_NAME_SET.add(name);
    }

    public void load() {
        this.statusManager.load();
        this.titleManager.load();
    }

    public void save() {
        this.statusManager.save();
        this.titleManager.save();
    }

    public StatusManager getStatusManager() {
        return statusManager;
    }

    public TitleManager getTitleManager() {
        return titleManager;
    }

    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getRawName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Get BaseComponent, which compose of title, raw player name.
     * This value is usually used to send message。
     * @return BaseComponent[]
     */
    public BaseComponent[] getNameAsBaseComponent() {
        // 0 -> [Title]<PlayerName>ChatMessage
        // 1 -> [Title] PlayerName: ChatMessage
        ComponentBuilder componentBuilder = new ComponentBuilder();
        /*TextComponent playerName = new TextComponent(getName());
        playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new Entity()));*/

        // Get Title
        Title presentTitle = titleManager.getPresentTitle();
        if (presentTitle != null && presentTitle.getName() != null) {
            componentBuilder.append(presentTitle.toBaseComponent());
        }

        switch (config.getInt("title.title-style")) {
            case 0 -> {
                componentBuilder.append("<" + getRawName() + ">");
            }
            case 1 -> {
                componentBuilder.append(" " + getRawName() + ": ");
            }
            default -> {
                Logger.getLogger("PlayerProfile").warning("Title style argument isn't in range! Please check PlayerProfile/config.yml !");
                componentBuilder.append("<" + getRawName() + ">");
            }
        }
        return componentBuilder.create();
    }

    /**
     * Get String, which compose of title and raw player name.
     * This value is usually used to modify message sent by player.
     * @return String
     */
    public String getNameAsString() {
        Title presentTitle = this.titleManager.getPresentTitle();

        switch (config.getInt("title.title-style")) {
            case 0 -> {
                if (presentTitle != null) {
                    return presentTitle.getColor() + "[" + presentTitle.getName() + "]" + ChatColor.RESET + "<" + this.getRawName() + "> ";
                } else {
                    return "<" + this.getRawName() + "> ";
                }
            }
            case 1 -> {
                if (presentTitle != null) {
                    return presentTitle.getColor() + "[" + presentTitle.getName() + "] " + ChatColor.RESET + this.getRawName() + ":  ";
                } else {
                    return this.getRawName() + ":  ";
                }
            }
            default -> {
                Logger.getLogger("PlayerProfile").warning("Title style argument isn't in range! Please check PlayerProfile/config.yml !");
                if (presentTitle != null) {
                    return presentTitle.getColor() + "[" + presentTitle.getName() + "]" + ChatColor.RESET + "<" + this.getRawName() + "> ";
                } else {
                    return "<" + this.getRawName() + "> ";
                }
            }
        }
    }
}
