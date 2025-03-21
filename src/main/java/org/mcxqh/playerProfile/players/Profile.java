package org.mcxqh.playerProfile.players;

import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.profile.title.IssuerClass;
import org.mcxqh.playerProfile.players.profile.title.Title;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.TitleManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


public class Profile {
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));
    private final UUID uniqueId;
    private final String name;
    private final TitleManager titleManager;
    private final StatusManager statusManager;
    private final List<IssuerClass> issuerClassList = new ArrayList<>();
    private final Player player;

    public Profile(Player player) {
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
        this.titleManager = new TitleManager(player);
        this.statusManager = new StatusManager(player);
        this.player = player;

        // Register on Plugin
        Data.profileMapWithUUID.put(uniqueId, this);
        Data.playerMapWithName.put(this.name, player);

        // Register on Server
        Data.playerMapWithUUID.put(player.getUniqueId(), player);
        Data.playerNameArrayList.add(this.name);
    }

    public StatusManager getStatusManager() {
        return statusManager;
    }

    public TitleManager getTitleManager() {
        return titleManager;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
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
    public BaseComponent[] getMixedName() {
        // 0 -> [Title]<PlayerName>ChatMessage
        // 1 -> [Title] PlayerName: ChatMessage
        ComponentBuilder componentBuilder = new ComponentBuilder();
        /*TextComponent playerName = new TextComponent(getName());
        playerName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new Entity()));*/

        // Get Title
        Title presentTitle = titleManager.getPresentTitle();
        if (presentTitle != null && presentTitle.getName() != null) {
            componentBuilder.append(presentTitle.getTitle());
        }

        switch (config.getInt("title.title-style")) {
            case 0 -> {
                componentBuilder.append("<" + getName() + ">");
            }
            case 1 -> {
                componentBuilder.append(" " + getName() + ": ");
            }
            default -> {
                Logger.getLogger("PlayerProfile").warning("Title style argument isn't in range! Please check PlayerProfile/config.yml !");
                componentBuilder.append("<" + getName() + ">");
            }
        }
        return componentBuilder.create();
    }

    /**
     * Get String, which compose of title and raw player name.
     * This value is usually used to modify message sent by player.
     * @return String
     */
    public String getStringName() {
        Title presentTitle = this.titleManager.getPresentTitle();

        switch (config.getInt("title.title-style")) {
            case 0 -> {
                if (presentTitle != null) {
                    return presentTitle.getColor() + "[" + presentTitle.getName() + "]" + ChatColor.RESET + "<" + this.getName() + "> ";
                } else {
                    return "<" + this.getName() + "> ";
                }
            }
            case 1 -> {
                if (presentTitle != null) {
                    return presentTitle.getColor() + "[" + presentTitle.getName() + "] " + ChatColor.RESET + this.getName() + ":  ";
                } else {
                    return this.getName() + ":  ";
                }
            }
            default -> {
                Logger.getLogger("PlayerProfile").warning("Title style argument isn't in range! Please check PlayerProfile/config.yml !");
                if (presentTitle != null) {
                    return presentTitle.getColor() + "[" + presentTitle.getName() + "]" + ChatColor.RESET + "<" + this.getName() + "> ";
                } else {
                    return "<" + this.getName() + "> ";
                }
            }
        }
    }

    public void loadSetting() {
        this.statusManager.loadSetting();
        this.titleManager.loadTitle();
    }

    public void saveSetting() {
        this.statusManager.saveStatus();
        this.titleManager.saveTitle();
    }

    public List<IssuerClass> getIssuerClassList() {
        return issuerClassList;
    }
}
