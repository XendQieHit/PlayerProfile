package org.mcxqh.playerProfile.gui.java.title;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;

public class title implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.MENU_TITLE);

    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {

    }
}
