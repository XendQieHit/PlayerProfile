package org.mcxqh.playerProfile.events;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class PlayerChatListener implements Listener {
    private FileConfiguration config = YamlConfiguration.loadConfiguration(new File(new File("plugins/PlayerProfile"),"config.yml"));
    private final Map<UUID, Profile> profileMapWithUUID = Data.profileMapWithUUID;
    @EventHandler(priority = EventPriority.NORMAL)
    public void EventChatListener(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = profileMapWithUUID.get(player.getUniqueId());
        Title title = profile.getTitleManager().getPresentTitle();

        // build message
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append(profile.getMixedName()); // Player
        componentBuilder.append(event.getMessage()); // Message
        Bukkit.getServer().spigot().broadcast(componentBuilder.create());
    }
}
