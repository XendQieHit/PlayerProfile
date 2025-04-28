package org.mcxqh.playerProfile.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {
    boolean run(CommandSender sender, Player operatorPlayer, String[] args);
    List<String> tab(String[] args, Player player);
}
