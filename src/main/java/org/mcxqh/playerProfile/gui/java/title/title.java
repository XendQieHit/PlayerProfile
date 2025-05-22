package org.mcxqh.playerProfile.gui.java.title;

import net.md_5.bungee.api.chat.ComponentBuilder;
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
import org.mcxqh.playerProfile.gui.java.GUIList;
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.TitleManager;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.ArrayList;
import java.util.List;

public class title implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.MENU_TITLE);
        Profile profile = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId());
        TitleManager titleManager = profile.getTitleManager();
        guiMeta.setAddition(titleManager);
        Inventory inventory = Bukkit.createInventory(null, 54, "Title称号");

        // PlaceHolder
        GUIComponentLib.placeHolderFrame(inventory);

        // PlayerInfo
        inventory.setItem(4, GUIComponentLib.playerInfo(profile));

        // TitleNowDisplaying
        if (titleManager.getPresentTitle() != null) {
            inventory.setItem(22, GUIComponentLib.enchantEffect(GUIComponentLib.createItemStack(Material.NAME_TAG, ChatColor.YELLOW + "目前正在展示的称号", List.of(titleManager.getPresentTitle().toString()))));
        } else {
            inventory.setItem(22, GUIComponentLib.createItemStack(Material.BARRIER, ChatColor.YELLOW + "目前正在展示的称号", List.of(ChatColor.RED + "无")));
        }

        // AwardTitle
        inventory.setItem(30, GUIComponentLib.createItemStackWithoutLore(Material.WRITABLE_BOOK, ChatColor.YELLOW + "颁发称号"));

        // TitleList
        inventory.setItem(31, GUIComponentLib.createItemStackWithoutLore(Material.NAME_TAG, ChatColor.YELLOW + "称号列表"));

        // HideTitle
        inventory.setItem(32, GUIComponentLib.createItemStackWithoutLore(Material.BARRIER, ChatColor.RED + "隐藏称号"));

        // Return
        inventory.setItem(49, GUIComponentLib.Return());

        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        switch (event.getSlot()) {
            case 4 -> { // PlayerInfo(Menu)
                GUI.MENU.display(player, guiMeta);
            }
            case 30 -> { // Award
                ArrayList<Object> arrayList = new ArrayList<>();
                for (int i = 0; i < 5; i++) arrayList.add("");
                guiMeta.setAddition(arrayList);
                GUI.TITLE_AWARD.display(player, guiMeta);
            }
            case 31 -> { // List
                guiMeta.setPageIndex(1);
                GUI.TITLE_LIST.display(player, guiMeta);
            }
            case 32 -> { // Hide
                ((TitleManager) guiMeta.getAddition()).setPresentTitle(null);
                player.spigot().sendMessage(new ComponentBuilder("隐藏成功").color(ChatColor.YELLOW.asBungee()).create());
                GUI.TITLE.display(player, guiMeta);
            }
            case 49 -> {
                GUI.MENU.display(player, guiMeta);
            }
        }
    }
}
