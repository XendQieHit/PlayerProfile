package org.mcxqh.playerProfile.gui.java.status;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.gui.java.GUIList;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.GUIPresentable;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.ArrayList;
import java.util.List;

public class statusList extends GUIList {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.STATUS_LIST);
        int pageIndex = guiMeta.getPageIndex();
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, 54, "");
        StatusManager statusManager = profile.getStatusManager();
        ArrayList<Status> statuses = statusManager.getStatuses();

        // Instances
        Material[] materials = statuses
                .stream()
                .limit(28L * pageIndex)
                .map(GUIPresentable::getGUIMaterial)
                .toArray(Material[]::new);
        String[] itemNames = statusManager.getAllSubStatusNames().toArray(String[]::new);
        List<List<String>> itemLore = statuses
                .stream()
                .limit(28L * pageIndex)
                .map(GUIPresentable::getDescription)
                .toList();
        GUIComponentLib.createListPanel(inventory, materials, itemNames, itemLore, pageIndex, null);
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        executeInList(event, player, guiMeta, GUI.STATUS, GUI.STATUS_DETAIL, (GM) -> {
            ItemStack currentItem = event.getCurrentItem();
            GM.setItemStack(currentItem);
            GM.setAddition(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getStatusManager().getStatusWithString(currentItem.getItemMeta().getItemName()));
        });
    }


}
