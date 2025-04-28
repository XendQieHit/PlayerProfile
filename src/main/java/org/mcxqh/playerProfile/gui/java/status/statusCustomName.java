package org.mcxqh.playerProfile.gui.java.status;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.AnvilView;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIComponentLib;
import org.mcxqh.playerProfile.players.profile.status.Status;

import java.util.logging.Logger;

public class statusCustomName implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.STATUS_CUSTOM_NAME);
        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL, "更改状态自定义名称");
        inventory.setItem(0, GUIComponentLib.createItemStackWithoutLore(Material.NAME_TAG, "在这里修改名称"));
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        if (event.getSlot() == 2) {
            Logger.getLogger("PlayerProfile").info("coming");
            Logger.getLogger("PlayerProfile").info(event.getView().toString());

            if (event.getView().getType() == InventoryType.ANVIL) {
                Logger.getLogger("PlayerProfile").info("is anvil");
                if (event.getView() instanceof AnvilView view) {
                    Logger.getLogger("PlayerProfile").info("is anvilView");
                    String renameText = view.getRenameText();
                    ((Status) guiMeta.getAddition()).setCustomName(renameText);
                    GUI.STATUS_DETAIL.display(player, guiMeta);
                    player.spigot().sendMessage(new ComponentBuilder(ChatColor.YELLOW + "设置成功!").create());
                }
            }
        }
    }
}
