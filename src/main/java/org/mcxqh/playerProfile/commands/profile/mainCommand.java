package org.mcxqh.playerProfile.commands.profile;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.profile.subcommand.status;
import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;

public class mainCommand implements CommandExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public mainCommand() {
        this.subCommands.add(new status());
        this.subCommands.add(new status());
    }
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        if (Data.playerMapWithName.containsKey(args[0])) { // if args contains player's name
            player = Data.playerMapWithName.get(args[0]);
        } else {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.spigot().sendMessage(new ComponentBuilder("/profile <Player> custom|list|reload|set|toggle").color(ChatColor.YELLOW).create());
                return true;
            }
        }

        for (SubCommand subCommand : subCommands) {
            if (args[0].equalsIgnoreCase(subCommand.getClass().getSimpleName())) {
                return subCommand.run(sender, player, args);
            }
        }
        return false;
    }
}