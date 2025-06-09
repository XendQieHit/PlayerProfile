package org.mcxqh.playerProfile.gui.java.title;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.gui.java.GUIAnvil;

import java.util.ArrayList;

public class titleAwardName extends GUIAnvil {


    /**
     * This method will be executed when displayed GUIAnvil to player.
     *
     * @param player
     * @param guiMeta
     */
    @Override
    public void onDisplay(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TITLE_AWARD_NAME);
    }

    /**
     * This method will be executed when player submit string of renamed item.
     *
     * @param player
     * @param guiMeta
     * @param string  The string of renamed item you will be gotten.
     */
    @Override
    public void onRename(Player player, GUIMeta guiMeta, String string) {
        ((ArrayList<Object>) guiMeta.getAddition()).set(1, string);
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        GUI.TITLE_AWARD.display(player, guiMeta);
    }
}
