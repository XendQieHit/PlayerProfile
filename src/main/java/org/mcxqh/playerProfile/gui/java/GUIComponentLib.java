package org.mcxqh.playerProfile.gui.java;

import jdk.jfr.Experimental;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;
import java.util.List;

public class GUIComponentLib {
    /* 流式操作创建物品，尽情期待 */
    @Experimental
    public class ItemStackBuilder {
        private Material material;
        private ItemStack previewItemStack;

        public ItemStackBuilder(Material material) {
            this.material = material;
        }

        public ItemStack build() {
            return this.previewItemStack;
        }
    }

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

    public static ItemStack enchantEffect(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setEnchantmentGlintOverride(true);
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
        return createItemStackWithoutLore(Material.RED_WOOL, ChatColor.RED + "上一级菜单");
    }

    public static ItemStack next() {
        return createItemStackWithoutLore(Material.ARROW, ChatColor.GRAY + "下一页");
    }

    public static ItemStack back() {
        return createItemStackWithoutLore(Material.ARROW, ChatColor.GRAY + "上一页");
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

    /**
     * Create list GUI with provided parameters.
     * @param pageIndex This will be used for add turn-page buttons.
     * @param indexesOfHighlightItem These item at this indexes will be set the effect of enchanted. Look like highlighted.
     */
    public static void createListPanel(Inventory inventory, Material[] materials, String[] itemNames, List<List<String>> itemLore, int pageIndex, int @Nullable [] indexesOfHighlightItem) {
        int length = materials.length;
        // PlaceHolder
        placeHolderFrame(inventory);
        // Instances Filling
        for (int i = 28 * (pageIndex - 1), j = 0; i < length && i < 28 * pageIndex; i++, j++) {
            GUIComponentLib.addComponent(inventory, j + 10 + (j / 7) * 2, materials[i], itemNames[i], itemLore.get(i));
        }
        // Next
        if (length > 28 && pageIndex * 28 < length) inventory.setItem(53, GUIComponentLib.next());
        // Back
        if (pageIndex > 1) inventory.setItem(45, GUIComponentLib.back());
        // Return
        inventory.setItem(49, Return());

        // Highlight
        if (indexesOfHighlightItem != null) {
            for (int i : indexesOfHighlightItem) {
                int indexInInventory = i + 10 + (i / 7) * 2;
                inventory.setItem(indexInInventory, enchantEffect(inventory.getItem(indexInInventory)));
            }
        }
    }

    public static void createListPanel(Inventory inventory, Material material, String[] itemNames, List<List<String>> itemLore, int pageIndex, int @Nullable [] indexesOfHighlightItem) {
        int length = itemNames.length;
        // PlaceHolder
        placeHolderFrame(inventory);
        // Instances Filling
        for (int i = 28 * (pageIndex - 1), j = 0; i < length && i < 28 * pageIndex; i++, j++) {
            GUIComponentLib.addComponent(inventory, j + 10 + (j / 7) * 2, material, itemNames[i], itemLore.get(i));
        }
        // Next
        if (length > 28 && pageIndex * 28 < length) inventory.setItem(53, GUIComponentLib.next());
        // Back
        if (pageIndex > 1) inventory.setItem(45, GUIComponentLib.back());
        // Return
        inventory.setItem(49, Return());

        // Highlight
        if (indexesOfHighlightItem != null) {
            for (int i : indexesOfHighlightItem) {
                int indexInInventory = i + 10 + (i / 7) * 2;
                inventory.setItem(indexInInventory, enchantEffect(inventory.getItem(indexInInventory)));
            }
        }
    }
}
