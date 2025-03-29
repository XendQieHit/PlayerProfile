package org.mcxqh.playerProfile;

import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.collectives.instances.Guild;
import org.mcxqh.playerProfile.players.Profile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    // Normally, map with name is usually used in Player-oriented operation, such as command input.
    public static final Map<UUID, Profile> profileMapWithUUID = new ConcurrentHashMap<>();
    public static final Map<String, Profile> profileMapWithName = new ConcurrentHashMap<>();

    public static final Map<String, Player> playerMapWithName = new ConcurrentHashMap<>();
    public static final Map<UUID, Player> playerMapWithUUID = new ConcurrentHashMap<>();

    public static final HashSet<String> playerNameSet = new HashSet<>();
    public static final List<Guild> guilds = new ArrayList<>();
}
