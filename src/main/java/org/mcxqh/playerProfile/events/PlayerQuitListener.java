package org.mcxqh.playerProfile.events;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.status.Status;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void EventQuitListener(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.profileMapWithUUID.get(player.getUniqueId());
        World world = player.getWorld();

        // Remove from all playerLists
        Profile.profileMapWithUUID.remove(player.getUniqueId());
        if (profile.getStatus().getAFK().isAFK()) {
            StatusListener.getAFKPlayerList().remove(profile);
        } else {
            StatusListener.getActivePlayerList().remove(profile);
        }

        // Save status config
        Status status = profile.getStatus();
        status.saveStatus();

        // Broadcast
        world.playSound(player, Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 10F, 5F);
        event.setQuitMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + player.getName());
    }
}