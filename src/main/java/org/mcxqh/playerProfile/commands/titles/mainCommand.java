package org.mcxqh.playerProfile.commands.titles;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.titles.subcommand.add;
import org.mcxqh.playerProfile.commands.titles.subcommand.hide;
import org.mcxqh.playerProfile.commands.titles.subcommand.list;
import org.mcxqh.playerProfile.commands.titles.subcommand.set;

import java.util.ArrayList;
import java.util.List;

public class mainCommand implements TabExecutor {
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public mainCommand() {
        this.subCommands.add(new add());
        this.subCommands.add(new list());
        this.subCommands.add(new set());
        this.subCommands.add(new hide());
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
     * @param args    [Player] add|list|set|hide [...]
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.spigot().sendMessage(SendHelpMsg());
        return true;
    }

    private BaseComponent[] SendHelpMsg() {
        ComponentBuilder componentBuilder = new ComponentBuilder("===================" + ChatColor.YELLOW + "/titles" + ChatColor.AQUA + "=================\n").color(ChatColor.AQUA);
        componentBuilder.append("/title add [称号名] [颜色] 添加自定义称号\n").color(ChatColor.YELLOW);
        componentBuilder.append("/title list                 查看拥有的称号\n");
        componentBuilder.append("/title set [称号]           展示称号\n");
        componentBuilder.append("/title hide [称号]          隐藏称号\n");
        componentBuilder.append("===========================================").color(ChatColor.AQUA);
        return componentBuilder.create();
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
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
        return List.of();
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
