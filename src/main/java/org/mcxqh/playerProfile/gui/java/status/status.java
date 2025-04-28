package org.mcxqh.playerProfile.gui.java.status;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.players.Profile;

import java.util.List;

public class status implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.MENU_STATUS);
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(player, 54, "Status状态");

        // PlaceHolders
        GUIComponentLib.placeHolderFrame(inventory);

        // PlayerInfo(Menu)
        inventory.setItem(4, GUIComponentLib.playerInfo(profile));

        // StatusNow
        GUIComponentLib.addComponent(inventory, 22, Material.CLOCK,
                ChatColor.YELLOW + "现在状态: " + profile.getStatusManager().getPresentStatus().getClass().getSimpleName(),
                List.of(ChatColor.WHITE + "效果预览: " + profile.getStatusManager().getPresentStatus().getOriginalName())
        );

        // StatusList
        GUIComponentLib.addComponent(inventory, 31, Material.NAME_TAG, ChatColor.GREEN+"状态列表", List.of(ChatColor.YELLOW+"查看所有状态"));

        // Return
        inventory.setItem(45, GUIComponentLib.Return());

        // Exit
        inventory.setItem(49, GUIComponentLib.exit());

        // END
        player.openInventory(inventory);
    }



    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        switch (event.getSlot()) {
            case 4, 45 -> GUI.MENU.display(player, guiMeta); // Menu
            case 31 -> { // Status List
                guiMeta.setPageIndex(1);
                GUI.STATUS_LIST.display(player, guiMeta);
            }
            case 49 -> { // Exit
                player.closeInventory();
            }
        }
    }
}
