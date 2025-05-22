package org.mcxqh.playerProfile.gui.java.title;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.players.profile.TitleManager;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.List;

public class titleDetail implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TITLE_DETAIL);
        Title title = (Title) guiMeta.getAddition();
        Inventory inventory = Bukkit.createInventory(null, 54, title.getName());

        // PlaceHolders
        GUIComponentLib.placeHolderFrame(inventory);

        // Return
        inventory.setItem(49, GUIComponentLib.Return());

        // TitleDisplayer
        inventory.setItem(4, GUIComponentLib.createItemStackWithoutLore(Material.NAME_TAG, title.toString()));

        // Description
        inventory.setItem(21, GUIComponentLib.createItemStack(Material.PAPER, ChatColor.YELLOW + "简介", List.of(title.getDescription())));

        // Toggle
        Title presentTitle = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getTitleManager().getPresentTitle();
        if (presentTitle != null && presentTitle.equals(title)) {
            inventory.setItem(22, GUIComponentLib.createItemStackWithoutLore(Material.RED_CONCRETE, ChatColor.RED + "隐藏该称号"));
        } else {
            inventory.setItem(22, GUIComponentLib.createItemStackWithoutLore(Material.GREEN_CONCRETE, ChatColor.GREEN + "展示该称号"));
        }

        // Issuer
        Identity issuerIdentity = title.getIssuerIdentity();
        PlayerProfile playerProfile = Bukkit.getServer().createPlayerProfile(issuerIdentity.getUniqueId());
        ItemStack issuerInfo = GUIComponentLib.createItemStackWithoutLore(Material.PLAYER_HEAD, issuerIdentity.toString());
        SkullMeta skullMeta = (SkullMeta) issuerInfo.getItemMeta();
        skullMeta.setOwnerProfile(playerProfile);
        issuerInfo.setItemMeta(skullMeta);
        inventory.setItem(23, issuerInfo);

        // End
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        switch (event.getSlot()) {
            case 22 -> { // toggle
                TitleManager titleManager = Data.PROFILE_MAP_WITH_UUID.get(player.getUniqueId()).getTitleManager();
                if (event.getCurrentItem().getType() == Material.GREEN_CONCRETE) {
                    titleManager.setPresentTitle((Title) guiMeta.getAddition());
                    player.spigot().sendMessage(new ComponentBuilder("已展示该称号").color(net.md_5.bungee.api.ChatColor.YELLOW).create());
                } else {
                    titleManager.setPresentTitle(null);
                    player.spigot().sendMessage(new ComponentBuilder("已隐藏该称号").color(net.md_5.bungee.api.ChatColor.YELLOW).create());
                }
                GUI.TITLE_DETAIL.display(player, guiMeta);
            }
            case 49 -> { // Return
                GUI.TITLE_LIST.display(player, guiMeta);
            }
        }
    }
}