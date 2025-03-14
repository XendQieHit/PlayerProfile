package org.mcxqh.playerProfile.events;

import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;

import java.io.FileNotFoundException;
import java.util.ConcurrentModificationException;
import java.util.logging.Logger;

public class PlayerJoinListener implements Listener {
    private Configuration configuration;

    @EventHandler(priority = EventPriority.LOWEST)
    public void EventJoinListener(PlayerJoinEvent event) {
        // initialize new player
        org.bukkit.entity.Player player = event.getPlayer();
        EventJoinHandler(player);

        // broadcast
        World world = player.getWorld();
        world.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 5F, 5F);
        event.setJoinMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + player.getName());

    }

    public static void EventJoinHandler(Player player) {
        FileHandler fileHandler = new FileHandler();

        // add new profile
        Profile profile = new Profile(player);

        // load json file
        JsonObject JsonStatusSetting, JsonProfileSetting;
        try {
            JsonStatusSetting = fileHandler.getStatus(player);
        } catch (FileNotFoundException e) {
            Logger.getLogger("PlayerProfile").severe("Could not read json file: " + e);
            throw new RuntimeException(e);
        }

        // load player status setting
        profile.getStatus().loadSetting();

        // put player into statusListener pool
        try {
            StatusListener.getActivePlayerList().add(profile);
            PlayerProfile.playerArrayList.add(player);
            PlayerProfile.playerNameArrayList.add(player.getName());

            Logger.getLogger("PlayerProfile").info("Add " + player.getName() + " successfully!");
        } catch (ConcurrentModificationException e) {
            Logger.getLogger("PLayerProfile").info("Add " + player.getName() + " failed");
        }
    }
}