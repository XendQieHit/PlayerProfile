package org.mcxqh.playerProfile.events;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;

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

        // load player setting and data
        profile.load();

        // put player into statusListener pool
        try {
            StatusListener.getActivePlayerList().add(profile);
            Data.playerMapWithUUID.put(player.getUniqueId(), player);
            Data.playerNameSet.add(player.getName());

            Logger.getLogger("PlayerProfile").info("Add " + player.getName() + " successfully!");
        } catch (ConcurrentModificationException e) {
            Logger.getLogger("PLayerProfile").info("Add " + player.getName() + " failed");
        }
    }
}