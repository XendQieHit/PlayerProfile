package org.mcxqh.playerProfile.gui.java.title;

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
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.gui.java.GUIList;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.TitleManager;

import java.util.List;

public class titleList extends GUIList {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TITLE_LIST);
        Inventory inventory = Bukkit.createInventory(null, 54, "称号列表");
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());

        String[] itemNames = profile.getTitleManager().getTitleStringArrayList().toArray(String[]::new);
        List<List<String>> itemLore = profile.getTitleManager().getTitleArrayList().stream()
                .map(title -> {
                    String description = title.getDescription();
                    if (description.length() < 24) {
                        return List.of(
                                ChatColor.WHITE + description,
                                ChatColor.YELLOW + "颁发者：" + title.getIssuerIdentity().toString()
                        );
                    }
                    return List.of(
                            ChatColor.WHITE + description.substring(0, 24),
                            ChatColor.YELLOW + "颁发者：" + title.getIssuerIdentity().toString()
                    );
                })
                .toList();

        GUIComponentLib.createListPanel(inventory, Material.NAME_TAG, itemNames, itemLore, guiMeta.getPageIndex());
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        executeInList(event, player, guiMeta, GUI.TITLE, GUI.TITLE_DETAIL, (GM) -> {
            TitleManager titleManager = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getTitleManager();
            int index = titleManager.getTitleStringArrayList().indexOf(event.getCurrentItem().getItemMeta().getItemName());
            GM.setGuiPanel(GUIPanel.TITLE_DETAIL);
            GM.setAddition(titleManager.getTitleArrayList().get(index));
        });
    }
}
