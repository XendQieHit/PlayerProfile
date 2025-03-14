package org.mcxqh.playerProfile.players.profile.status.subStatus;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.status.SubStatus;

import static org.mcxqh.playerProfile.events.StatusListener.AFKPlayerListProfile;
import static org.mcxqh.playerProfile.events.StatusListener.activePlayerListProfile;

public class Idle extends SubStatus {
    private boolean isIdle;
    private int Idle_TIME;

    /**
     * This constructor is mostly used for loading player's status setting.
     */
    public Idle(Player player) {
        this.player = player;
        this.isIdle = false;
        this.Idle_TIME = 0;
        this.customName = null;
        this.defaultColor = ChatColor.WHITE;
        this.color = defaultColor;
        this.isDisplayCustomName = false;
        this.name = defaultColor + "Idle[闲置]" + ChatColor.RESET;
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
        Profile profile = Profile.profileMapWithUUID.get(player.getUniqueId());
        AFK afk = profile.getStatus().getAFK();
        Idle idle = profile.getStatus().getIdle();
        // change active status
        afk.setAFK(false);
        idle.setIdle(true);
        AFKPlayerListProfile.remove(profile);
        activePlayerListProfile.add(profile);

        if (this.isDisplay) {
            // recover player's name
            String playerName;
            if (isDisplayCustomName && !customName.isEmpty()) {
                playerName = player.getName() + " " + idle.getCustomName();
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
}
