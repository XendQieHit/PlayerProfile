package org.mcxqh.playerProfile.gui;

import org.bukkit.inventory.ItemStack;

public class GUIMeta {
    private int pageIndex;
    private GUIPanel guiPanel;
    private ItemStack itemStack;
    private Object addition;

    public GUIMeta(int pageIndex, GUIPanel guiPanel, ItemStack itemStack, Object addition) {
        this.pageIndex = pageIndex;
        this.guiPanel = guiPanel;
        this.itemStack = itemStack;
        this.addition = addition;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * @param pageIndex <code>0</code> means that the GUI panel is not a page.
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public GUIPanel getGuiPanel() {
        return guiPanel;
    }

    public void setGuiPanel(GUIPanel guiPanel) {
        this.guiPanel = guiPanel;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Object getAddition() {
        return addition;
    }

    public void setAddition(Object addition) {
        this.addition = addition;
    }
}
