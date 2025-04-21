package org.mcxqh.playerProfile.gui.java;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mcxqh.playerProfile.Data;
import org.mcxqh.playerProfile.gui.GUITemplate;
import org.mcxqh.playerProfile.players.Profile;

import java.util.ArrayList;
import java.util.List;

public class menu implements GUITemplate {
    @Override
    public void display(Player player) {
        Profile profile = Data.profileMapWithUUID.get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(player, 54, "PlayerProfile玩家档案");

        // Placeholder
        int[] placeHolderIndex = {0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int holderIndex : placeHolderIndex) {
            ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            item.getItemMeta().setItemName("");
            inventory.setItem(holderIndex, item);
        }

        // PlayerInfo
        ItemStack playerInfo = new ItemStack(Material.PLAYER_HEAD);
        List<String> playerInfoLore = new ArrayList<>();
        playerInfoLore.add("状态：" + profile.getStatusManager().getPresentStatus().toString()); // Status
        profile.getIdentityManager().getIdentities().forEach(identity -> { // Guild, Team
            switch (identity.getIdentityType()) {
                case GUILD -> playerInfoLore.add("公会：" + identity.getCollective().getName());
                case TEAM -> playerInfoLore.add("队伍：" + identity.getCollective().getName());
            }
        });
        playerInfo.getItemMeta().setLore(playerInfoLore);
        playerInfo.getItemMeta().setItemName(profile.getNameAsString());
        inventory.setItem(4, playerInfo);

        // Batch
        Material[] materials = {
                Material.REDSTONE_TORCH, // Status
                Material.BOOK, // ProfileManagement
                Material.NAME_TAG, // Title
                Material.GOLDEN_HORSE_ARMOR, // Guild
                Material.BLUE_BANNER, // Team
                Material.IRON_PICKAXE // Group
        };
        String[] itemNames = {ChatColor.GREEN+"状态", ChatColor.GREEN+"档案", ChatColor.GREEN+"称号", ChatColor.GREEN+"公会", ChatColor.GREEN+"队伍", ChatColor.GREEN+"团队"};
        List<List<String>> lore = List.of(
                List.of("管理状态", "自定义状态"),
                List.of("管理", "迁移玩家档案"),
                List.of("管理称号", "颁发称号"),
                List.of("查看公会", "管理公会"),
                List.of("管理队伍"),
                List.of("管理团队")
        );
        int[] indexes = {21, 22, 23, 30, 31, 32};

        for (int i = 0; i < materials.length; i++) {
            ItemStack itemStack = new ItemStack(materials[i]);
            itemStack.getItemMeta().setItemName(itemNames[i]);
            itemStack.getItemMeta().setLore(lore.get(i));
            inventory.setItem(indexes[i], itemStack);
        }

        // Exit
        ItemStack exit = new ItemStack(Material.BARRIER);
        exit.getItemMeta().setItemName(ChatColor.RED + "关闭");
        inventory.setItem(49, exit);

        // End
        player.openInventory(inventory);
    }
}
