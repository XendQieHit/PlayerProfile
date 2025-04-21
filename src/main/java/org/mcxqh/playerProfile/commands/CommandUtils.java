package org.mcxqh.playerProfile.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.mcxqh.playerProfile.Constants;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;

public class CommandUtils {

    private static final Map<Class<?>, Predicate<String>> VALIDATORS = new HashMap<>();

    // Initialize the cast of method to parse of basic type.
    static {
        VALIDATORS.put(int.class, str -> {
            try {
                Integer.parseInt(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        VALIDATORS.put(double.class, str -> {
            try {
                Double.parseDouble(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        VALIDATORS.put(boolean.class, str -> "true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str));
        VALIDATORS.put(char.class, str -> str.length() == 1);
        VALIDATORS.put(byte.class, str -> {
            try {
                Byte.parseByte(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        VALIDATORS.put(short.class, str -> {
            try {
                Short.parseShort(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    /**
     * Test if this str can be parsed into basic type specified.
     */
    public static boolean canConvertTo(String str, Class<?> type) {
        Predicate<String> validator = VALIDATORS.get(type);
        if (validator == null) {
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
        return validator.test(str);
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

    /**
     * Check if args conforms to the type format without absence param.
     * Each params in <code>paramNamesBiList</code> correspond to only one params in <code>paramClassesBiList</code> according to same index.
     * The further ahead in paramsBiList, the higher priority of this format to be matched.
     * Please ensure that length of <code>paramNamesBiList</code> is equal to length of <code>paramClassesBiList</code>.
     *
     * @param paramNamesBiList List of params, each params match different command params standards.
     * @param paramClassesBiList An array of Class Object, which defined the type format of args.
     * @param args Command arguments that inputted by sender.
     * @param usageMsg If args is <code>null</code>, this message will be sent to sender.
     *
     * @return <code>true</code> if passed, otherwise <code>false</code>.
     */
    public static boolean checkArgs(CommandSender sender, String[] args, String[][] paramNamesBiList, Class<?>[][] paramClassesBiList, BaseComponent[] usageMsg) {
        if (args == null || args.length < 1) {
            sender.spigot().sendMessage(usageMsg);
            return false;
        }

        int[] paramsMatchPoints = new int[paramNamesBiList.length]; // Stats matched points of params with args.
        String[] defaultParamNames = paramNamesBiList[0];
        Class<?>[] defaultParamClasses = paramClassesBiList[0];
        // Match
        for (int i = 0; i < args.length && paramNamesBiList.length > 1; i++) {
            for (int j = 0; j < paramNamesBiList.length; j++) {
                if (paramNamesBiList[j] != null) {
                    if (paramNamesBiList[j].length <= i) {
                        paramNamesBiList[j] = null;
                        paramsMatchPoints[j] = -1;
                        continue;
                    }
                    if (paramNamesBiList[j][i].equals(args[i])) paramsMatchPoints[j]++;
                }
            }
        }

        // Filter
        int index = -1, point = 0;
        for (int i = 0; i < paramNamesBiList.length; i++) {
            if (paramNamesBiList[i] != null) {
                index = i;
                break;
            }
        }

        for (int j = 0; j < paramsMatchPoints.length; j++) {
            int paramsMatchPoint = paramsMatchPoints[j];
            if (paramsMatchPoint > point) {
                point = paramsMatchPoint;
                index = j;
            }
        }

        // Check
        if (index == -1 && checkArgsAbsence(sender, args, defaultParamNames, usageMsg))
            return checkArgsWrong(sender, args, defaultParamClasses);
        if (checkArgsAbsence(sender, args, paramNamesBiList[index], usageMsg))
            return checkArgsWrong(sender, args, paramClassesBiList[index]);
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
        if (args == null || args.length < 1) {
            sender.spigot().sendMessage(usageMsg);
            return false;
        }
        if (args.length < paramNames.length) {
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
                .append(ChatColor.ITALIC + " _ <--[此处]")
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
                Enum<?>[] enumValues;
                String[] enumValuesStringList;

                // Get string name of enums.
                try {
                    enumValues = ((Enum<?>[]) paramsClasses[i].getMethod("values").invoke(null));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                enumValuesStringList = new String[enumValues.length];
                for (int j = 0; j < enumValues.length; j++) {
                    enumValuesStringList[j] = enumValues[j].name();
                }

                // Match
                for (String enumValueString : enumValuesStringList) {
                    if (enumValueString.equals(args[i])) return true;
                }
                if (paramsClasses[i] == org.bukkit.ChatColor.class) {
                    return sendTypeErrMsg(sender, args, paramsClasses, i, Constants.CHAT_COLOR_STRING_ARRAY);
                }
                return sendTypeErrMsg(sender, args, paramsClasses, i, enumValuesStringList);

            } else if (paramsClasses[i] == String.class) {
                continue;

            } else { // Basic Variable
                if (!canConvertTo(args[i], paramsClasses[i])) sendTypeErrMsg(sender, args, paramsClasses, i, null);
            }
        }
        return true;
    }
    private static boolean sendTypeErrMsg(CommandSender sender, String[] args, Class<?>[] paramsClasses, int i, String[] example) {
        String[] argsDisplayed = Arrays.copyOf(args, i + 1);
        String userArgsString = "/" + String.join(" ", argsDisplayed);
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
        if (example != null) componentBuilder.append("\n" + Arrays.toString(example) + " 的其中一个参数");
        sender.spigot().sendMessage(componentBuilder.create());
        return false;
    }
}
