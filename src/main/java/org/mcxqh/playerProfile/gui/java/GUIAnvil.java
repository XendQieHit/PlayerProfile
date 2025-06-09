package org.mcxqh.playerProfile.gui.java;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.mcxqh.playerProfile.PlayerProfile;
import org.mcxqh.playerProfile.gui.GUI;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUIPanel;
import org.mcxqh.playerProfile.gui.GUITemplate;

import java.util.logging.Logger;

import static org.mcxqh.playerProfile.PlayerProfile.protocolManager;

public abstract class GUIAnvil implements GUITemplate {

    @Override
    public void display(Player player, GUIMeta guiMeta) {
        GUITemplate latestGUI = guiMeta.getGuiPanel().instance;
        onDisplay(player, guiMeta);

        // Thing will append when player submit string of renamed item
        PacketAdapter renameItem = new PacketAdapter(
                PlayerProfile.instance,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.ITEM_NAME
        ) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Logger.getLogger("PlayerProfile").info("Renamed!");
                onRename(player, guiMeta, event.getPacket().getStrings().read(0));

            }
        };
        protocolManager.addPacketListener(renameItem);

        Inventory inventory = Bukkit.createInventory(null, InventoryType.ANVIL, "请输入文字后点击右边物品以确定");
        inventory.setItem(0, GUIComponentLib.createItemStackWithoutLore(Material.NAME_TAG, "请输入文字"));
        player.openInventory(inventory);
    }

    /**
     * This method will be executed when displayed GUIAnvil to player.
     */
    abstract public void onDisplay(Player player, GUIMeta guiMeta);

    /**
     * This method will be executed when player submit string of renamed item.
     * @param string The string of renamed item you will be gotten.
     */
    abstract public void onRename(Player player, GUIMeta guiMeta, String string);

    abstract public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta);
}
