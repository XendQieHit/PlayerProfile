package org.mcxqh.playerProfile.commands.titles.subcommand;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcxqh.playerProfile.Data;
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

public class award implements SubCommand {

    /**
     * @param args [player] [name] [color] [description] [issuerClass](Optional)
     */
    @Override
    public boolean run(CommandSender sender, Player player, String[] args) {

        Data.profileMapWithName.get(args[0])
        if (Data.playerMapWithName.containsKey(args[0])) {
            Data.playerMapWithName.get(args[0]);
        }

        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        IdentityManager identityManager = profile.getIdentityManager();
        List<String> identityStringList = identityManager.getIdentitiesAsStringList();

        // For identity supplied
        if (args.length >= 5 && identityStringList.contains(args[4])) {
            int i = identityStringList.indexOf(args[4]);
            Identity identity = identityManager.getIdentities().stream().toList().get(i);
            if (identity.verify()) {

            }
        } else {
            // 这里还要加个身份验证程序，啊啊啊好麻烦啊啊啊——————
            if (sender instanceof Player) {


            } else {
                identityType = AuthLevel.SERVER;
            }
        }

        if (args.length >= 4) {
            Title title = new Title(args[1], ChatColor.valueOf(args[2].toUpperCase()), args[3], sender.getName(), );
        }


        return true;
    }

    @Override
    public List<String> tab(String[] args, Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        List<Identity> identities = profile.getIdentities().stream().toList();

        switch (args.length) {
            case 1 -> { // Player
                return mainCommand.pair(args[0], Data.playerMapWithName.keySet().stream().toList());
            }
            case 3 -> { // Color
                return mainCommand.pair(args[2], Arrays.stream(ChatColor.values()).map(Enum::name).toList());
            }
            case 5 -> { // IssuerClass
                return mainCommand.pair(args[4], identities.stream().map(Identity::toString).toList());
            }
        }
        return List.of();
    }
}
