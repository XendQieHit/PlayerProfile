package org.mcxqh.playerProfile.players.profile.status.instances;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.Optional;
import java.util.logging.Logger;

import static org.mcxqh.playerProfile.events.StatusListener.AFKPlayerListProfile;
import static org.mcxqh.playerProfile.events.StatusListener.activePlayerListProfile;

public class AFK extends Status {
    private transient boolean isAFK;
    private transient int AFK_TIME;

    /**
     * This constructor is mostly used for generate new default status setting.
     */
    public AFK(Player player, Profile profile) {
        super((byte) 0, ChatColor.DARK_GRAY, player, profile);
        this.isAFK = false;
        this.AFK_TIME = 0;
        this.customName = "";
        this.color = defaultColor;
        this.isDisplayCustomName = false;
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
    public String toString() {
        if (customName != null && !customName.isEmpty()) {
            return color + "[" + customName + "]" + ChatColor.RESET;
        }
        return ChatColor.DARK_GRAY + "[AFK]" + ChatColor.RESET;
    }

    @Override
    public boolean now() {
        Logger.getLogger("PlayerProfile").info(profile.toString());
        Optional<StatusManager> optional = Optional.ofNullable(profile.getStatusManager());
        optional.ifPresent(statusManager -> Logger.getLogger("PlayerProfile").info("I'm here."));
        Idle idle = profile.getStatusManager().getIdle();
        // set AFK status
        this.setAFK(true);
        idle.setIdle(false);
        idle.setIdle_TIME(0);
        activePlayerListProfile.remove(profile);
        AFKPlayerListProfile.add(profile);

        if (this.isDisplay) {
            String AFKDisplayedName = net.md_5.bungee.api.ChatColor.DARK_GRAY + profile.getRawName() + " " + this.toString();

            player.setDisplayName(AFKDisplayedName);
            player.setPlayerListName(AFKDisplayedName);
            player.setCustomName(AFKDisplayedName);

            // build text component to broadcast
            ComponentBuilder componentBuilder = new ComponentBuilder();

            // if title-display-on-broadcast is enabled, display player's name with title.
            if (config.getBoolean("title-display-on-broadcast")) {
                componentBuilder.append(profile.getNameAsBaseComponent());
                componentBuilder.color(net.md_5.bungee.api.ChatColor.DARK_GRAY);
            } else {
                componentBuilder.append(profile.getRawName());
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
