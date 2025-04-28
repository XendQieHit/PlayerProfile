package org.mcxqh.playerProfile.players.profile.status.instances;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.List;

import static org.mcxqh.playerProfile.events.StatusListener.AFKPlayerListProfile;
import static org.mcxqh.playerProfile.events.StatusListener.activePlayerListProfile;

public class Idle extends Status {
    private transient boolean isIdle;
    private transient int Idle_TIME;

    public static final Material MATERIAL = Material.POTION;
    public static final List<String> DESCRIPTION = List.of(ChatColor.YELLOW+"闲置");

    /**
     * This constructor is mostly used for loading player's status setting.
     */
    public Idle(Player player, Profile profile) {
        super((byte) 1, ChatColor.WHITE, player, profile);
        this.isIdle = true;
        this.Idle_TIME = 0;
        this.customName = "";
        this.color = defaultColor;
        this.isDisplayCustomName = false;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }

    public long getIdle_TIME() {
        return Idle_TIME;
    }

    public void setIdle_TIME(int idle_TIME) {
        Idle_TIME = idle_TIME;
    }

    public void addIdle_TIME(int num) {
        Idle_TIME += num;
    }

    /**
     * This method only modify playerArrayList and Status, not send message.
     */
    @Override
    public boolean now() {
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        AFK afk = profile.getStatusManager().getAFK();
        Idle idle = profile.getStatusManager().getIdle();
        // change active status
        afk.setAFK(false);
        idle.setIdle(true);
        AFKPlayerListProfile.remove(profile);
        activePlayerListProfile.add(profile);

        if (this.isDisplay) {
            // recover player's name
            String playerName;
            if (isDisplayCustomName && !toString().isEmpty()) {
                playerName = player.getName() + " " + idle.toString();
            } else {
                playerName = player.getName();
            }
            player.setDisplayName(playerName);
            player.setPlayerListName(playerName);
            player.setCustomName(playerName);


        } else {
            player.setDisplayName(player.getName());
            player.setCustomName(player.getName());
            player.setPlayerListName(player.getName());
        }
        return true;
    }

    @Override
    public Material getGUIMaterial() {
        return MATERIAL;
    }

    @Override
    public List<String> getDescription() {
        return List.of();
    }
}
