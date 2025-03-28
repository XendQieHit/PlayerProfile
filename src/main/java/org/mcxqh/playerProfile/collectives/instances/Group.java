package org.mcxqh.playerProfile.collectives.instances;

import org.mcxqh.playerProfile.collectives.Collective;

import java.util.*;

public class Group extends Collective {
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

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }

    public void removeFromManager(String playerName) {

    }
}
