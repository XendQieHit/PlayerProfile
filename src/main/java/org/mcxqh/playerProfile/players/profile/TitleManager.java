package org.mcxqh.playerProfile.players.profile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class TitleManager {
    private final Player player;
    private Title presentTitle;
    private final ArrayList<Title> titleArrayList = new ArrayList<>();
    private final ArrayList<String> titleStringArrayList = new ArrayList<>();

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
        titleStringArrayList.add(title.toString());
    }

    public void removeTitle(Title title) {
        titleArrayList.remove(title);
        titleStringArrayList.remove(title.toString());
    }

    public ArrayList<Title> getTitleArrayList() {
        return titleArrayList;
    }

    public ArrayList<String> getTitleStringArrayList() {
        return titleStringArrayList;
    }

    /**
     * Load player's title within json file of title.
     */
    public void load() {
        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = null;

        // Firstly, read json file.
        jsonArray = fileHandler.getTitle(player);

        if (jsonArray == null) {
            Logger.getLogger("PlayerProfile").severe("无法获取文件");
            this.player.spigot().sendMessage(new ComponentBuilder("无法获取文件").color(net.md_5.bungee.api.ChatColor.RED).create());
            throw new RuntimeException();
        }

        // Reading finished. Now setting player's status.
        Logger.getLogger("PlayerProfile").info("Title: " + jsonArray + " Loading Setting...");
        Gson gson = new Gson();
        jsonArray.forEach(jsonElement -> {
            try {
                titleArrayList.add(gson.fromJson(jsonElement, Title.class));
            } catch (NullPointerException e) {
                player.spigot().sendMessage(new ComponentBuilder("加载称号失败：" + e).color(net.md_5.bungee.api.ChatColor.RED).create());
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Save player's title in json file
     */
    public void save() {
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
