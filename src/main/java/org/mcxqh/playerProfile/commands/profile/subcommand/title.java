package org.mcxqh.playerProfile.commands.profile.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.commands.SubCommand;

import java.util.List;

public class title implements SubCommand {
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        return operatorPlayer.performCommand(String.join(" ", args));
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
