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
import org.mcxqh.playerProfile.players.Profile;
import org.mcxqh.playerProfile.players.profile.identity.Identity;
import org.mcxqh.playerProfile.players.profile.title.Title;

import java.util.ArrayList;
import java.util.List;

public class titleAward implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TITLE_AWARD);
        Inventory inventory = Bukkit.createInventory(null, 54, "颁发称号");

        // Load Draft
        if (guiMeta.getAddition().getClass() != ArrayList.class) {
            guiMeta.setAddition(new ArrayList<>());
        }
        ArrayList<?> draft = (ArrayList<?>) guiMeta.getAddition();
        /*
           0 -> Identity,
           1 -> Name,
           2 -> Description
           3 -> TargetPlayerProfile
        */
        String titleName = draft.get(1).toString();

        // PlaceHolders
        GUIComponentLib.placeHolderFrame(inventory);

        // Return
        inventory.setItem(45, GUIComponentLib.Return());

        // Preview
        if (draft.isEmpty()) {
            inventory.setItem(4, GUIComponentLib.createItemStack(
                    Material.NAME_TAG,
                    ChatColor.YELLOW + "称号效果预览",
                    List.of(ChatColor.WHITE + "称号效果预览会在这里展示")
            ));
        } else {
            inventory.setItem(4, GUIComponentLib.createItemStack(
                    Material.NAME_TAG,
                    titleName,
                    List.of((String) draft.get(2), ChatColor.YELLOW + "颁发者：" + draft.get(0))
            ));
        }

        // Identity
        ItemStack identityItem = GUIComponentLib.createItemStack(Material.TOTEM_OF_UNDYING, ChatColor.YELLOW + "选择身份", List.of(ChatColor.WHITE + "点击选择身份"));
        if (draft.get(0) instanceof Identity identity) {
            ItemMeta identityMeta = identityItem.getItemMeta();
            identityMeta.setLore(List.of(identity.toString()));
            identityItem.setItemMeta(identityMeta);
        }
        inventory.setItem(21, identityItem);

        // TitleName
        if (titleName != "") {
            inventory.setItem(22, GUIComponentLib.createItemStack(Material.NAME_TAG, ChatColor.YELLOW + "称号名称", List.of(ChatColor.WHITE + titleName)));
        } else {
            inventory.setItem(22, GUIComponentLib.createItemStack(Material.NAME_TAG, ChatColor.YELLOW + "称号名称", List.of(ChatColor.YELLOW + "点击输入称号名称")));
        }

        // TargetPlayer
        ItemStack targetPlayer = GUIComponentLib.createItemStack(Material.PLAYER_HEAD, ChatColor.YELLOW + "选择被颁发玩家", List.of(ChatColor.WHITE + "点击选择目标玩家"));
        if (draft.get(3) instanceof PlayerProfile profile) {
            SkullMeta targetPlayerItemMeta = (SkullMeta) targetPlayer.getItemMeta();
            targetPlayerItemMeta.setOwnerProfile(profile);
            targetPlayerItemMeta.setLore(List.of(ChatColor.WHITE + profile.getName()));
            targetPlayer.setItemMeta(targetPlayerItemMeta);
        }
        inventory.setItem(23, targetPlayer);

        // Description -> This will open a writing book as input GUI
        if (draft.get(2) instanceof List list) {
            inventory.setItem(31, GUIComponentLib.createItemStack(Material.WRITABLE_BOOK, ChatColor.YELLOW + "称号简介", list));
        } else {
            inventory.setItem(31, GUIComponentLib.createItemStack(Material.WRITABLE_BOOK, ChatColor.YELLOW + "称号简介", List.of(ChatColor.YELLOW+"点击填写称号简介")));
        }

        // Submit
        inventory.setItem(49, GUIComponentLib.createItemStackWithoutLore(Material.GREEN_CONCRETE, ChatColor.GREEN + "颁发称号"));

        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        switch (event.getSlot()) {
            case 21 -> { // Identity

            }
            case 22 -> { // TitleName

            }
            case 45 -> { // Return
                GUI.TITLE.display(player, guiMeta);
            }
            case 49 -> { // Submit
                ArrayList<Object> draft = (ArrayList<Object>) guiMeta.getAddition();

                if (!(draft.get(0) instanceof Identity identity)) {
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.RED + "未选择身份！").create());
                    GUI.TITLE_AWARD.display(player, guiMeta);
                    return;
                }

                Object nameObj = draft.get(1);
                if (!(nameObj instanceof String) || ((String) nameObj).isEmpty()) {
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.RED + "未填写称号名称！").create());
                    GUI.TITLE_AWARD.display(player, guiMeta);
                    return;
                }
                String titleName = (String) nameObj;

                Object descObj = draft.get(2);
                if (!(descObj instanceof List<?> descList) || descList.isEmpty()) {
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.RED + "未填写简介！").create());
                    GUI.TITLE_AWARD.display(player, guiMeta);
                    return;
                }

                StringBuilder builder = new StringBuilder();
                for (Object item : descList) {
                    if (!(item instanceof String)) {
                        player.spigot().sendMessage(new ComponentBuilder(ChatColor.RED + "简介内容不合法！").create());
                        GUI.TITLE_AWARD.display(player, guiMeta);
                        return;
                    }
                    builder.append((String) item);
                }
                String description = builder.toString();

                Object profileObj = draft.get(3);
                if (!(profileObj instanceof PlayerProfile playerProfile)) {
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.RED + "未选择玩家！").create());
                    GUI.TITLE_AWARD.display(player, guiMeta);
                    return;
                }

                Profile profile = Data.PROFILE_MAP_WITH_UUID.get(playerProfile.getUniqueId());
                if (profile == null) {
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.RED + "找不到该玩家的资料！").create());
                    GUI.TITLE_AWARD.display(player, guiMeta);
                    return;
                }

                profile.getTitleManager().addTitle(new Title(titleName, description, identity));
                GUI.TITLE.display(player, guiMeta);
            }
        }
    }
}
