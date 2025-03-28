package org.mcxqh.playerProfile.players.profile.identity;

import org.mcxqh.playerProfile.collectives.Collective;
import org.mcxqh.playerProfile.collectives.instances.Group;
import org.mcxqh.playerProfile.collectives.instances.Guild;
import org.mcxqh.playerProfile.collectives.instances.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class IdentityFactory {

    public IdentityFactory() {}

    public Identity process(AuthLevel authLevel, UUID uuid, Collective collective) {
        // 参数校验
        if (authLevel == null || uuid == null || collective == null) {
            throw new IllegalArgumentException("Input parameters cannot be null");
        }

        // 根据 AuthLevel 和 Collective 类型选择处理逻辑
        return switch (authLevel) {
            case OFFICIAL -> handleOfficial(uuid, collective);
            case GROUP, GUILD -> handleGroupOrGuild(authLevel, uuid, collective);
            case TEAM -> handleTeam(uuid, collective);
            default -> null; // 或者抛出异常：throw new UnsupportedOperationException("Unsupported AuthLevel");
        };
    }

    private Identity handleOfficial(UUID uuid, Collective collective) {
        if (collective instanceof Group group && group.isOfficial()) {
            return createIdentity(AuthLevel.OFFICIAL, collective, uuid);
        }
        return null;
    }

    private Identity handleGroupOrGuild(AuthLevel authLevel, UUID uuid, Collective collective) {
        if (collective instanceof Group || collective instanceof Guild) {
            return createIdentity(authLevel, collective, uuid);
        }
        return null;
    }

    private Identity handleTeam(UUID uuid, Collective collective) {
        if (collective instanceof Team) {
            return createIdentity(AuthLevel.TEAM, collective, uuid);
        }
        return null;
    }

    private Identity createIdentity(AuthLevel authLevel, Collective collective, UUID uuid) {
        String level = collective.verify(uuid);
        if (level == null) return null;
        return new Identity(authLevel, collective, level, uuid);
    }
}
