package org.mcxqh.playerProfile.events;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.instances.AFK;
import org.mcxqh.playerProfile.players.profile.status.instances.Idle;

import java.io.File;
import java.util.*;

public class StatusListener implements Listener {
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));
    private final Map<UUID, Profile> profileMapWithUUID = Data.PROFILE_MAP_WITH_UUID;
    public static final ArrayList<Profile> activePlayerListProfile = new ArrayList<>();
    public static final ArrayList<Profile> AFKPlayerListProfile = new ArrayList<>();
    public static ArrayList<Profile> getActivePlayerList() {
        return activePlayerListProfile;
    }
    public static ArrayList<Profile> getAFKPlayerList() {
        return AFKPlayerListProfile;
    }

    public static void AFKDetector() {
        List<Profile> snapshot;
        // 创建列表的一个快照
        synchronized (activePlayerListProfile) {
            snapshot = new ArrayList<>(activePlayerListProfile);
        }

        for (Profile profile : snapshot) {
            if (profile != null) {
                isAFK(profile);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void disableAFK(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();
        AFK afk = statusManager.getAFK();
        Idle idle = statusManager.getIdle();

        if (afk.isAFK()) {
            // disable AFK status of moving afk player
            statusManager.setPresentStatus(idle);
            idle.now();

            // build text component to broadcast
            ComponentBuilder componentBuilder = new ComponentBuilder();
            if (config.getBoolean("title-display-on-broadcast")) {
                componentBuilder.append(profile.getNameAsBaseComponent());
            } else {
                componentBuilder.append(profile.getRawName());
                componentBuilder.append(" 回来了！");
                componentBuilder.color(net.md_5.bungee.api.ChatColor.YELLOW);
                Bukkit.getServer().spigot().broadcast(componentBuilder.create());
            }

        } else { // if player is not in AFKPlayerArrayList, reset his idle time.
            idle.setIdle_TIME(0);
        }
    }

    public static void isAFK(Profile profile) {
        Player player = profile.getPlayer();
        StatusManager statusManager = profile.getStatusManager();
        AFK afk = statusManager.getAFK();
        Idle idle = statusManager.getIdle();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));

        // verify idle time
        if (idle.getIdle_TIME() < 300) { // 5 minutes
            // add idle time
            idle.addIdle_TIME(1);
        } else {
            // set AFK status
            statusManager.setPresentStatus(afk);
            afk.now();
        }
    }
}
