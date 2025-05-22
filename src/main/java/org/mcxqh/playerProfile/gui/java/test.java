package org.mcxqh.playerProfile.gui.java;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.mcxqh.playerProfile.gui.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test extends GUIList {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TEST);
        Inventory inventory = Bukkit.createInventory(null, 54, "test");
        Material[] materials = new Material[100];
        Arrays.fill(materials, Material.LIGHT);
        String[] itemNames = new String[100];
        Arrays.fill(itemNames, "aaa");
        List<List<String>> itemLore = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            itemLore.add(List.of("bbb"));
        }
        GUIComponentLib.createListPanel(inventory, materials, itemNames, itemLore, guiMeta.getPageIndex());
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        executeInList(event, player, guiMeta, GUI.STATUS, GUI.STATUS, (GM) -> {});
    }
}
