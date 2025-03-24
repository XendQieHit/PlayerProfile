package org.mcxqh.playerProfile.collectives.instances;

import org.mcxqh.playerProfile.collectives.Collective;

import java.util.*;

public class Group extends Collective {
    private final Map<UUID, String> managerMap = new HashMap<>();
    private final Map<String, UUID> reverseManagerMap = new HashMap<>();
    private boolean isOfficial;

    public Group(String name) {
        this.name = name;
        isOfficial = false;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public Map<UUID, String> getManagerMap() {
        return managerMap;
    }

    public void removeFromManager(String playerName) {

    }
}
