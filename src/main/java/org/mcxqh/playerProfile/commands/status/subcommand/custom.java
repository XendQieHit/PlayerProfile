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

public class custom implements SubCommand {

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Profile.profileMapWithUUID.get(player.getUniqueId());
        Status status = profile.getStatus();
        return args.length == 1 ? mainCommand.pair(args[0], status.getAllSubStatusNames()) : List.of();
    }

    /**
     * @param args  [Status] [StatusName] [ChatColor] or [Status] true|false or [Status] reset
     */
    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {
        if (args.length < 1) {
            sendUsageMessage(sender);
            return true;
        }

        Profile profile = Profile.profileMapWithUUID.get(player.getUniqueId());
        Status status = profile.getStatus();

        for (SubStatus subStatus : status.getAllSubStatuses()) {
            if (!args[0].equalsIgnoreCase(subStatus.getClass().getSimpleName())) continue;

            boolean intoChatColor = false;

            for (int i = 1; i < args.length; i++) {
                switch (i) {
                    case 1 -> handleFirstArgument(sender, subStatus, args[i], intoChatColor);
                    case 2 -> handleSecondArgument(sender, subStatus, args[i]);
                }
            }

            status.saveStatus();
            subStatus.loadSetting();
            return true;
        }
        sendUsageMessage(sender);
        return true;
    }

    private void handleFirstArgument(CommandSender sender, SubStatus subStatus, String arg, boolean intoChatColor) {
        if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            boolean b = Boolean.parseBoolean(arg);
            subStatus.setDisplayCustomName(b);
            sendMessage(sender, b ? "已启用自定义状态名" : "已禁用自定义状态名", ChatColor.YELLOW);
        } else if (arg.equalsIgnoreCase("reset")) {
            subStatus.resetCustomName();
            sendMessage(sender, "已重置自定义状态名", ChatColor.YELLOW);
        } else if (arg.isEmpty()) {
            sendMessage(sender, "自定义名称不能为空", ChatColor.RED);
        } else {
            subStatus.setCustomName(arg);
            sendMessage(sender, "已设置自定义状态名", ChatColor.YELLOW);
        }
    }

    private void handleSecondArgument(CommandSender sender, SubStatus subStatus, String arg) {
        if (!arg.isEmpty()) {
            try {
                subStatus.setColor(org.bukkit.ChatColor.valueOf(arg.toUpperCase()));
                sendMessage(sender, "已设置自定义状态名颜色", ChatColor.YELLOW);
            } catch (IllegalArgumentException e) {
                sendMessage(sender, "颜色代码错误，输入你想要设置的颜色的英文单词就可以了", ChatColor.RED);
            }
        }
    }

    private void sendMessage(CommandSender sender, String message, ChatColor color) {
        sender.spigot().sendMessage(new ComponentBuilder(message).color(color).create());
    }

    private void sendUsageMessage(CommandSender sender) {
        ComponentBuilder cb = new ComponentBuilder("输入格式：\n");
        cb.append("设置状态自定义名称：/status custom <status>(状态) <customName>(自定义名称) [ChatColor](颜色)\n");
        cb.append("设置状态自定义名称显示：/status custom <status>(状态) <true|false(显示自定义状态名)>\n");
        cb.append("重置状态自定义名称：/status custom <status> reset");
        sender.spigot().sendMessage(cb.color(ChatColor.RED).create());
    }
}
