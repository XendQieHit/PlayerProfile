package org.mcxqh.playerProfile.players.profile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.status.Status;
import org.mcxqh.playerProfile.players.profile.status.instances.AFK;
import org.mcxqh.playerProfile.players.profile.status.instances.Idle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StatusManager {
    private AFK afk;
    private Idle idle;
    private final Player player;
    private final ArrayList<Status> statusArrayList = new ArrayList<>();

    private final ArrayList<Class<? extends Status>> statusClassArrayList = new ArrayList<>();

    public StatusManager(Player player, Profile profile) {
        this.player = player;
        this.afk = new AFK(player, profile);
        this.idle = new Idle(player, profile);

        this.statusClassArrayList.add(AFK.class);
        this.statusClassArrayList.add(Idle.class);

        this.statusArrayList.add(afk);
        this.statusArrayList.add(idle);
    }

    public AFK getAFK() {
        return afk;
    }

    public Idle getIdle() {
        return idle;
    }

    public ArrayList<Status> getStatuses() {
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
        List<String> list = new ArrayList<>(this.getStatuses().size());
        for (Status status : this.getStatuses()) {
            list.add(status.getClass().getSimpleName().toLowerCase());
        }
        return list;
    }

    /**
     * Save PLayer's status setting as json in <code>plugins/PlayerProfile/Status</code> folder.
     * It needs to be provided a <code>JsonObject</code> if you want to write manually.
     * Or <code>saveStatus()</code> is more recommended.
     */
    public void save(JsonArray statusJSON) {
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
    public void save() {
        JsonArray json = this.toJson();
        Logger.getLogger("PlayerProfile").info(json.toString());
        showDetail();
        save(json);
    }

    /**
     * Batch all status by <code>loadSetting</code> method.
     */
    public void load() {
        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = fileHandler.getStatus(player);

        // Got JsonObject, now instance statuses.
        Gson gson = new Gson();
        statusArrayList.forEach(status -> {
            JsonObject json = (JsonObject) jsonArray.get(status.id);
            Logger.getLogger("PlayerProfile").info(json.toString());
            status.load(json);
            Logger.getLogger("PlayerProfile").info(status.toString());
        });

        showDetail();
    }

    private void showDetail() {
        ComponentBuilder componentBuilder = new ComponentBuilder("Status List:");
        statusArrayList.forEach(status -> {
            componentBuilder.append("\n" + status.getName() + "    " + status.getColor().name() + "    " + status.getCustomName());
        });
        this.player.spigot().sendMessage(componentBuilder.create());
    }

    /**
     * Transform all statuses attributes to <code>JsonArray</code>.
     * @return <code>JsonArray</code>
     */
    public JsonArray toJson() {
        JsonArray array = new JsonArray();
        for (Status status : statusArrayList) {
            array.add(status.toJson());
        }
        return array;
    }
}
