package org.mcxqh.playerProfile.players.profile.status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.Utils;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class Status {
    protected boolean isDisplay;
    protected boolean isDisplayCustomName;
    protected String customName;
    protected ChatColor color;

    protected final byte id;
    protected final transient ChatColor defaultColor;
    public final transient Profile profile;
    public final transient Player player;

    public final transient FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));

    public Status(byte id, ChatColor defaultColor, Player player) {
        this.id = id;
        this.defaultColor = defaultColor;
        this.player = player;
        this.profile = Data.profileMapWithUUID.get(player.getUniqueId());
    }

    public String getRawCustomStatus() {
        return customName;
    }

    public String getCustomName() {
        if (customName != null && !customName.isEmpty()) {
            return color + "[" + getRawCustomStatus() + "]" + ChatColor.RESET;
        }
        return "";
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    public boolean isDisplayCustomName() {
        return isDisplayCustomName;
    }

    public void setDisplayCustomName(boolean displayCustomName) {
        this.isDisplayCustomName = displayCustomName;
    }

    public void resetCustomName() {
        this.color = this.defaultColor;
        this.customName = "";
    }

    /**
     * Load status within json.
     */
    public <T extends Status> void load() {
        String statusName = this.getClass().getSimpleName();
        // Firstly, read json file.
        FileHandler fileHandler = new FileHandler();
        JsonArray jsonArray = fileHandler.getStatus(player);
        Gson gson = new Gson();

        JsonObject json = gson.fromJson(jsonArray.get(id), JsonObject.class);

        this.isDisplay = json.get("isDisplay").getAsBoolean();
        this.isDisplayCustomName = json.get("isDisplayCustomName").getAsBoolean();
        this.color = Utils.valueOfChatColor(json.get("color").getAsString());
        this.customName = json.get("customName").getAsString();


        // Reading finished. Now setting player's status.
        Logger.getLogger("PlayerProfile").info("Status: " + json.toString() + " Loading Setting...");
    }

    /**
     * Get JsonObject of sub-status.
     */
    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }

    /**
     * Return combined custom status.
     * @return String Composed of decorated symbol, ChatColor and raw custom status name
     * */
    public String getName() {
        return defaultColor + this.getClass().getSimpleName() + ChatColor.RESET;
    }

    /**
     * Show the sub-status now.
     * For using this method, <code>isDisplay</code> must be true.
     * @return true if the method work, or false.
     */
    abstract public boolean now();
}
