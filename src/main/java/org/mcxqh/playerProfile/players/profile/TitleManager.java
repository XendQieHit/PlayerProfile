package org.mcxqh.playerProfile.players.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.profile.title.IssuerClass;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TitleManager {
    private final Player player;
    private Title presentTitle;
    private final ArrayList<Title> titleArrayList = new ArrayList<>();

    public TitleManager(Player player) {
        this.player = player;
    }

    public Title getPresentTitle() {
        return presentTitle;
    }

    public void setPresentTitle(Title presentTitle) {
        this.presentTitle = presentTitle;
    }

    public void addTitle(Title title) {
        titleArrayList.add(title);
    }

    /**
     * Load player's title within json file of title.
     */
    public void loadTitle() {
        JsonArray jsonArray = null;

        // Firstly, read json file.
        jsonArray = getTitleWithTry(jsonArray);

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
                throw new RuntimeException(e);
            }
        });
    }
    /**
     * This method is for <code>loadTitle()</code>.
     */
    private JsonArray getTitleWithTry(JsonArray jsonArray) {
        FileHandler fileHandler = new FileHandler();
        for (int attempt = 0; attempt < 2; attempt++) {
            Logger.getLogger("PlayerProfile").info("This is in" + attempt + "try...");
            try {
                jsonArray = fileHandler.getTitle(this.player);
                break;
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
        return jsonArray;
    }
    private void sendErrorMsg(Exception e, IOException ex) {
        Logger.getLogger("PlayerProfile").severe("读取称号配置文件失败: " + e);
        this.player.spigot().sendMessage(new ComponentBuilder("读取称号配置文件失败: " + e).color(net.md_5.bungee.api.ChatColor.RED).create());
    }

    /**
     * Save player's title in json file
     */
    public void saveTitle() {
        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = new JsonArray();
        titleArrayList.forEach(title -> {
            jsonArray.add(title.toJson());
        });
        try {
            fileHandler.saveTitle(this.player, jsonArray.getAsJsonArray());
        } catch (IOException e) {
            this.player.spigot().sendMessage(new ComponentBuilder("保存称号设置失败：" + e).create());
            throw new RuntimeException(e);
        }
        this.player.spigot().sendMessage(new ComponentBuilder("保存称号设置成功").create());
    }

}
