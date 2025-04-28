package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;

import java.util.List;

public class hide implements SubCommand {
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        Data.PROFILE_MAP_WITH_UUID.get(operatorPlayer.getUniqueId()).getTitleManager().setPresentTitle(null);
        sender.spigot().sendMessage(new ComponentBuilder("已隐藏称号").color(ChatColor.YELLOW.asBungee()).create());
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
