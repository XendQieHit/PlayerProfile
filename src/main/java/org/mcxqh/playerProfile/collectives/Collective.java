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
    protected final Map<String, UUID> reverseMemberMap = new ConcurrentHashMap<>();
    protected UUID leader;

    public void addMember(Player player) {
        memberMap.put(player.getUniqueId(), player.getName());
        reverseMemberMap.put(player.getName(), player.getUniqueId());
    }
    public void addMember(Profile profile) {
        memberMap.put(profile.getUniqueId(), profile.getName());
        reverseMemberMap.put(profile.getName(), profile.getUniqueId());
    }

    /**
     * Get member's <code>Profile</code> object.
     * @return <code>null</code> if this member doesn't exist.
     */
    public Profile getMemberAsProfile(UUID uuid) {
        if (memberMap.containsKey(uuid)) {
            return Data.profileMapWithUUID.get(uuid);
        }
        return null;
    }
    public Profile getMemberAsProfile(String playerName) {
        if (reverseMemberMap.containsKey(playerName)) {
            return Data.profileMapWithName.get(playerName);
        }
        return null;
    }

    /**
     * Get member's <code>Player</code> object.
     * @return <code>null</code> if this member doesn't exist.
     */
    public Player getMemberAsPlayer(UUID uuid) {
        if (memberMap.containsKey(uuid)) {
            return Data.playerMapWithUUID.get(uuid);
        }
        return null;
    }
    public Player getMemberAsPlayer(String playerName) {
        if (reverseMemberMap.containsKey(playerName)) {
            return Data.playerMapWithName.get(playerName);
        }
        return null;
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

    abstract public void

    abstract public void save();

    abstract public void load();
}
