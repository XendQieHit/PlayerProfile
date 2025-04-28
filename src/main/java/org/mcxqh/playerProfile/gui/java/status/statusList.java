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
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.StatusManager;
import org.mcxqh.playerProfile.players.profile.status.GUIPresentable;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class statusList implements GUITemplate{
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.STATUS_LIST);
        int pageIndex = guiMeta.getPageIndex();
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, 54, "");
        StatusManager statusManager = profile.getStatusManager();
        ArrayList<Status> statuses = statusManager.getStatuses();

        // PlaceHolder
        GUIComponentLib.placeHolderFrame(inventory);

        // StatusInstance
        addStatusItemStacks(pageIndex, statuses, statusManager, inventory);

        // Next
        if (statuses.size() > 28) inventory.setItem(53, GUIComponentLib.next());
        // Back
        if (pageIndex != 0) inventory.setItem(45, GUIComponentLib.back());

        // Return
        inventory.setItem(49, GUIComponentLib.Return());
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        int slot = event.getSlot();
        final int pageIndex = guiMeta.getPageIndex();
        final ItemStack currentItem = event.getCurrentItem();
        switch (slot) {
            case 49 -> GUI.STATUS.display(player, guiMeta); // Return
            case 45 -> { // Back
                if (currentItem == GUIComponentLib.back()) {
                    guiMeta.setPageIndex(pageIndex - 1);
                    GUI.STATUS_LIST.display(player, guiMeta);
                }
            }
            case 53 -> { // Next
                if (currentItem == GUIComponentLib.next()) {
                    guiMeta.setPageIndex(pageIndex + 1);
                    GUI.STATUS_LIST.display(player, guiMeta);
                }
            }
            default -> { // StatusInstance
                boolean isInner = slot < 45 && slot > 9 && slot % 9 > 0 && slot % 9 < 8;
                if (isInner && isNotAir(pageIndex, slot, player)) {
                    guiMeta.setItemStack(currentItem);
                    guiMeta.setAddition(Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getStatusManager().getStatusWithString(currentItem.getItemMeta().getItemName()));
                    GUI.STATUS_DETAIL.display(player, guiMeta);
                }
            }
        }
    }

    private boolean isNotAir(int pageIndex, int slot, Player player) {
        Logger.getLogger("PlayerProfile").info(slot+"");
        return (pageIndex - 1) * 28 - slot - (slot - 10) / 7 * 2 < Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getStatusManager().getStatuses().size();
    }

    private void addStatusItemStacks(int pageIndex, ArrayList<Status> statuses, StatusManager statusManager, Inventory inventory) {
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
        for (int j = 10, i = 28 * (pageIndex - 1);i < materials.length && j < 44; j++, i++) {
            GUIComponentLib.addComponent(inventory, j, materials[i], itemNames[i], itemLore.get(i));
        }
    }
}
