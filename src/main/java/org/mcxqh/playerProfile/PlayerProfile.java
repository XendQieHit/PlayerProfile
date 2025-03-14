package org.mcxqh.playerProfile;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcxqh.playerProfile.commands.profile.mainCommand;
import org.mcxqh.playerProfile.events.PlayerChatListener;
import org.mcxqh.playerProfile.events.PlayerJoinListener;
import org.mcxqh.playerProfile.events.PlayerQuitListener;
import org.mcxqh.playerProfile.events.StatusListener;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class PlayerProfile extends JavaPlugin {
    /* Config Side */
    private final FileConfiguration pluginConfig = getConfig();
    private final File dataFolder = getDataFolder();
    private final File configFile = new File(dataFolder,"config.yml");
    public static final ArrayList<Player> playerArrayList = new ArrayList<>();
    public static final ArrayList<String> playerNameArrayList = new ArrayList<>();

    @Override
    public void onEnable() {
        // Initialize all player if existed.
        // But this section do not work as usual. I can not find the reason. Is there anyone could find?
        for (World world : getServer().getWorlds()) {
            if (!world.getPlayers().isEmpty()) {
                playerArrayList.addAll(world.getPlayers());
            }
        }
        if (!playerArrayList.isEmpty()) {
            for (Player player : playerArrayList) {
                playerNameArrayList.add(player.getName());
            }
            for (Player player : playerArrayList) {
                PlayerJoinListener.EventJoinHandler(player);
            }
        }

        // Load config
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
        Logger.getLogger("PlayerProfile").info("Reading Config...");

        // Friendly TP
        if (pluginConfig.getBoolean("tp.toggle-enable")) {
            //...
            Logger.getLogger("PlayerProfile").info("Allow TP times: " + pluginConfig.getInt("tp.time-limit.times"));
        }
        // CustomStatus
        if (pluginConfig.getBoolean("status.enable-custom")) {
            //...
            Logger.getLogger("PlayerProfile").info("Enable custom-status: " + pluginConfig.getBoolean("status.enable-custom"));

        }
        // PlayerTitle
        if (pluginConfig.getBoolean("title.toggle-enable")) {
            Logger.getLogger("PlayerProfile").info("Enable Player: " + pluginConfig.getBoolean("title.toggle-enable"));

        }

        // Register EventListeners
        getServer().getPluginManager().registerEvents(new StatusListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                StatusListener.AFKDetector();
            }
        }, 0L, 20L); // per 1 second

        // 这里原本是优化数组长度的定时任务，但考虑性能开销，就先注释掉了
        /*Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                playerArrayList.trimToSize();
                playerNameArrayList.trimToSize();
            }
        }, 0L, 6000L);*/

        // Register Commands
        this.getCommand("profile").setExecutor(new mainCommand());
        this.getCommand("status").setExecutor(new org.mcxqh.playerProfile.commands.status.mainCommand());
        this.getCommand("titles").setExecutor(new org.mcxqh.playerProfile.commands.titles.mainCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic\
        for (Player player : playerArrayList) {
            Status status = Profile.profileMapWithUUID.get(player.getUniqueId()).getStatus();
            status.saveStatus();

        }
    }
}
