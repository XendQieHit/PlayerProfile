package org.mcxqh.playerProfile.gui.java;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUITemplate;

import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class GUIList implements GUITemplate {
    abstract public void display(Player player, GUIMeta guiMeta);

    abstract public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta);

    /**
     * @param detailGUI The GUI will be opened when selection be clicked.
     * @param GUIMetaOperation The action will be executed when selection be clicked.
     */
    public void executeInList(InventoryClickEvent event, Player player, GUIMeta guiMeta, GUITemplate preGUI, GUITemplate detailGUI, Consumer<GUIMeta> GUIMetaOperation) {
        int slot = event.getSlot();
        final int pageIndex = guiMeta.getPageIndex();
        final ItemStack currentItem = event.getCurrentItem();
        switch (slot) {
            case 49 -> preGUI.display(player, guiMeta); // Return
            case 45 -> { // Back
                if (currentItem.getType() == Material.ARROW) {
                    Logger.getLogger("PlayerProfile").info("back");
                    guiMeta.setPageIndex(pageIndex - 1);
                    this.display(player, guiMeta);
                }
            }
            case 53 -> { // Next
                if (currentItem.getType() == Material.ARROW) {
                    Logger.getLogger("PlayerProfile").info("next");
                    guiMeta.setPageIndex(pageIndex + 1);
                    this.display(player, guiMeta);
                }
            }
            default -> { // StatusInstance
                if (isNotAir(slot, currentItem)) {
                    GUIMetaOperation.accept(guiMeta);
                    detailGUI.display(player, guiMeta);
                }
            }
        }
    }

    public boolean isNotAir(int slot, ItemStack currentItem) {
        Logger.getLogger("PlayerProfile").info(slot+"");
        return currentItem != null && slot < 45 && slot > 9 && slot % 9 > 0 && slot % 9 < 8;
    }
}
