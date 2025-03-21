package org.mcxqh.playerProfile.commands.status.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.status.mainCommand;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.List;
import java.util.logging.Logger;

public class set implements SubCommand {

    /**
     * @param args [Status]
     */
    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();

        Logger.getLogger("PlayerProfile").info(args.toString());

        for (Status status : statusManager.getAllSubStatuses()) {
            if (args[0].equalsIgnoreCase(status.getClass().getSimpleName())) {
                status.now();
                sender.spigot().sendMessage(new ComponentBuilder("设置成功").color(ChatColor.YELLOW).create());
            }
        }
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();
        return args.length == 1 ? mainCommand.pair(args[0], statusManager.getAllSubStatusNames()) : List.of();
    }
}
