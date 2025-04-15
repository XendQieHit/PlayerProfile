package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.Utils;
import org.mcxqh.playerProfile.commands.CommandUtils;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.titles.mainCommand;
import org.mcxqh.playerProfile.files.FileHandler;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.identity.AuthLevel;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class create implements SubCommand {

    /**
     * @param args [Name] [Color] [Description]
     */
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        // Primary check args
        String[] paramNames = new String[]{"称号名", "颜色", "简介"};
        ComponentBuilder componentBuilder = new ComponentBuilder("给自己创建称号：/title create <称号名> <颜色> <简介>");
        if (!CommandUtils.checkArgsAbsence(sender, args, paramNames, componentBuilder.create())) return true;

        FileHandler fileHandler = new FileHandler();
        if (sender instanceof Player) {
            handleWithPlayer(sender, operatorPlayer, args);
        } else {
            sender.spigot().sendMessage(new ComponentBuilder("This command is only for entity of player.").color(ChatColor.YELLOW).create());
        }
        return true;
    }
    private void handleWithPlayer(CommandSender sender, Player operatorPlayer, String[] args) {
        Profile profile = Data.profileMapWithUUID.get(operatorPlayer.getUniqueId());

        // Detect existed personal title issued by self.
        if (hasPersonalTitle(profile)) {
            sender.spigot().sendMessage(new ComponentBuilder("有一个就够了，快端下去罢（").color(ChatColor.YELLOW).create());
        } else {
            // Create Personal Title
            profile.getTitleManager().addTitle(new Title(args[0], Utils.valueOfChatColor(args[1]), args[2], Identity.of(AuthLevel.PERSONAL, profile.getUniqueId(), profile.getName(), null, null)));
        }
    }
    private boolean hasPersonalTitle(Profile profile) {
        ArrayList<Title> titleArrayList = profile.getTitleManager().getTitleArrayList();
        UUID uuid = profile.getUniqueId();
        for (Title title : titleArrayList) {
            if (title.getIssuerIdentity().getAuthLevel() == AuthLevel.PERSONAL && title.getIssuerIdentity().getUniqueId() == uuid) return true;
        }
        return false;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        if (args.length == 2) {
            return CommandUtils.pair(args[1], mainCommand.CHAT_COLOR_STRING_LIST);
        }
        return List.of();
    }
}
