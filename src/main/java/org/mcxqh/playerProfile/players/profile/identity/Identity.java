package org.mcxqh.playerProfile.players.profile.identity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.mcxqh.playerProfile.collectives.Collective;
import org.mcxqh.playerProfile.collectives.instances.Group;
import org.mcxqh.playerProfile.collectives.instances.Guild;

public class Identity {
    private AuthLevel authLevel;
    private final Collective collective;
    private final String level;

    public Identity(AuthLevel authLevel, Collective collective, String level) {
        this.authLevel = authLevel;
        this.collective = collective;
        this.level = level;
    }

    public boolean verify() {
        switch (collective.getClass().getSimpleName()) {
            case "Group", "Guild" -> {

            }
            case "Official" -> {

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
