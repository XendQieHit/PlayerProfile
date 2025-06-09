package org.mcxqh.playerProfile.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIPanel;

import java.util.logging.Logger;

public class PlayerCloseWindow extends PacketAdapter {

    public PlayerCloseWindow() {
        super(PlayerProfile.instance, PacketType.Play.Server.CLOSE_WINDOW);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (Data.GUI_META_MAP_FOR_PLAYER.get(event.getPlayer()).getGuiPanel() == null) return;
        Player player = event.getPlayer();
        GUIPanel guiPanel = Data.GUI_META_MAP_FOR_PLAYER.get(player).getGuiPanel();
        switch (guiPanel.panelType) {
            case ANVIL -> {
                guiPanel.parentGUIPanel.instance.display(player, Data.GUI_META_MAP_FOR_PLAYER.get(player));
            }
            case WRITABLE_BOOK -> {
                guiPanel.parentGUIPanel.instance.display(player, Data.GUI_META_MAP_FOR_PLAYER.get(player));
                player.spigot().sendMessage(new ComponentBuilder("已保存").color(ChatColor.YELLOW).create());
            }
            default -> {
                Logger.getLogger("PlayerProfile").info(player.getDisplayName() + " Quited.");
                Data.GUI_META_MAP_FOR_PLAYER.get(player).setGuiPanel(null);
            }
        }
    }
}
