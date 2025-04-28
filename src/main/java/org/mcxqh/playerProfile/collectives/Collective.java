package org.mcxqh.playerProfile.collectives;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.IdentityManager;
import org.mcxqh.playerProfile.players.profile.identity.AuthLevel;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.identity.IdentityLevel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public abstract class Collective {
    /**
     * To make sure less memory occupation, member is stored with Name.
     * If you want to get member as Profile, you need to convert it or use <code>getMemberAsProfile</code>
     */
    protected String name;
    protected AuthLevel authLevel;
    protected final Map<UUID, String> memberMap = new ConcurrentHashMap<>();
    protected final Map<UUID, String> managerMap = new ConcurrentHashMap<>();
    protected UUID leader;

    public void add(Player player) {
        add(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()));
    }
    public void add(Profile profile) {
        memberMap.put(profile.getUniqueId(), profile.getRawName());
        profile.getIdentityManager().addIdentity(Identity.of(this.authLevel, profile.getUniqueId(), profile.getRawName(), this, IdentityLevel.MEMBER));
    }

    public void addManager(Player player) {
        addManager(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()));
    }
    public void addManager(Profile profile) {
        managerMap.put(profile.getUniqueId(), profile.getRawName());
        profile.getIdentityManager().addIdentity(Identity.of(this.authLevel, profile.getUniqueId(), profile.getRawName(), this, IdentityLevel.MANAGER));
    }

    public void removeManager(Player player) {
        removeManager(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()));
    }
    public void removeManager(Profile profile) {
        profile.getIdentityManager().removeIdentity(Identity.of(this.authLevel, profile.getUniqueId(), profile.getRawName(), this, IdentityLevel.MANAGER));
        managerMap.remove(profile.getUniqueId());
    }

    public void remove(Player player) {
        remove(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()));
    }
    public void remove(Profile profile) {
        UUID uuid = profile.getUniqueId();
        IdentityManager identityManager = profile.getIdentityManager();
        if (leader.equals(uuid)) {
            Logger.getLogger("PlayerProfile").info("Can't remove leader!");
            return;
        }
        if (managerMap.containsKey(uuid)) {
            removeManager(profile);
        }
        memberMap.remove(uuid);
        identityManager.removeIdentity(Identity.of(this.authLevel, uuid, profile.getRawName(), this, IdentityLevel.MEMBER));
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
                .map(uuid -> Data.PROFILE_MAP_WITH_UUID.get(uuid))
                .toList();
    }

    /**
     * Get all members as <code>Player</code> wrapped in a List.
     * Because of base on map-getting action, this method should not be used frequently.
     */
    public List<Player> getAllMemberAsPlayers() {
        return Arrays.stream(memberMap.values().toArray())
                .map(uuid -> Data.PLAYER_MAP_WITH_UUID.get(uuid))
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

    public IdentityLevel verify(UUID uuid) {
        if (!memberMap.containsKey(uuid)) {
            return null;
        }
        if (managerMap.containsKey(uuid)) {
            return IdentityLevel.MANAGER;
        }
        if (leader.equals(uuid)) {
            return IdentityLevel.LEADER;
        }
        return IdentityLevel.MEMBER;
    }

    public Map<UUID, String> getManagerMap() {
        return managerMap;
    }

    abstract public void save();

    abstract public void load();
}
