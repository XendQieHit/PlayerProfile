package org.mcxqh.playerProfile.gui.java;

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
import org.mcxqh.playerProfile.players.Profile;

import java.util.List;

public class menu implements GUITemplate {

    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.MENU);
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(player, 54, "PlayerProfile玩家档案");

        // Placeholder
        GUIComponentLib.placeHolderFrame(inventory);

        // PlayerInfo
        inventory.setItem(4, GUIComponentLib.playerInfo(profile));

        // Batch
        Material[] materials = {
                Material.REDSTONE_TORCH, // Status
                Material.BOOK, // ProfileManagement
                Material.NAME_TAG, // Title
                Material.GOLDEN_HORSE_ARMOR, // Guild
                Material.BLUE_BANNER, // Team
                Material.IRON_PICKAXE // Group
        };
        String[] itemNames = {
                ChatColor.GREEN+"状态",
                ChatColor.GREEN+"档案",
                ChatColor.GREEN+"称号",
                ChatColor.GREEN+"公会",
                ChatColor.GREEN+"队伍",
                ChatColor.GREEN+"团队"
        };
        List<List<String>> lore = List.of(
                List.of(ChatColor.YELLOW+"管理状态", ChatColor.YELLOW+"自定义状态"),
                List.of(ChatColor.YELLOW+"管理", ChatColor.YELLOW+"迁移玩家档案"),
                List.of(ChatColor.YELLOW+"管理称号", ChatColor.YELLOW+"颁发称号"),
                List.of(ChatColor.YELLOW+"查看公会", ChatColor.YELLOW+"管理公会"),
                List.of(ChatColor.YELLOW+"管理队伍"),
                List.of(ChatColor.YELLOW+"管理团队")
        );
        int[] indexes = {21, 22, 23, 30, 31, 32};
        for (int i = 0; i < materials.length; i++)
            GUIComponentLib.addComponent(inventory, indexes[i], materials[i], itemNames[i], lore.get(i));

        // Exit
        inventory.setItem(49, GUIComponentLib.exit());

        // End
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        switch (event.getSlot()) {
            case 21 -> GUI.STATUS.display(player, guiMeta); // Status
            case 22 -> { // ProfileManagement

            }
            case 23 -> { // Title

            }
            case 30 -> { // Guild

            }
            case 31 -> { // Team

            }
            case 32 -> { // Group

            }
            case 49 ->  // exit
                player.closeInventory();
        }
        event.setCancelled(true);
    }
}
