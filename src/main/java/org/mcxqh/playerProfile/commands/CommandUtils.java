package org.mcxqh.playerProfile.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    /**
     * Check if args conforms to the type format without absence param.
     *
     * @param paramNamesBiList List of params, each params match different command params standards.
     * @param args Command arguments that inputted by sender.
     * @param usageMsg If args is <code>null</code>, this message will be sent to sender.
     * @param paramClassesBiList An array of Class Object, which defined the type format of args.
     *
     * @return <code>true</code> if passed, otherwise <code>false</code>.
     */
    public static boolean checkArgs(CommandSender sender, String[] args, List<String[]> paramNamesBiList, List<Class<?>[]> paramClassesBiList, BaseComponent[] usageMsg) {
        if (args == null) {
            sender.spigot().sendMessage(usageMsg);
            return false;
        }
        Iterator<String[]> paramNamesIterator = paramNamesBiList.iterator();
        Iterator<Class<?>[]> paramTypesIterator = paramClassesBiList.iterator();
        for (int i = 0; i < args.length && paramNamesBiList.size() > 1; i++) {
            while (paramNamesIterator.hasNext() && paramTypesIterator.hasNext()) {
                String[] params = paramNamesIterator.next();
                if (!params[i].equals(args[i])) {
                    paramNamesIterator.remove();
                    paramTypesIterator.remove();
                }
            }
        }
        if (checkArgsAbsence(sender, args, paramNamesBiList.getFirst(), usageMsg)) {
            return checkArgsWrong(sender, args, paramClassesBiList.getFirst());
        }
        return false;
    }

    /**
     * Check if args without absence, in case of <code>ArrayIndexOutOfBoundsException</code> and <code>NullPointerException</code>.
     * This method is to handle args and point wrong place of user's input smartly with different command params standards.
     *
     * @param paramNamesBiList List of params, each params match different command params standards.
     * @param args Command arguments that inputted by sender.
     * @param usageMsg If args is <code>null</code>, this message will be sent to sender.
     *
     * @return <code>true</code> if passed, otherwise <code>false</code>.
     */
    public static boolean checkArgsAbsence(CommandSender sender, String[] args, List<String[]> paramNamesBiList, BaseComponent[] usageMsg) {
        if (args == null) {
            sender.spigot().sendMessage(usageMsg);
            return false;
        }
        Iterator<String[]> iterator = paramNamesBiList.iterator();
        for (int i = 0; i < args.length && paramNamesBiList.size() > 1; i++) {
            while (iterator.hasNext()) {
                String[] params = iterator.next();
                if (!params[i].equals(args[i])) iterator.remove();
            }
        }
        return checkArgsAbsence(sender, args, paramNamesBiList.getFirst(), usageMsg);
    }
    /**
     * Check if args without absence, in case of <code>ArrayIndexOutOfBoundsException</code> and <code>NullPointerException</code>.
     *
     * @param paramNames Standard of this command params.
     * @param args Command arguments that inputted by sender.
     * @param usageMsg If args is <code>null</code>, this message will be sent to sender.
     *
     * @return <code>true</code> if passed, otherwise <code>false</code>.
     */
    public static boolean checkArgsAbsence(CommandSender sender, String[] args, String[] paramNames, BaseComponent[] usageMsg) {
        if (args == null) {
            sender.spigot().sendMessage(usageMsg);
            return false;
        }

        for (int i = 0; i < paramNames.length; i++) {
            if (args[i].isEmpty()) {
                args = Arrays.stream(args)
                        .limit(i)
                        .toArray(String[]::new);
                return sendEmptyErrMsg(sender, args, paramNames);
            }
        }
        if (args.length <= paramNames.length) {
            return sendEmptyErrMsg(sender, args, paramNames);
        }
        return true;
    }
    private static boolean sendEmptyErrMsg(CommandSender sender, String[] args, String[] paramNames) {
        String userArgsString = "/" + String.join(" ", args);
        int beginIndexOfSubString = Math.max(0, userArgsString.length() - 10);

        // Send Msg
        ComponentBuilder componentBuilder = new ComponentBuilder("不完整的命令参数：")
                .color(ChatColor.RED)
                .append("\n..." + userArgsString.substring(beginIndexOfSubString))
                .color(ChatColor.GRAY)
                .append(ChatColor.ITALIC + "<--[此处]")
                .color(ChatColor.RED);
        // Display type of lost param
        if (!paramNames[args.length].isEmpty()) {
            componentBuilder.append("\n缺少参数：" + "[" + paramNames[args.length] + "]")
                    .color(ChatColor.RED);
        }
        sender.spigot().sendMessage(componentBuilder.create());
        return false;
    }

    /**
     * Check if args conforms to the type format.
     * Before using this method, please ensure that args has no empty.
     * In other words, check args with <code>checkArgsAbsence</code> before using this.
     *
     * @param paramsClasses An array of Class Object, which defined the type format of args.
     *
     * @return <code>true</code> if passed, otherwise <code>false</code>.
     */
    public static boolean checkArgsWrong(CommandSender sender, String[] args, Class<?>[] paramsClasses) {
        for (int i = 0; i < paramsClasses.length; i++) {
            // Enum
            if (paramsClasses[i].isEnum()) {
                String enumValues = null;
                try {
                    enumValues = paramsClasses[i].getMethod("values").invoke(null).toString();
                    Enum<?> anEnum = Enum.valueOf((Class<Enum>) paramsClasses[i], args[i]);
                } catch (IllegalAccessException e) {
                    return sendTypeErrMsg(sender, args, paramsClasses, i, enumValues);
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                // Basic Variable
            } else {
                try {
                    paramsClasses[i].cast(args[i]);
                } catch (ClassCastException e) {
                    return sendTypeErrMsg(sender, args, paramsClasses, i, null);
                }
            }
        }
        return true;
    }
    private static boolean sendTypeErrMsg(CommandSender sender, String[] args, Class<?>[] paramsClasses, int i, String example) {
        System.arraycopy(args, 0, args, 0, i + 1);
        String userArgsString = "/" + String.join(" ", args);
        int beginIndexOfSubString = Math.max(0, userArgsString.length() - 10);
        // Send Msg
        ComponentBuilder componentBuilder = new ComponentBuilder("输入的参数有误：")
                .color(ChatColor.RED)
                .append("\n..." + userArgsString.substring(beginIndexOfSubString))
                .color(ChatColor.GRAY)
                .append(ChatColor.ITALIC + "<--[此处]")
                .color(ChatColor.RED)
                .append("\n参数类型应为：\n" + paramsClasses[i].getSimpleName())
                .color(ChatColor.RED);
        // Add examples tip
        if (example != null) componentBuilder.append(example);
        sender.spigot().sendMessage(componentBuilder.create());
        return false;
    }
}
