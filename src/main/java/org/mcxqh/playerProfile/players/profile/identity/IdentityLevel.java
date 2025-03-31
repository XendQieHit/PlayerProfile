package org.mcxqh.playerProfile.players.profile.identity;

public enum IdentityLevel {
    MEMBER("Member"),
    MANAGER("Manager"),
    LEADER("Leader");

    private final String name;

    IdentityLevel(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
