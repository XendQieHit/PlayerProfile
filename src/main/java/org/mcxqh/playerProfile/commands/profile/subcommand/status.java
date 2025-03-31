package org.mcxqh.playerProfile.commands.profile.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.commands.SubCommand;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class status implements SubCommand {
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        StringBuilder argsC = new StringBuilder();
        for (String s : args) {
            argsC.append(" ").append(s);
        }
        return operatorPlayer.performCommand("status " + argsC);
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
