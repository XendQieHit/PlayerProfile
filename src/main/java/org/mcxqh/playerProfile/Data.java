package org.mcxqh.playerProfile;

import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.collectives.Guild;
import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    public static final Map<UUID, Profile> profileMapWithUUID = new ConcurrentHashMap<>();
    public static final Map<String, Player> playerMapWithName = new ConcurrentHashMap<>();
    public static final Map<UUID, Player> playerMapWithUUID = new ConcurrentHashMap<>();
    public static final ArrayList<String> playerNameArrayList = new ArrayList<>();
    public static final List<Guild> guilds = new ArrayList<>();
}
