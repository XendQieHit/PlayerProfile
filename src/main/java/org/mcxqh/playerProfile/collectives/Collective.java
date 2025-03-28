package org.mcxqh.playerProfile.collectives;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Collective {
    /**
     * To make sure less memory occupation, member is stored with Name.
     * If you want to get member as Profile, you need to convert it or use <code>getMemberAsProfile</code>
     */
    protected String name;
    protected final Map<UUID, String> memberMap = new ConcurrentHashMap<>();
    protected final Map<UUID, String> managerMap = new ConcurrentHashMap<>();
    protected UUID leader;

    public void addMember(Player player) {
        memberMap.put(player.getUniqueId(), player.getName());
    }
    public void addMember(Profile profile) {
        memberMap.put(profile.getUniqueId(), profile.getName());
    }

    public Map<UUID, String> getMemberMap() {
        return memberMap;
    }

    /**
     * Get all member's name wrapped in a List.
     */
    public List<String> getAllMemberAsName() {
        return (List<String>) memberMap.values();
    }

    /**
     * Get all members as <code>Profile</code> wrapped in a List.
     * Because of base on map-getting action, this method should not be used frequently.
     */
    public List<Profile> getAllMemberAsProfiles() {
        return Arrays.stream(memberMap.keySet().toArray())
                .map(uuid -> Data.profileMapWithUUID.get(uuid))
                .toList();
    }

    /**
     * Get all members as <code>Player</code> wrapped in a List.
     * Because of base on map-getting action, this method should not be used frequently.
     */
    public List<Player> getAllMemberAsPlayers() {
        return Arrays.stream(memberMap.values().toArray())
                .map(uuid -> Data.playerMapWithUUID.get(uuid))
                .toList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }

    public String verify(UUID uuid) {
        if (memberMap.containsKey(uuid)) {
            if (managerMap.containsKey(uuid)) {
                return "Manager";
            }
            return "Member";
        }
        return null;
    }

    public Map<UUID, String> getManagerMap() {
        return managerMap;
    }

    abstract public void save();

    abstract public void load();
}
