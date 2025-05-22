package org.mcxqh.playerProfile.gui.java.status;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mcxqh.playerProfile.Constants;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class statusDetail implements GUITemplate {

    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.STATUS_DETAIL);
        ItemStack statusItem = guiMeta.getItemStack();
        String itemName = statusItem.getItemMeta().getItemName();
        Logger.getLogger("PlayerProfile").info(itemName);
        Status status = (Status) guiMeta.getAddition();
        Inventory inventory = Bukkit.createInventory(null, 54, itemName);

        // PlaceHolder
        GUIComponentLib.placeHolderFrame(inventory);

        // StatusInfo
        List<String> statusInfoLore = genInfoLore(status);
        inventory.setItem(4, GUIComponentLib.createItemStack(statusItem.getType(), itemName, statusInfoLore));

        // ToggleButton
        if (status.isDisplay()) {
            inventory.setItem(22, GUIComponentLib.disable());
        } else {
            inventory.setItem(22, GUIComponentLib.enable());
        }

        // CustomColor 这个等有技术力了，改成像Hypixel那样的动态切换展示物品
        int i = 0;
        ChatColor[] chatColors = ChatColor.values();
        for (; i < chatColors.length; i++) {
            if (chatColors[i] == status.getColor()) {
                inventory.setItem(30, GUIComponentLib.createItemStackWithoutLore(Constants.CHAT_COLOR_MATERIALS_ARRAY[i], ChatColor.YELLOW+"自定义颜色"));
            }
        }

        // CustomName
        inventory.setItem(31, GUIComponentLib.createItemStack(Material.NAME_TAG, "自定义状态名称", List.of(ChatColor.YELLOW+"当前名称："+status.getRawCustomName())));

        // CustomNameToggleButton
        if (status.isDisplayCustomName()) {
            inventory.setItem(32, GUIComponentLib.disableWithName("点击禁用自定义名称"));
        } else {
            inventory.setItem(32, GUIComponentLib.enableWithName("点击启用自定义名称"));
        }

        // Return
        inventory.setItem(49, GUIComponentLib.Return());
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        Status status = (Status) guiMeta.getAddition();
        switch (event.getSlot()) {
            case 22 -> { // toggleButton
                switch (event.getCurrentItem().getType()) {
                    case RED_CONCRETE -> status.setDisplay(false);
                    case GREEN_CONCRETE -> status.setDisplay(true);
                }
                display(player, guiMeta);
            }
            case 30 -> { // CustomColor
                GUI.STATUS_CUSTOM_COLOR.display(player, guiMeta);
            }
            case 31 -> { // CustomName
                GUI.STATUS_CUSTOM_NAME.display(player, guiMeta);
            }
            case 32 -> { // toggleCustomName
                switch (event.getCurrentItem().getType()) {
                    case RED_CONCRETE -> status.setDisplayCustomName(false);
                    case GREEN_CONCRETE -> status.setDisplayCustomName(true);
                }
                display(player, guiMeta);
            }
            case 43 -> {
                guiMeta.setPageIndex(1);
                GUI.TEST.display(player, guiMeta);
            }
            case 49 -> GUI.STATUS_LIST.display(player, guiMeta); // Return
        }
    }

    private List<String> genInfoLore(Status status) {
        List<String> statusInfoLore = new ArrayList<>();
        if (status.isDisplay()) {
            statusInfoLore.add(ChatColor.GREEN + "已启用");
        } else {
            statusInfoLore.add(ChatColor.GRAY + "已禁用");
        }
        if (status.isDisplayCustomName()) {
            statusInfoLore.add(ChatColor.YELLOW + "自定义：" + ChatColor.GREEN + "已启用");
        } else {
            statusInfoLore.add(ChatColor.YELLOW + "自定义：" + ChatColor.GRAY + "已禁用");
        }
        statusInfoLore.add(ChatColor.YELLOW + "效果预览：" + status);
        return statusInfoLore;
    }
}
