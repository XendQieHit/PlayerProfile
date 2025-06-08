package org.mcxqh.playerProfile.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;

import java.util.logging.Logger;

public class PlayerClickInventory implements Listener {

    /**
     * This method is used in GUI detection.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void EventInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GUIMeta guiMeta = Data.GUI_META_MAP_FOR_PLAYER.get(player);
        if (guiMeta.getGuiPanel() != null) {
            guiPanelDetermine(event, guiMeta, player);
        }
    }

    private static void guiPanelDetermine(InventoryClickEvent event, GUIMeta guiMeta, Player player) {
        Logger.getLogger("PlayerProfile").info(player.getDisplayName());
        GUI.GUI_EXECUTE_MAP_WITH_GUI_PANEL.get(guiMeta.getGuiPanel()).execute(event, player, guiMeta);
        event.setCancelled(true);
    }
}
