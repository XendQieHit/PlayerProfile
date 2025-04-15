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

import java.util.List;

public class custom implements SubCommand {

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();
        return args.length == 1 ? CommandUtils.pair(args[0], statusManager.getAllSubStatusNames()) : List.of();
    }

    /**
     * @param args  [Status] [StatusName] [ChatColor] or [Status] true|false or [Status] reset
     */
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        List<String[]> paramNamesBiList = List.of(
                new String[]{"Status", "StatusName", "ChatColor"},
                new String[]{"Status", "true|false"},
                new String[]{"Status", "reset"}
        );
        List<Class<?>[]> paramClassesBiList = List.of(
                new Class<?>[]{String.class, String.class, org.bukkit.ChatColor.class},
                new Class<?>[]{String.class, Boolean.class},
                new Class<?>[]{String.class, String.class}
        );
        ComponentBuilder cb = new ComponentBuilder("输入格式：\n")
                .append("设置状态自定义名称：/status custom <status>(状态) <customName>(自定义名称) [ChatColor](颜色)\n")
                .append("设置状态自定义名称显示：/status custom <status>(状态) <true|false(显示自定义状态名)>\n")
                .append("重置状态自定义名称：/status custom <status> reset");
        if (!CommandUtils.checkArgs(sender, args, paramNamesBiList, paramClassesBiList, cb.create())) return true;


        Profile profile = Data.profileMapWithUUID.get(operatorPlayer.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();

        for (Status status : statusManager.getStatuses()) {
            if (!args[0].equalsIgnoreCase(status.getClass().getSimpleName())) continue;

            boolean intoChatColor = false;

            for (int i = 1; i < args.length; i++) {
                switch (i) {
                    case 1 -> handleFirstArgument(sender, status, args[i], intoChatColor);
                    case 2 -> handleSecondArgument(sender, status, args[i]);
                }
            }

            statusManager.save();
            status.load();
            return true;
        }
        return true;
    }

    private void handleFirstArgument(CommandSender sender, Status status, String arg, boolean intoChatColor) {
        if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            boolean b = Boolean.parseBoolean(arg);
            status.setDisplayCustomName(b);
            sendMessage(sender, b ? "已启用自定义状态名" : "已禁用自定义状态名", ChatColor.YELLOW);
        } else if (arg.equalsIgnoreCase("reset")) {
            status.resetCustomName();
            sendMessage(sender, "已重置自定义状态名", ChatColor.YELLOW);
        } else if (arg.isEmpty()) {
            sendMessage(sender, "自定义名称不能为空", ChatColor.RED);
        } else {
            status.setCustomName(arg);
            sendMessage(sender, "已设置自定义状态名", ChatColor.YELLOW);
        }
    }

    private void handleSecondArgument(CommandSender sender, Status status, String arg) {
        if (!arg.isEmpty()) {
            try {
                status.setColor(org.bukkit.ChatColor.valueOf(arg.toUpperCase()));
                sendMessage(sender, "已设置自定义状态名颜色", ChatColor.YELLOW);
            } catch (IllegalArgumentException e) {
                sendMessage(sender, "颜色代码错误，输入你想要设置的颜色的英文单词就可以了", ChatColor.RED);
            }
        }
    }

    private void sendMessage(CommandSender sender, String message, ChatColor color) {
        sender.spigot().sendMessage(new ComponentBuilder(message).color(color).create());
    }
}
