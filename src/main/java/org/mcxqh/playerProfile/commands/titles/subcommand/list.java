package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.List;

public class list implements SubCommand {
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        ComponentBuilder componentBuilder = new ComponentBuilder("已拥有的称号：").color(ChatColor.YELLOW);

        for (Title title : Data.PROFILE_MAP_WITH_UUID.get(operatorPlayer.getUniqueId()).getTitleManager().getTitleArrayList())
            componentBuilder.append("\n").append(title.toBaseComponent());

        sender.spigot().sendMessage(componentBuilder.create());
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
