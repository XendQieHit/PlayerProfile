package org.mcxqh.playerProfile.gui.java;

import com.comphenix.protocol.ProtocolLib;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.mcxqh.playerProfile.gui.GUIMeta;
import org.mcxqh.playerProfile.gui.GUITemplate;

public class GUIAnvil implements GUITemplate {

    @Override
    public void display(Player player, GUIMeta guiMeta) {
        toNMS(player)
    }

    public EntityPlayer toNMS(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public void execute(InventoryClickEvent event, Player player, GUIMeta guiMeta) {

    }
}
