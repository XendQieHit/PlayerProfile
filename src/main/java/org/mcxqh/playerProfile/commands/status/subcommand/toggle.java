package org.mcxqh.playerProfile.commands.status.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.CommandUtils;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class toggle implements SubCommand {

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();

        if (args.length == 1) {
            return CommandUtils.pair(args[0], statusManager.getAllSubStatusNames());

        } else if (args.length == 2 || args.length == 3) {
            List<String> options = new ArrayList<>(2);
            options.add("true");
            options.add("false");
            return CommandUtils.pair(args[args.length - 1], options);
        }
        return List.of();
    }

    /**
     * @param args [true|false] [true|false](Optional)
     */
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        if (args.length < 1) {
            sendUsageMessage(sender);
            return true;
        }

        Profile profile = Data.profileMapWithUUID.get(operatorPlayer.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();

        for (Status status : statusManager.getStatuses()) {
            if (!args[0].equalsIgnoreCase(status.getClass().getSimpleName())) continue;

            List<Consumer<Boolean>> methods = List.of(
                    status::setDisplay,
                    status::setDisplayCustomName
            );

            for (int i = 1; i <= 2 && i < args.length; i++) { // 确保只处理前两个额外参数
                if (i != 2 || status.toString() != null) { // Ensure CustomName is not null
                    switch (args[i].toLowerCase()) {
                        case "true" -> {
                            methods.get(i - 1).accept(true);
                            sendMessage(sender, i == 1 ? "状态已启用" : "自定义状态名称已启用", ChatColor.YELLOW);
                        }
                        case "false" -> {
                            methods.get(i - 1).accept(false);
                            sendMessage(sender, i == 1 ? "状态已禁用" : "自定义状态名称已禁用", ChatColor.YELLOW);
                        }
                        default -> {
                            sendMessage(sender, i == 1 ? "状态显示设置参数错误(应为true或false)" : "自定义状态名称显示设置参数错误(应为true或false)", ChatColor.RED);
                            return true;
                        }
                    }
                } else {
                    sendMessage(sender, "自定义状态名称为空，无法启用自定义状态名称", ChatColor.RED);
                    return true;
                }
            }

            statusManager.save();
            status.load();
            return true;
        }
        sendUsageMessage(sender);
        return true;
    }

    private void sendMessage(CommandSender sender, String message, ChatColor color) {
        sender.spigot().sendMessage(new ComponentBuilder(message).color(color).create());
    }

    private void sendUsageMessage(CommandSender sender) {
        sender.spigot().sendMessage(new ComponentBuilder("输入格式: /status toggle <true|false>(显示状态) [true|false](显示自定义状态名)").color(ChatColor.RED).create());
    }

    /* 呜呼，我不想放弃费劲心思做出来的递归代码口牙qwq */
    /*private boolean matchBoolean(int i, CommandSender sender, String[] inputTextLib, List<Consumer<Boolean>> funcLib, String[] sucMsgLib, String[] errMsgLib) {
        if (i > inputTextLib.length || inputTextLib[i-1].isEmpty()) {
            return true;
        }
        switch (inputTextLib[i-1].toLowerCase()) {
            case "true" -> {
                funcLib.get(i-1).accept(true);

                if (matchBoolean(i + 1, sender, inputTextLib, funcLib, sucMsgLib, errMsgLib)) {

                    sender.spigot().sendMessage(new ComponentBuilder(sucMsgLib[i*2-2]).color(ChatColor.YELLOW).create());
                    return true;
                } else {
                    return false;
                }
            }
            case "false" -> {
                funcLib.get(i-1).accept(false);

                if (matchBoolean(i + 1, sender, inputTextLib, funcLib, sucMsgLib, errMsgLib)) {

                    sender.spigot().sendMessage(new ComponentBuilder(sucMsgLib[i*2-1]).color(ChatColor.YELLOW).create());
                    return true;
                } else {
                    return false;
                }
            }
            default -> {
                // Not be matched
                sender.spigot().sendMessage(new ComponentBuilder(errMsgLib[i-1]).color(ChatColor.RED).create());
                return false;
            }
        }
    }*/

    /*      实际应用到的场景
            if (args[0].equalsIgnoreCase(subStatus.getClass().getSimpleName())) {
                args = main.removeFirst(args);

                List<Consumer<Boolean>> funcLib = List.of(
                        subStatus::setDisplay,
                        subStatus::setDisplayCustomName
                );
                String[] sucMsg = {
                        "状态已启用",
                        "状态已禁用",
                        "自定义状态名称已启用",
                        "自定义状态名称已禁用"
                };
                String[] errMsg = {
                        "状态显示设置参数错误(应为true或false)",
                        "自定义状态名称显示设置参数错误(应为true或false)"
                };
                if (matchBoolean(1, sender, args, funcLib, sucMsg, errMsg)) {
                    status.saveStatus();
                    subStatus.loadSetting();
                    return true;
                }
            }
*/

}
