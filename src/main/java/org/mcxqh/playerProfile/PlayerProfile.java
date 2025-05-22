package org.mcxqh.playerProfile;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcxqh.playerProfile.commands.status.mainCommand;
import org.mcxqh.playerProfile.events.*;
import org.mcxqh.playerProfile.players.Profile;

import java.io.File;
import java.util.logging.Logger;

public final class PlayerProfile extends JavaPlugin {
    /* Config Side */
    private final FileConfiguration pluginConfig = getConfig();
    public static PlayerProfile instance;
    private final File dataFolder = getDataFolder();
    private final File configFile = new File(dataFolder,"config.yml");
    public static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        instance = this;
        // Initialize all player if existed.
        // But this section do not work as usual. I can not find the reason. Is there anyone could find?
        for (World world : getServer().getWorlds()) {
            if (!world.getPlayers().isEmpty()) {
                world.getPlayers().forEach(player -> Data.PLAYER_MAP_WITH_UUID.put(player.getUniqueId(), player));
            }
        }
        if (!Data.PLAYER_MAP_WITH_UUID.isEmpty()) {
            Data.PLAYER_MAP_WITH_UUID.forEach((uuid, player) -> {
                Data.PLAYER_NAME_SET.add(player.getName());
                PlayerJoinListener.EventJoinHandler(player);
            });
        }

        // Initialize ProtocolLib
        protocolManager = ProtocolLibrary.getProtocolManager();

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
        Bukkit.getPluginManager().registerEvents(new StatusListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerClickInventory(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, StatusListener::AFKDetector, 0L, 20L); // per 1 second

        // Register Commands
        this.getCommand("profile").setExecutor(new org.mcxqh.playerProfile.commands.profile.mainCommand());
        this.getCommand("status").setExecutor(new mainCommand());
        this.getCommand("titles").setExecutor(new org.mcxqh.playerProfile.commands.titles.mainCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic\
        Data.PLAYER_MAP_WITH_UUID.forEach((uuid, player) -> {
            Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
            profile.save();
        });
    }
}
