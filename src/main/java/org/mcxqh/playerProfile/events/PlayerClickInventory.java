package org.mcxqh.playerProfile.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;

import java.util.HashMap;
import java.util.Map;

public class PlayerClickInventory implements Listener {

    public static final Map<GUIPanel, GUITemplate> GUI_EXECUTE_MAP_WITH_GUI_PANEL = new HashMap<>();

    static {
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TEST, GUI.TEST);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.MENU, GUI.MENU);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.MENU_STATUS, GUI.STATUS);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_LIST, GUI.STATUS_LIST);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_DETAIL, GUI.STATUS_DETAIL);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_CUSTOM_NAME, GUI.STATUS_CUSTOM_NAME);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.STATUS_CUSTOM_COLOR, GUI.STATUS_CUSTOM_COLOR);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.MENU_TITLE, GUI.TITLE);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_LIST, GUI.TITLE_LIST);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_DETAIL, GUI.TITLE_DETAIL);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD, GUI.TITLE_AWARD);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD_COLOR, GUI.TITLE_AWARD_COLOR);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD_IDENTITY, GUI.TITLE_AWARD_IDENTITY);
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.put(GUIPanel.TITLE_AWARD_NAME, GUI.TITLE_AWARD_NAME);
    }

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
        GUI_EXECUTE_MAP_WITH_GUI_PANEL.get(guiMeta.getGuiPanel()).execute(event, player, guiMeta);
        event.setCancelled(true);
    }
}
