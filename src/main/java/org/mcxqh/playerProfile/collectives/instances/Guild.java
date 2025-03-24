package org.mcxqh.playerProfile.collectives.instances;

import org.mcxqh.playerProfile.collectives.Collective;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Guild extends Collective {
    private final Map<UUID, String> managerMap = new ConcurrentHashMap<>();


}
