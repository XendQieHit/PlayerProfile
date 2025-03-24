package org.mcxqh.playerProfile.players.profile.identity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.mcxqh.playerProfile.collectives.Collective;

public class Identity {
    private IdentityType identityType;
    private Collective collective;

    public Identity(IdentityType identityType, Collective collective) {
        this.identityType = identityType;
        this.collective = collective;
    }

    public Collective getCollective() {
        return collective;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public JsonObject toJson() {
        Gson gson = new Gson();
        return (JsonObject) gson.toJsonTree(this);
    }
}
