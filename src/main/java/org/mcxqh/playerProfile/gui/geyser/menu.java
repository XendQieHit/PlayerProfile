package org.mcxqh.playerProfile.gui.geyser;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.players.Profile;

public class menu implements GUITemplate {

    @Override
    public void display(Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        SimpleForm.Builder builder = SimpleForm.builder()
                .title("PlayerProfile玩家档案")
                .content("你的名字：" + profile.getNameAsString())
                .content("你的状态：" + profile.getStatusManager().getPresentStatus().toString()); // Status
        profile.getIdentityManager().getIdentities().forEach(identity -> { // Guild, Team
            switch (identity.getIdentityType()) {
                case GUILD -> builder.content("所处公会：" + identity.getCollective().getName());
                case TEAM -> builder.content("所处队伍：" + identity.getCollective().getName());
            }
        });

        builder.button("状态", FormImage.Type.PATH, "resource/gui/status/redstone_torch.png")
                .button("称号", FormImage.Type.PATH, "resource/gui/title/name_tag.png")
                .button("队伍", FormImage.Type.PATH, "resource/gui/team/flag_aqua.png")
                .button("公会", FormImage.Type.PATH, "resource/gui/guild/loom_banner_dark_green.png")
                .button("团队", FormImage.Type.PATH, "resource/gui/group/icon_multiplayer.png")
                .button("档案管理", FormImage.Type.PATH, "resources/gui/profile/storageIconColor.png");
    }
}
