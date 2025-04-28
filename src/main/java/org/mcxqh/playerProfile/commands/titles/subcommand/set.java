package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.CommandUtils;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.players.profile.TitleManager;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.ArrayList;
import java.util.List;

public class set implements SubCommand {

    /**
     * @param args [title]
     */
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        // Primary args checking
        ComponentBuilder componentBuilder = new ComponentBuilder("展示称号：/title set <称号>");
        if (!CommandUtils.checkArgsAbsence(sender, args, new String[]{"称号"}, componentBuilder.create())) return true;

        TitleManager titleManager = Data.PROFILE_MAP_WITH_UUID.get(operatorPlayer.getUniqueId()).getTitleManager();
        ArrayList<Title> titleArrayList = titleManager.getTitleArrayList();
        ArrayList<String> titleStringArrayList = titleManager.getTitleStringArrayList();

        int i = titleStringArrayList.indexOf(args[0]);
        if (i != -1) {
            titleManager.setPresentTitle(titleArrayList.get(i));
        } else {
            sender.spigot().sendMessage(new ComponentBuilder("找不到称号").color(ChatColor.YELLOW.asBungee()).create());
        }
        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return CommandUtils.pair(args[0], Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getIdentityManager().getIdentitiesAsStringList());
    }
}
