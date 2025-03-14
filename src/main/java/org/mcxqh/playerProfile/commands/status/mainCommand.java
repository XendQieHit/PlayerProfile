package org.mcxqh.playerProfile.commands.status;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.status.subcommand.custom;
import org.mcxqh.playerProfile.commands.status.subcommand.list;
import org.mcxqh.playerProfile.commands.status.subcommand.set;
import org.mcxqh.playerProfile.commands.status.subcommand.toggle;
import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class mainCommand implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    private final ArrayList<String> subCommandsNames = new ArrayList<>();
    /**
     * The Constructor will be executed while this command initialized.
     */
    public mainCommand() {
        this.subCommands.add(new custom());
        this.subCommands.add(new list());
        this.subCommands.add(new set());
        this.subCommands.add(new toggle());
        for (SubCommand subCommand : subCommands) {
            subCommandsNames.add(subCommand.getClass().getSimpleName().toLowerCase());
        }
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
     * @param args [Player] custom|list|set|toggle [...]
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // args pretreatment
        Player player;
        if (args.length != 0) { // if args is not empty.

            if (Profile.playerMapWithName.containsKey(args[0])) { // if args contains player's name
                player = Profile.playerMapWithName.get(args[0]);
                args = removeFirst(args);
            } else {
                if (sender instanceof Player) {
                    player = (Player) sender;
                } else {
                    sender.spigot().sendMessage(new ComponentBuilder("/status <Player> custom|list|reload|set|toggle").color(ChatColor.YELLOW).create());
                    return true;
                }
            }

            // run command
            for (SubCommand subCommand : subCommands) {
                if (args[0].equalsIgnoreCase(subCommand.getClass().getSimpleName())) {
                    Logger.getLogger("PlayerProfile").info("start: " + Arrays.toString(args));
                    args = removeFirst(args);
                    return subCommand.run(sender, player, args);
                }
            }
            return false;

        } else {
            return false;
        }
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        ArrayList<String> playerNameList = PlayerProfile.playerNameArrayList;
        if (!args[0].isEmpty()) { // if args is not empty.

            if (Profile.playerMapWithName.containsKey(args[0])) { // if args contains player's name
                player = Profile.playerMapWithName.get(args[0]);
                args = removeFirst(args);
            }

            // run tabCompleter
            ArrayList<String> linkedList = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                String subCommandName = subCommand.getClass().getSimpleName();

                if (args[0].equalsIgnoreCase(subCommandName) && args.length >= 2) {
                    args = removeFirst(args);
                    Logger.getLogger("PlayerProfile").info(Arrays.toString(args));
                    return subCommand.tab(args, player);
                } else if (args.length == 1 && subCommandName.startsWith(args[0].toLowerCase())) {
                    linkedList.add(subCommandName);
                }
            }
            return linkedList;

        } else {
            return subCommandsNames;
        }
    }

    /**
     * Using for args, remove first element of args and make up.
     */
    public static String[] removeFirst(String[] args) {
        String[] args1 = new String[args.length - 1];
        System.arraycopy(args, 1, args1, 0, args.length - 1);
        return args1;
    }

    /**
     * Return a filtered list by <code>startWith</code>.
     * Isn't that a similar effect like vanilla?
     */
    public static List<String> pair(String keyword, List<String> resourceList) {
        ArrayList<String> linkedList = new ArrayList<>();

        for (String subStatusName : resourceList) {
            if (subStatusName.startsWith(keyword.toLowerCase())) {
                linkedList.add(subStatusName);
            }
        }
        return linkedList;
    }
}