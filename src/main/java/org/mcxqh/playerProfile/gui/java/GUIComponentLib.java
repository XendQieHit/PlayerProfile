package org.mcxqh.playerProfile.gui.java;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;
import java.util.List;

public class GUIComponentLib {
    public static void addComponent(Inventory inventory, int index, Material material, String name, List<String> lore) {
        ItemStack itemStack = createItemStack(material, name, lore);
        inventory.setItem(index, itemStack);
    }

    public static @NotNull ItemStack createItemStack(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createItemStackWithoutLore(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void placeHolderFrame(Inventory inventory) {
        int[] placeHolderIndex = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int holderIndex : placeHolderIndex) {
            inventory.setItem(holderIndex, placeHolder());
        }
    }

    public static ItemStack placeHolder() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setItemName("");
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack playerInfo(Profile profile) {
        // PlayerInfo
        List<String> playerInfoLore = new ArrayList<>();
        playerInfoLore.add("状态：" + profile.getStatusManager().getPresentStatus().getClass().getSimpleName()); // Status
        profile.getIdentityManager().getIdentities().forEach(identity -> { // Guild, Team
            switch (identity.getIdentityType()) {
                case GUILD -> playerInfoLore.add("公会：" + identity.getCollective().getName());
                case TEAM -> playerInfoLore.add("队伍：" + identity.getCollective().getName());
            }
        });
        return createItemStack(Material.PLAYER_HEAD, profile.getNameAsString(), playerInfoLore);
    }

    public static ItemStack Return() {
        return createItemStackWithoutLore(Material.RED_WOOL, ChatColor.WHITE + "上一级菜单");
    }

    public static ItemStack next() {
        return createItemStackWithoutLore(Material.ARROW, ChatColor.GRAY + "上一页");
    }

    public static ItemStack back() {
        return createItemStackWithoutLore(Material.ARROW, ChatColor.GRAY + "下一页");
    }

    public static ItemStack exit() {
        return createItemStackWithoutLore(Material.BARRIER, ChatColor.RED + "关闭");
    }

    public static ItemStack enable() {
        return createItemStackWithoutLore(Material.GREEN_CONCRETE, ChatColor.GREEN + "点击启用");
    }
    public static ItemStack disable() {
        return createItemStackWithoutLore(Material.RED_CONCRETE, ChatColor.RED + "点击禁用");
    }
    public static ItemStack enableWithName(String s) {
        return createItemStackWithoutLore(Material.GREEN_CONCRETE, ChatColor.GREEN + s);
    }
    public static ItemStack disableWithName(String s) {
        return createItemStackWithoutLore(Material.RED_CONCRETE, ChatColor.RED + s);
    }
}
