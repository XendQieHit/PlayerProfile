package org.mcxqh.playerProfile;

import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.collectives.instances.Guild;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.players.Profile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    // Normally, map with name is usually used in Player-oriented operation, such as command input.
    public static final Map<UUID, Profile> PROFILE_MAP_WITH_UUID = new ConcurrentHashMap<>();
    public static final Map<String, Profile> PROFILE_MAP_WITH_NAME = new ConcurrentHashMap<>();

    public static final Map<String, Player> PLAYER_MAP_WITH_NAME = new ConcurrentHashMap<>();
    public static final Map<UUID, Player> PLAYER_MAP_WITH_UUID = new ConcurrentHashMap<>();

    public static final HashSet<String> PLAYER_NAME_SET = new HashSet<>();
    public static final List<Guild> GUILDS = new ArrayList<>();

    /* The update of the Map is in lazy, only update when display() worked in GUI class implementing GUITemplate. */
    public static final Map<Player, GUIMeta> GUI_META_MAP_FOR_PLAYER = new HashMap<>();
}
