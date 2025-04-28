package org.mcxqh.playerProfile.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GUITemplate {
    void display(Player player, GUIMeta guiMeta);
    void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta);
}
