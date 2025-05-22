package org.mcxqh.playerProfile.gui.java.status;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.bukkit.util.Vector;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.gui.*;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.ArrayList;
import java.util.logging.Logger;

public class statusCustomName implements GUITemplate {

    @Override
    public void display(Player player, GUIMeta guiMeta) {
        Logger.getLogger("PlayerProfile").info("test AnvilView...");
        guiMeta.setGuiPanel(GUIPanel.STATUS_CUSTOM_NAME);


    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        if (event.getSlot() == 2) {

            if (event.getView().getType() == InventoryType.ANVIL) {
                Logger.getLogger("PlayerProfile").info("is anvil");
                if (event.getView() instanceof AnvilView view) {
                    Logger.getLogger("PlayerProfile").info("is anvilView");
                    String renameText = view.getRenameText();
                    ((Status) guiMeta.getAddition()).setCustomName(renameText);
                    GUI.STATUS_DETAIL.display(player, guiMeta);
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.YELLOW + "设置成功!").create());
                }
            }
        }
    }
}
