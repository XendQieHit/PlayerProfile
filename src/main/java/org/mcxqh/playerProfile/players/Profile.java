package org.mcxqh.playerProfile.players;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.profile.IssuerClass;
import org.mcxqh.playerProfile.players.profile.Title;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class Profile {
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));
    private final UUID uniqueId;
    private final String name;
    private Title presentTitle;
    private final ArrayList<Title> titleArrayList = new ArrayList<>();
    private Status status;
    private final Player player;

    public static Map<UUID, Profile> profileMapWithUUID = new ConcurrentHashMap<>();
    public static Map<Profile, Player> playerMapWithProfile = new ConcurrentHashMap<>();
    public static Map<String, Player> playerMapWithName = new ConcurrentHashMap<>();

    public Profile(Player player) {
        this.uniqueId = player.getUniqueId();
        this.name = player.getName();
        this.presentTitle = null;
        this.status = new Status(player);
        this.player = player;

        profileMapWithUUID.put(uniqueId, this);
        playerMapWithProfile.put(this, player);
        playerMapWithName.put(this.name, player);
        PlayerProfile.playerArrayList.add(player);
        PlayerProfile.playerNameArrayList.add(this.name);
    }

    public Title getPresentTitle() {
        return presentTitle;
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
        if (this.getPresentTitle() != null && this.getPresentTitle().getName() != null) {
            componentBuilder.append(getPresentTitle().getTitle());
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

    public void setPresentTitle(Title presentTitle) {
        this.presentTitle = presentTitle;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void loadSetting() {
        this.status.loadSetting();
        this.loadTitle();
    }

    public void loadTitle() {
        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = null;

        // Firstly, read json file.
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                jsonArray = fileHandler.getTitle(this.player);
            } catch (FileNotFoundException e) {
                try {
                    fileHandler.createTitle(this.player);
                } catch (IOException ex) {
                    sendErrorMsg(e, ex);
                    throw new RuntimeException(ex);
                }
            } catch (NullPointerException e) {
                try {
                    fileHandler.resetTitle(this.player);
                } catch (IOException ex) {
                    sendErrorMsg(e, ex);
                    throw new RuntimeException(ex);
                }
            }
        }

        if (jsonArray == null) {
            Logger.getLogger("PlayerProfile").severe("无法获取文件");
            this.player.spigot().sendMessage(new ComponentBuilder("无法获取文件").color(net.md_5.bungee.api.ChatColor.RED).create());
            throw new RuntimeException();
        }

        Logger.getLogger("PlayerProfile").info("Title: " + jsonArray + " Loading Setting...");

        jsonArray.forEach(jsonElement -> {
            try {
                JsonObject json = (JsonObject) jsonElement;

                String name = json.get("name").toString();
                ChatColor color = ChatColor.valueOf(json.get("color").getAsString());
                String description = json.get("description").getAsString();
                JsonObject issuer = (JsonObject) json.get("issuer");
                String issuerName = issuer.get("name").getAsString();
                IssuerClass issuerClass = IssuerClass.valueOf(issuer.get("class").getAsString());

                titleArrayList.add(new Title(name, color, description, issuerName, issuerClass));
            } catch (NullPointerException e) {
                player.spigot().sendMessage(new ComponentBuilder("加载称号失败：" + e).color(net.md_5.bungee.api.ChatColor.RED).create());
            }
        });
    }

    public void saveTitle() {
        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = new JsonArray();
        titleArrayList.forEach(title -> {
            jsonArray.add(title.toJson());
        });
        try {
            fileHandler.saveTitle(this.player, jsonArray.getAsJsonObject());
        } catch (IOException e) {
            this.player.spigot().sendMessage(new ComponentBuilder("保存称号设置失败：" + e).create());
            throw new RuntimeException(e);
        }
        this.player.spigot().sendMessage(new ComponentBuilder("保存称号设置成功").create());
    }

    private void sendErrorMsg(Exception e, IOException ex) {
        Logger.getLogger("PlayerProfile").severe("读取配置文件失败: " + e);
        this.player.spigot().sendMessage(new ComponentBuilder("读取配置文件失败: " + e).color(net.md_5.bungee.api.ChatColor.RED).create());
    }

    /**
     * Gets the player's unique id.
     *
     * @return the player's unique id, or <code>null</code> if not available
     */
    public UUID getUniqueId() {
        return uniqueId;
    }
    /**
     * Gets the player name.
     *
     * @return the player name, or <code>null</code> if not available
     */
    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }
}
