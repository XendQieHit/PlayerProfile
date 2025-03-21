package org.mcxqh.playerProfile.players.profile.status;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class Status {
    protected boolean isDisplay;
    protected boolean isDisplayCustomName;
    protected String customName;
    protected String name;
    protected ChatColor color;
    protected ChatColor defaultColor;
    public Profile profile;
    public Player player;
    public final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));

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
    public void loadSetting() {
        String statusName = this.getClass().getSimpleName();
        JsonObject json = null;
        // Firstly, read json file.
        json = getStatusWithTry(json);

        if (json == null) {
            Logger.getLogger("PlayerProfile").severe("无法获取文件");
            this.player.spigot().sendMessage(new ComponentBuilder("无法获取文件").color(net.md_5.bungee.api.ChatColor.RED).create());
            throw new RuntimeException();
        }

        // Reading finished. Now setting player's status.
        Logger.getLogger("PLayerProfile").info("Status: " + json.toString() + " Loading Setting...");

        this.profile = Data.profileMapWithUUID.get(this.player.getUniqueId());

        this.isDisplay = json.get("isDisplay").getAsBoolean();
        this.isDisplayCustomName = json.get("isDisplayCustomName").getAsBoolean();
        this.customName = json.has("customName") && !json.get("customName").isJsonNull() ? json.get("customName").getAsString() : null;
        try {
            this.color = ChatColor.valueOf(json.get("chatColor").getAsString());
        } catch (IllegalArgumentException e) {
            this.player.spigot().sendMessage(new ComponentBuilder("加载状态颜色设置失败: "+ this.getClass().getSimpleName() + ": " + e + "现已还原成初始设置").color(net.md_5.bungee.api.ChatColor.YELLOW).create());
        }
    }
    /**
     * This method is for <code>loadSetting()</code>.
     */
    private JsonObject getStatusWithTry(JsonObject json) {
        FileHandler fileHandler = new FileHandler();
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                json = fileHandler.getStatus(this.player).getAsJsonObject(this.getClass().getSimpleName());
                break;
            } catch (FileNotFoundException e) {
                try {
                    fileHandler.createStatus(this.player);
                } catch (IOException ex) {
                    sendErrorMsg(e, ex);
                    throw new RuntimeException(ex);
                }
            } catch (NullPointerException e) {
                try {
                    fileHandler.resetStatus(this.player);
                } catch (IOException ex) {
                    sendErrorMsg(e, ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        return json;
    }

    private void sendErrorMsg(Exception e, IOException ex) {
        Logger.getLogger("PlayerProfile").severe("读取配置文件失败: " + e);
        this.player.spigot().sendMessage(new ComponentBuilder("读取配置文件失败: " + e).color(net.md_5.bungee.api.ChatColor.RED).create());
    }

    /**
     * Get JsonObject of sub-status.
     */
    public JsonObject toJson() {
        // {
        //   this.getClass().getSimpleName(): {
        //     "isDisplay": isDisplay(),
        //     "isDisplayCustomName": isDisplayCustomName(),
        //     "customName": getRawCustomStatus(),
        //     "chatColor": getChatColor().toString()
        //   }
        // }
        JsonObject json = new JsonObject();
        json.addProperty("isDisplay", this.isDisplay());
        json.addProperty("isDisplayCustomName", this.isDisplayCustomName());
        json.addProperty("customName", this.getRawCustomStatus());
        json.addProperty("chatColor", Optional.ofNullable(this.getColor()).isPresent() ? this.getColor().asBungee().getName().toUpperCase() : "");
        return json;
    }

    /**
     * Return combined custom status.
     * @return String Composed of decorated symbol, ChatColor and raw custom status name
     * */
    public String getName() {
        return this.name;
    }

    /**
     * Show the sub-status now.
     * For using this method, <code>isDisplay</code> must be true.
     * @return true if the method work, or false.
     */
    abstract public boolean now();
}
