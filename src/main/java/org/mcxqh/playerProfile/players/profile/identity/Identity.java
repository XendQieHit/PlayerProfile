package org.mcxqh.playerProfile.players.profile.identity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcxqh.playerProfile.collectives.Collective;
import org.mcxqh.playerProfile.collectives.instances.Group;
import org.mcxqh.playerProfile.collectives.instances.Guild;
import org.mcxqh.playerProfile.collectives.instances.Team;

import java.util.UUID;

public class Identity {
    private final AuthLevel authLevel;
    private final Collective collective;
    private final IdentityLevel level;
    private final UUID uuid;
    private final String ownerName;

    /**
     * Create Identity without verified.
     * If you want to create Identity safely, use <code>Identity.of()</code>.
     */
    public Identity(AuthLevel authLevel, UUID uuid, String ownerName, Collective collective, IdentityLevel level) {
        this.authLevel = authLevel;
        this.collective = collective;
        this.level = level;
        this.uuid = uuid;
        if (ownerName != null) this.ownerName = ownerName;
        else this.ownerName = "";
    }

    public boolean verify() {
        switch (authLevel) {
            case OFFICIAL -> {
                return collective instanceof Group group && group.isOfficial() && group.verify(uuid).equals(level);
            }
            case GROUP, GUILD -> {
                return collective instanceof Group group && group.verify(uuid).equals(level) ||
                        collective instanceof Guild guild && guild.verify(uuid).equals(level);
            }
            case TEAM -> {
                return collective instanceof Team team && team.verify(uuid).equals(level);
            }
        }
        return false;
    }

    public AuthLevel getAuthLevel() {
        return authLevel;
    }

    public Collective getCollective() {
        return collective;
    }

    public AuthLevel getIdentityType() {
        return authLevel;
    }

    public IdentityLevel getIdentityLevel() {
        return level;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }

    public BaseComponent[] toBaseComponent() {
        ComponentBuilder componentBuilder = new ComponentBuilder();
        componentBuilder.append("[" + authLevel.toString() + "]" + ownerName);
        componentBuilder.color(authLevel.getColor().asBungee());
        return componentBuilder.create();
    }

    /**
     * Get String of Identities in the format of <code>%s.%s.%s</code>.
     * For example: <pre>{@code
     * Identity identity = new identity(AuthLevel.GROUP, exampleGroup, IdentityLevel.Manager));
     * String s = identity.toString();
     * }</pre>
     * <code>s</code> will be converted into String of <code>"Group.ExampleGroup.Manager"</code>
     */
    @Override
    public String toString() {
        return String.format("%s.%s.%s", collective.getClass().getSimpleName(), getCollective().getName(), level);
    }

    /**
     * Return Identity Object, which is verified.
     * Because of checking maps of collective, This method must only be used after player's uuid added into maps of collective.
     */
    public static Identity of(@NotNull AuthLevel authLevel, UUID uuid, @Nullable String ownerName , @Nullable Collective collective, @Nullable IdentityLevel level) {
        // 根据 AuthLevel 和 Collective 类型选择处理逻辑
        return switch (authLevel) {
            case OFFICIAL -> handleOfficial(uuid, collective, level, ownerName);
            case GROUP, GUILD -> handleGroupOrGuild(authLevel, uuid, collective, level, ownerName);
            case TEAM -> handleTeam(uuid, collective, level, ownerName);
            case PERSONAL -> new Identity(AuthLevel.PERSONAL, uuid, ownerName, null, null);
            case SERVER -> new Identity(AuthLevel.SERVER, null, null, null, null);
        };
    }
    private static Identity handleOfficial(UUID uuid, Collective collective, IdentityLevel level, String ownerName) {
        if (collective instanceof Group group && group.isOfficial()) {
            return createIdentity(AuthLevel.OFFICIAL, collective, uuid, ownerName, level);
        }
        return null;
    }
    private static Identity handleGroupOrGuild(AuthLevel authLevel, UUID uuid, Collective collective, IdentityLevel level, String ownerName) {
        if (collective instanceof Group || collective instanceof Guild) {
            return createIdentity(authLevel, collective, uuid, ownerName, level);
        }
        return null;
    }
    private static Identity handleTeam(UUID uuid, Collective collective, IdentityLevel level, String ownerName) {
        if (collective instanceof Team) {
            return createIdentity(AuthLevel.TEAM, collective, uuid, ownerName, level);
        }
        return null;
    }
    private static Identity createIdentity(AuthLevel authLevel, Collective collective, @NotNull UUID uuid, String ownerName, IdentityLevel level) {
        if (collective != null && level != null && level != collective.verify(uuid)) return null;
        return new Identity(authLevel, uuid, ownerName, collective, level);
    }
}
