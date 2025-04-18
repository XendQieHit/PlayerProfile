package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.Utils;
import org.mcxqh.playerProfile.commands.CommandUtils;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.commands.titles.mainCommand;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.IdentityManager;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.identity.AuthLevel;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mcxqh.playerProfile.commands.CommandUtils.checkArgsLength;

public class award implements SubCommand {

    /**
     * @param args [player] [name] [color] [description] [issuerClass](Optional)
     */
    @Override
    public boolean run(CommandSender sender, Player operatorPlayer, String[] args) {
        // Primary check args
        String[] paramTypes = new String[]{"玩家", "称号名", "颜色", "简介"};
        BaseComponent[] usageMsg = new ComponentBuilder()
                .append("以个人身份给玩家颁发称号：/title award <玩家> <称号名> <颜色> <简介>")
                .append("以拥有的身份给玩家颁发称号：/title award <玩家> <称号名> <颜色> <简介> [身份]")
                .create();
        if (!CommandUtils.checkArgsLength(sender, 4, args, paramTypes, usageMsg)) return true;

        // TargetPlayer
        Profile targetProfile = Data.profileMapWithName.get(args[0]);
        if (targetProfile == null) {
            sender.spigot().sendMessage(new ComponentBuilder("未找到玩家").color(ChatColor.YELLOW.asBungee()).create());
            return true;
        }

        // Main
        if (operatorPlayer != null) {
            if (handlePlayer(sender, operatorPlayer, args, targetProfile)) return true;
        } else {
            handleNotPlayer(sender, args, targetProfile);
        }
        return true;
    }
    // SubMethod
    private boolean handlePlayer(CommandSender sender, Player operaterPlayer, String[] args, Profile targetProfile) {
        Profile operatorProfile = Data.profileMapWithUUID.get(operaterPlayer.getUniqueId());

        if (operatorProfile.getUniqueId() == targetProfile.getUniqueId()){
            sender.spigot().sendMessage(new ComponentBuilder("想给自己一个称号？使用/title create").color(ChatColor.RED.asBungee()).create());
            return true;
        }

        IdentityManager identityManager = operatorProfile.getIdentityManager();
        List<String> identityStringList = identityManager.getIdentitiesAsStringList();

        // Title Issue
        // If identity supplied
        if (args.length >= 5 && identityStringList.contains(args[4])) {
            int i = identityStringList.indexOf(args[4]);
            Identity identity = identityManager.getIdentities().stream().toList().get(i); // get the identity issuer chosen
            // Verify Operator's Identity
            if (identity.verify()) {
                targetProfile.getTitleManager().addTitle(new Title(args[1], Utils.valueOfChatColor(args[2]), args[3], identity));
                sender.spigot().sendMessage(new ComponentBuilder("称号颁发成功").color(ChatColor.YELLOW.asBungee()).create());
            } else {
                sender.spigot().sendMessage(new ComponentBuilder("身份验证失败").color(ChatColor.RED.asBungee()).create());
            }
            return true;
        } else { // Select Identity Automatically
            targetProfile.getTitleManager().addTitle(new Title(args[1], Utils.valueOfChatColor(args[2]), args[3], Identity.of(AuthLevel.PERSONAL, operaterPlayer.getUniqueId(), operaterPlayer.getName(), null, null)));
        }
        return false;
    }
    private void handleNotPlayer(CommandSender sender, String[] args, Profile targetProfile) {
        if (sender instanceof ConsoleCommandSender) {
            targetProfile.getTitleManager().addTitle(new Title(args[1], Utils.valueOfChatColor(args[2]), args[3], Identity.of(AuthLevel.SERVER, null, null, null, null)));
            sender.spigot().sendMessage(new ComponentBuilder("称号颁发成功").color(ChatColor.YELLOW.asBungee()).create());
        } else {
            sender.spigot().sendMessage(new ComponentBuilder("Only player or console can award new one.").color(ChatColor.RED.asBungee()).create());
        }
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        List<Identity> identities = profile.getIdentityManager().getIdentities().stream().toList();

        switch (args.length) {
            case 1 -> { // Player
                return CommandUtils.pair(args[0], Data.playerMapWithName.keySet().stream().toList());
            }
            case 3 -> { // Color
                return CommandUtils.pair(args[2], mainCommand.CHAT_COLOR_STRING_LIST);
            }
            case 5 -> { // IssuerClass
                return CommandUtils.pair(args[4], identities.stream().map(Identity::toString).toList());
            }
        }
        return List.of();
    }
}
