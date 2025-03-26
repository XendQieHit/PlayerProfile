package org.mcxqh.playerProfile.players.profile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.profile.status.Status;
import org.mcxqh.playerProfile.players.profile.status.instances.AFK;
import org.mcxqh.playerProfile.players.profile.status.instances.Idle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StatusManager {
    private AFK afk;
    private Idle idle;
    private final Player player;
    private final ArrayList<Status> statusArrayList = new ArrayList<>();

    private final ArrayList<Class<? extends Status>> statusClassArrayList = new ArrayList<>();

    public StatusManager(Player player) {
        this.player = player;

        this.statusClassArrayList.add(AFK.class);
        this.statusClassArrayList.add(Idle.class);

        this.statusArrayList.add(afk);
        this.statusArrayList.add(idle);

        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = fileHandler.getStatus(player);

        // Got JsonObject, now instance statuses.
        Gson gson = new Gson();
        for (int i = 0; i < statusArrayList.size(); i++) {
            statusArrayList.set(i, gson.fromJson(jsonArray.get(i), statusClassArrayList.get(i)));
        }
    }

    public AFK getAFK() {
        return afk;
    }

    public Idle getIdle() {
        return idle;
    }

    public ArrayList<Status> getAllSubStatuses() {
        return statusArrayList;
    }

    /**
     * This method is used in FileHandler that batch to generate new status default setting.
     */
    public ArrayList<Class<? extends Status>> getStatusClassArrayList() {
        return statusClassArrayList;
    }

    /**
     * This method will lowerCase all subStatusName automatically.
     */
    public List<String> getAllSubStatusNames() {
        List<String> list = new ArrayList<>(this.getAllSubStatuses().size());
        for (Status status : this.getAllSubStatuses()) {
            list.add(status.getClass().getSimpleName().toLowerCase());
        }
        return list;
    }

    /**
     * Save PLayer's status setting as json in <code>plugins/PlayerProfile/Status</code> folder.
     * It needs to be provided a <code>JsonObject</code> if you want to write manually.
     * Or <code>saveStatus()</code> is more recommended.
     */
    public void saveStatus(JsonObject statusJSON) {
        FileHandler fileHandler = new FileHandler();
        try {
            fileHandler.saveStatus(this.player, statusJSON);
        } catch (IOException e) {
            this.player.spigot().sendMessage(new ComponentBuilder("读写失败: " + e).color(ChatColor.RED).create());
            throw new RuntimeException(e);
        }
    }

    /**
     * Save PLayer's status setting as json in <code>plugins/PlayerProfile/Status</code> folder.
     */
    public void saveStatus() {
        saveStatus(this.toJson());
    }

    /**
     * Batch all status by <code>loadSetting</code> method.
     */
    public void load() {
        for (Status status : statusArrayList) {
            status.load();
        }
    }

    /**
     * Transform status attributes to <code>JsonObject</code>.
     * @return <code>JsonObject</code>
     */
    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }
}
