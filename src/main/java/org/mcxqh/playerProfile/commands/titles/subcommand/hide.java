package org.mcxqh.playerProfile.commands.titles.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.titles.mainCommand;

import java.util.List;

public class hide implements SubCommand {
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        Data.profileMapWithUUID.get(operatorPlayer.getUniqueId()).getTitleManager().setPresentTitle(null);
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
