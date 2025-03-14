package org.mcxqh.playerProfile.commands.status.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.status.mainCommand;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.status.Status;
import org.mcxqh.playerProfile.players.profile.status.SubStatus;

import java.util.List;
import java.util.logging.Logger;

public class set implements SubCommand {

    /**
     * @param args [Status]
     */
    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {
        Profile profile = Profile.profileMapWithUUID.get(player.getUniqueId());
        Status status = profile.getStatus();

        Logger.getLogger("PlayerProfile").info(args.toString());

        for (SubStatus subStatus : status.getAllSubStatuses()) {
            if (args[0].equalsIgnoreCase(subStatus.getClass().getSimpleName())) {
                subStatus.now();
                sender.spigot().sendMessage(new ComponentBuilder("设置成功").color(ChatColor.YELLOW).create());
            }
        }
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Profile.profileMapWithUUID.get(player.getUniqueId());
        Status status = profile.getStatus();
        return args.length == 1 ? mainCommand.pair(args[0], status.getAllSubStatusNames()) : List.of();
    }
}
