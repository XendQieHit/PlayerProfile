package org.mcxqh.playerProfile.gui.java;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.inventory.view.CrafterView;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;

import java.util.logging.Logger;

public class test implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TEST);
        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL, "aaa");
        player.openInventory(inventory);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        Logger.getLogger("PlayerProfile").info("clicked");
        if (event.getView().getType() == InventoryType.ANVIL) {
            Logger.getLogger("PlayerProfile").info("it is a anvil!");
            if (event.getView() instanceof AnvilView view) {
                Logger.getLogger("PlayerProfile").info(view.getTitle());
            }
            // It is an anvil, but it can not be cast to AnvilView. How?
        }
    }
}
