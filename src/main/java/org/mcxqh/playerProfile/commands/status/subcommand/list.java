package org.mcxqh.playerProfile.commands.status.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.ArrayList;
import java.util.List;

public class list implements SubCommand {

    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        Profile profile = Data.profileMapWithUUID.get(operatorPlayer.getUniqueId());
        StatusManager statusManager = profile.getStatusManager();

        // 加载状态
        ArrayList<Status> statuses = new ArrayList<>();
        statuses.add(statusManager.getAFK());
        statuses.add(statusManager.getIdle());

        // Player 的状态设置：
        // 状态     显示     自定义状态名
        ComponentBuilder chatTopBar = new ComponentBuilder(operatorPlayer.getName() + " 的状态设置：\n").color(ChatColor.YELLOW);
        chatTopBar.append("状态        显示      自定义状态名").color(ChatColor.YELLOW);
        sender.spigot().sendMessage(chatTopBar.create());

        // 这里就是具体状态的各个属性了
        for (Status status : statuses) {
            operatorPlayer.spigot().sendMessage(batch(status));
        }
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }

    private <T extends Status> BaseComponent[] batch(T status) {
        ComponentBuilder componentBuilder = new ComponentBuilder();

        // 状态
        componentBuilder.append(status.getName() + "    ");

        // 显示
        if (status.isDisplayCustomName()) {
            componentBuilder.append(ChatColor.YELLOW + "已启用    " + ChatColor.RESET);
        } else {
            componentBuilder.append(ChatColor.GRAY + "已禁用    " + ChatColor.RESET);
        }

        // 自定义状态名
        componentBuilder.append(status.getCustomName());
        return componentBuilder.create();
    }
}
