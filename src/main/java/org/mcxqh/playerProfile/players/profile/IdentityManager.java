package org.mcxqh.playerProfile.players.profile;

import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.players.profile.identity.Identity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IdentityManager {
    private final Player player;
    private final Set<Identity> identities = new HashSet<>();
    private Identity presentIdentity = null;

    public IdentityManager(Player player) {
        this.player = player;
    }

    public Set<Identity> getIdentities() {
        return identities;
    }

    public Identity getPresentIdentity() {
        return presentIdentity;
    }

    public void setPresentIdentity(Identity identity) {
        presentIdentity = identity;
    }

    public void addIdentity(Identity identity) {
        identities.add(identity);
    }

    public void removeIdentity(Identity identity) {
        identities.remove(identity);
    }

    /**
     * Get String List of Identities in the format that <code>%s.%s.%s</code>.
     * Batch with <code>Identity.toString()</code>.
     */
    public List<String> getIdentitiesAsStringList() {
        return identities.stream()
                .map(Identity::toString)
                .toList();
    }
}
