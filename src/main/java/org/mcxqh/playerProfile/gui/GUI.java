package org.mcxqh.playerProfile.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mcxqh.playerProfile.gui.java.menu;
import org.mcxqh.playerProfile.gui.java.status.*;
import org.mcxqh.playerProfile.gui.java.test;
import org.mcxqh.playerProfile.gui.java.title.*;

public enum GUI {
    TEST(new test()),
    MENU(new menu()),
    STATUS(new status()),
    STATUS_LIST(new statusList()),
    STATUS_DETAIL(new statusDetail()),
    STATUS_CUSTOM_NAME(new statusCustomName()),
    STATUS_CUSTOM_COLOR(new statusCustomColor()),
    TITLE(new title()),
    TITLE_LIST(new titleList()),
    TITLE_DETAIL(new titleDetail()),
    TITLE_AWARD(new titleAward()),
    TITLE_AWARD_NAME(new titleAwardName()),
    TITLE_AWARD_IDENTITY(new titleAwardIdentity()),
    TITLE_AWARD_DESCRIPTION(new titleAwardDescription());

    GUITemplate gui;
    GUI(GUITemplate gui) {
        this.gui = gui;
    }

    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {
        gui.execute(event, player, guiMeta);
    }
    public void display(Player player, GUIMeta guiMeta) {
        gui.display(player, guiMeta);
    }
}