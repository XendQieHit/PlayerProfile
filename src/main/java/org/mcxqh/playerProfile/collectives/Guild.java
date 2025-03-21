package org.mcxqh.playerProfile.collectives;

import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Guild {
    private final String name;
    private Profile leader;
    private final List<Profile> managers = new ArrayList<>();
    private final List<UUID> members = new ArrayList<>();

    public Guild(String name, Profile leader) {
        this.name = name;
        this.leader = leader;
    }
}
