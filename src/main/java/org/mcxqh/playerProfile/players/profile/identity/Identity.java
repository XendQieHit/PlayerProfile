package org.mcxqh.playerProfile.players.profile.identity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.mcxqh.playerProfile.collectives.Collective;
import org.mcxqh.playerProfile.collectives.instances.Group;
import org.mcxqh.playerProfile.collectives.instances.Guild;

import java.util.UUID;

public class Identity {
    private AuthLevel authLevel;
    private final Collective collective;
    private final String level;
    private final UUID uuid;

    public Identity(AuthLevel authLevel, Collective collective, String level, UUID uuid) {
        this.authLevel = authLevel;
        this.collective = collective;
        this.level = level;
        this.uuid = uuid;
    }

    public boolean verify() {
        switch (collective.getClass().getSimpleName()) {
            case "Official" -> {
                if (collective instanceof Group group && group.isOfficial() && group.verify(uuid).equals(level)) return true;
            }
            case "Group", "Guild" -> {
                if ((collective instanceof Group group && group.verify(uuid).equals(level)) ||
                        collective instanceof Guild guild && guild.verify())
            }
            case "Server" -> {

            }
        }
    }

    public Collective getCollective() {
        return collective;
    }

    public AuthLevel getIdentityType() {
        return authLevel;
    }

    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }
}
