package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.commands.SubCommand;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.title.IssuerClass;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.List;

public class award implements SubCommand {

    /**
     * @param args [player] [name] [color] [description] [issuerClass](Optional)
     */
    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {

        IssuerClass issuerClass;
        if (args.length >= 5) {
            try {
                issuerClass = IssuerClass.valueOf(args[5].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.spigot().sendMessage(new ComponentBuilder("颁发身份参数错误，应为：").color(ChatColor.RED.asBungee()).create());
                return true;
            }
        } else {
            // 这里还要加个身份验证程序，啊啊啊好麻烦啊啊啊——————
            if (sender instanceof Player) {
                Profile profile = Data.profileMapWithUUID.get(((Player) sender).getUniqueId());

            } else {
                issuerClass = IssuerClass.SERVER;
            }
        }

        if (args.length >= 4) {
            Title title = new Title(args[1], ChatColor.valueOf(args[2].toUpperCase()), args[3], sender.getName(), );
        }


        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        return List.of();
    }
}
