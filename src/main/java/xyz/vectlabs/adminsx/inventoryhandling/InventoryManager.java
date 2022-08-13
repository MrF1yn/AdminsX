package xyz.vectlabs.adminsx.inventoryhandling;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.databases.PlayerInfo;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

public class InventoryManager {
    private  HashMap<String, ItemStack[]> armourContents =  new HashMap<>();
    private  HashMap<String, ItemStack[]> inventoryContents =  new HashMap<>();
    private  HashMap<String, Location> locations = new HashMap<>();
    private  HashMap<String, Integer> xplevel = new HashMap<>();
    private  HashMap<String, GameMode> gamemode = new HashMap<>();


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
//        armourContents.put(player.getName(), player.getInventory().getArmorContents());
//        inventoryContents.put(player.getName(), player.getInventory().getContents());
//        locations.put(player.getName(), player.getLocation());
//        xplevel.put(player.getName(), player.getLevel());
//        gamemode.put(player.getName(), player.getGameMode());


    }


    public void restoreInventory(Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, () -> {
            try {
                PlayerInfo playerInfo = AdminsX.plugin.getDb().getPlayerInfo(player.getUniqueId());
                if (playerInfo == null)return;
                boolean status = playerInfo.status;
                AdminsX.plugin.getDb().updatePlayer(player.getUniqueId(), false);
                RawInv inv = RawInv.deserialize(playerInfo.inventory);
                Bukkit.getScheduler().runTask(AdminsX.plugin, () -> {
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


//        xplevel.remove(player.getName());
//        locations.remove(player.getName());
//        armourContents.remove(player.getName());
//        inventoryContents.remove(player.getName());
//        gamemode.remove(player.getName());

    }

}

