package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;

import java.util.List;

public class create implements SubCommand {

    /**
     * @param args [Name] [Color] [Description]
     */
    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {
        FileHandler fileHandler = new FileHandler();
        if (sender instanceof Player) {
            Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        } else {
            sender.spigot().sendMessage(new ComponentBuilder("This command is for entity of player.").color(ChatColor.YELLOW).create());
        }
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
