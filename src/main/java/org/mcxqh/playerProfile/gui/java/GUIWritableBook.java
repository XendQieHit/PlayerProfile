package org.mcxqh.playerProfile.gui.java;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;

public abstract class GUIWritableBook implements GUITemplate {
    @Override
    public void display(Player player, GUIMeta guiMeta) {
        guiMeta.setGuiPanel(GUIPanel.TITLE_AWARD_DESCRIPTION);
        ItemStack writingBook = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta bookMeta = (BookMeta) writingBook.getItemMeta();
        bookMeta.setPage(0, "在此输入文本，关闭界面以保存内容");
        writingBook.setItemMeta(bookMeta);
        player.openBook(writingBook);
        PlayerProfile.protocolManager.addPacketListener(new PacketAdapter(
                PlayerProfile.instance,
                PacketType.Play.Client.B_EDIT
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (Data.GUI_META_MAP_FOR_PLAYER.get(event.getPlayer()).getGuiPanel().panelType == GUIPanel.PanelType.WRITABLE_BOOK) {
                    event.getPacket().getStringArrays();
                }
            }
        });
    }

    @Override
    abstract public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta);
}
