package org.mcxqh.playerProfile.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CommandUtils {
    /**
     * Using for args, remove first element of args and make up.
     */
    public static String[] removeFirst(String[] args) {
        String[] args1 = new String[args.length - 1];
        System.arraycopy(args, 1, args1, 0, args.length - 1);
        return args1;
    }

    /**
     * Check args length is reached of targetLength, in case of <code>ArrayIndexOutOfBoundsException</code> and <code>NullPointerException</code>.
     * @return <code>true</code> if passed, unless <code>false</code>.
     */
    public static boolean checkArgsLength(CommandSender sender, int targetLength, String[] args, String[] params, BaseComponent[] baseComponents) {
        if (args == null) {
            sender.spigot().sendMessage(baseComponents);
            return false;
        }

        for (int i = 0; i < targetLength; i++) {
            if (args[i].isEmpty()) {
                args = Arrays.stream(args)
                        .limit(i)
                        .toArray(String[]::new);
                return sendErrMsg(sender, args, params);
            }
        }
        if (args.length <= targetLength) {
            return sendErrMsg(sender, args, params);
        }
        return true;
    }
    private static boolean sendErrMsg(CommandSender sender, String[] args, String[] paramTypes) {
        String userArgsString = "/" + String.join(" ", args);
        int beginIndexOfSubString = Math.max(0, userArgsString.length() - 10);

        // Send Msg
        ComponentBuilder componentBuilder = new ComponentBuilder("不完整的命令参数：")
                .color(ChatColor.RED)
                .append(userArgsString.substring(beginIndexOfSubString))
                .color(ChatColor.GRAY)
                .append(ChatColor.ITALIC + "<--[此处]")
                .color(ChatColor.RED);
        // Display type of lost param
        if (!paramTypes[args.length].isEmpty()) {
            componentBuilder.append("缺少参数：" + "[" + paramTypes[args.length] + "]")
                    .color(ChatColor.RED);
        }
        sender.spigot().sendMessage(componentBuilder.create());
        return false;
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
