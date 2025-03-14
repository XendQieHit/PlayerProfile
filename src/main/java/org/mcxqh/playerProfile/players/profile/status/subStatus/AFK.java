package org.mcxqh.playerProfile.players.profile.status.subStatus;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.players.profile.status.SubStatus;

import static org.mcxqh.playerProfile.events.StatusListener.AFKPlayerListProfile;
import static org.mcxqh.playerProfile.events.StatusListener.activePlayerListProfile;

public class AFK extends SubStatus {
    private boolean isAFK;
    private int AFK_TIME;

    /**
     * This constructor is mostly used for loading player's status setting.
     */
    public AFK(Player player) {
        this.player = player;
        this.isAFK = true;
        this.AFK_TIME = 0;
        this.customName = null;
        this.defaultColor = ChatColor.DARK_GRAY;
        this.color = defaultColor;
        this.isDisplayCustomName = false;
        this.name = defaultColor + "AFK[挂机]" + ChatColor.RESET;
    }

    public int getAFK_TIME() {
        return AFK_TIME;
    }

    public void setAFK_TIME(int AFK_TIME) {
        this.AFK_TIME = AFK_TIME;
    }

    public void addAFK_TIME(int num) {
        AFK_TIME += num;
    }

    public boolean isAFK() {
        return isAFK;
    }

    public void setAFK(boolean AFK) {
        isAFK = AFK;
    }

    @Override
    public String getCustomName() {
        if (customName != null && !customName.isEmpty()) {
            return color + "[" + getRawCustomStatus() + "]" + ChatColor.RESET;
        }
        return ChatColor.DARK_GRAY + "[AFK]" + ChatColor.RESET;
    }

    public boolean now() {
        Idle idle = profile.getStatus().getIdle();
        // set AFK status
        this.setAFK(true);
        idle.setIdle(false);
        idle.setIdle_TIME(0);
        activePlayerListProfile.remove(profile);
        AFKPlayerListProfile.add(profile);

        if (this.isDisplay) {
            String AFKDisplayedName = net.md_5.bungee.api.ChatColor.DARK_GRAY + profile.getName() + " " + this.getCustomName();

            player.setDisplayName(AFKDisplayedName);
            player.setPlayerListName(AFKDisplayedName);
            player.setCustomName(AFKDisplayedName);

            // build text component to broadcast
            ComponentBuilder componentBuilder = new ComponentBuilder();

            // if title-display-on-broadcast is enabled, display player's name with title.
            if (config.getBoolean("title-display-on-broadcast")) {
                componentBuilder.append(profile.getMixedName());
                componentBuilder.color(net.md_5.bungee.api.ChatColor.DARK_GRAY);
            } else {
                componentBuilder.append(profile.getName());
                componentBuilder.color(net.md_5.bungee.api.ChatColor.DARK_GRAY);
            }
            componentBuilder.append( " 睡着了...");
            componentBuilder.color(net.md_5.bungee.api.ChatColor.DARK_GRAY);
            Bukkit.getServer().spigot().broadcast(componentBuilder.create());
            return true;
        }
        return false;
    }
}
