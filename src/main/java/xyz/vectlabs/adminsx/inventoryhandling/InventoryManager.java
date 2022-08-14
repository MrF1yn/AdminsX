package xyz.vectlabs.adminsx.inventoryhandling;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.StaffVault;
import xyz.vectlabs.adminsx.commands.VaultCommand;
import xyz.vectlabs.adminsx.databases.PlayerInfo;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

public class InventoryManager {


    public void saveInventory(Player player) {
        RawInv inv = new RawInv(InventoryType.PLAYER);
        inv.setInvItems(player.getInventory().getContents());
        inv.setArmorContents(player.getInventory().getArmorContents());
        inv.setLocation(player.getLocation());
        inv.setXpLevel(player.getLevel());
        inv.setGameMode(player.getGameMode());
        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, () -> {
            try {
                if (AdminsX.plugin.getDb().isPlayerExists(player.getUniqueId())) {
                    AdminsX.plugin.getDb().updatePlayer(player.getUniqueId(), inv.serialize(), true);
                } else {
                    AdminsX.plugin.getDb().createPlayer(player.getUniqueId(), player.getName(), true, inv.serialize());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bukkit.getScheduler().runTask(AdminsX.plugin, () -> {
                player.getInventory().clear();
                player.setLevel(0);
            });
        });
    }


    public void restoreInventory(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, () -> {
            try {
                PlayerInfo playerInfo = AdminsX.plugin.getDb().getPlayerInfo(player.getUniqueId());
                if (playerInfo == null)return;
                boolean status = playerInfo.status;
                AdminsX.plugin.getDb().updatePlayer(player.getUniqueId(), false);
                RawInv inv = RawInv.deserialize(playerInfo.inventory);
                StaffVault vault = StaffVault.getStaffVaultOrCreate("d_"+player.getName());
                Bukkit.getScheduler().runTask(AdminsX.plugin, () -> {
                    if(vault.getInventory().firstEmpty()==-1){
                        vault.getInventory().clear();
                    }
                    vault.getInventory().addItem(player.getInventory().getContents());
                    vault.save();
                    player.getInventory().clear();
                    player.teleport(inv.getLocation());
                    player.getInventory().setContents(inv.getInvItems());
                    player.getInventory().setArmorContents(inv.getArmorContents());
                    player.setLevel(inv.getXpLevel());
                    player.setGameMode(inv.getGameMode());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

}

