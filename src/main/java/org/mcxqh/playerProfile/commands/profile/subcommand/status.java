package org.mcxqh.playerProfile.commands.profile.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.status.mainCommand;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class status implements SubCommand {
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        return operatorPlayer.performCommand(String.join(" ", args));
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
