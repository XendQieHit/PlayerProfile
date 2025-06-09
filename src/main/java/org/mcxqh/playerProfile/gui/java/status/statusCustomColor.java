package org.mcxqh.playerProfile.gui.java.status;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.mcxqh.playerProfile.Constants;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.List;
import java.util.logging.Logger;

public class statusCustomColor implements GUITemplate {

    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.STATUS_CUSTOM_COLOR);
        Inventory inventory = Bukkit.createInventory(null, 27, "调色盘");

        // Colors
        addColors(inventory);

        // Return
        inventory.setItem(26, GUIComponentLib.Return());

        player.openInventory(inventory);
    }

    private void addColors(Inventory inventory) {
        Material[] materials = Constants.CHAT_COLOR_MATERIALS_ARRAY;
        ChatColor[] chatColors = ChatColor.values();
        for (int i = 0; i < materials.length; i++) {
            inventory.setItem(i, GUIComponentLib.createItemStack(
                    materials[i],
                    chatColors[i]+Constants.CHAT_COLOR_STRING_ARRAY[i],
                    List.of(ChatColor.YELLOW + "点击设置")
            ));
        }
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        if (event.getSlot() < 16) {
            Material targetMaterial = event.getCurrentItem().getType();
            Material[] materials = Constants.CHAT_COLOR_MATERIALS_ARRAY;
            for (int i = 0; i < materials.length; i++) {
                Logger.getLogger("PlayerProfile").info(targetMaterial.name() + " | " + materials[i].name());
                if (targetMaterial == materials[i]) {
                    // 但这里很有可能更改的对象是声明的而不是引用的，还需要事件调试看看
                    Status status = (Status) guiMeta.getAddition();
                    status.setColor(ChatColor.values()[i]);
                    Logger.getLogger("PlayerProfile").info(ChatColor.values()[i].name() + " | " + status);
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.YELLOW + "设置成功!").create());
                    break;
                }
            }
        }
        GUI.STATUS_DETAIL.display(player, guiMeta);
    }
}
