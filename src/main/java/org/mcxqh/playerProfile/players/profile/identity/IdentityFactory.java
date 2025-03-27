package org.mcxqh.playerProfile.players.profile.identity;

import org.mcxqh.playerProfile.collectives.Collective;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Logger;

public class IdentityFactory {

    public IdentityFactory() {}

    public Identity process(UUID uuid, Collective collective, AuthLevel authLevel) {
        String collectiveType = collective.getClass().getSimpleName();

        String level;

        switch (collectiveType) {
            case "Group", "Guild" -> {
            }
            default -> {
                Logger.getLogger("PlayerProfile").info("No Such Collective Type.");
                throw new RuntimeException();
            }
        }
        return new Identity(authLevel, collective, level);
    }
}
