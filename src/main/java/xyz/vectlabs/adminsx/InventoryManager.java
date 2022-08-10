package xyz.vectlabs.adminsx;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryManager {
    private  HashMap<String, ItemStack[]> armourContents =  new HashMap<>();
    private  HashMap<String, ItemStack[]> inventoryContents =  new HashMap<>();
    private  HashMap<String, Location> locations = new HashMap<>();
    private  HashMap<String, Integer> xplevel = new HashMap<>();
    private  HashMap<String, GameMode> gamemode = new HashMap<>();


    public  void saveInventory(Player player) {

        armourContents.put(player.getName(), player.getInventory().getArmorContents());
        inventoryContents.put(player.getName(), player.getInventory().getContents());
        locations.put(player.getName(), player.getLocation());
        xplevel.put(player.getName(), player.getLevel());
        gamemode.put(player.getName(), player.getGameMode());
        player.getInventory().clear();
        player.setLevel(0);

    }


    public  void restoreInventory(Player player) {

        player.getInventory().clear();
        player.teleport(locations.get(player.getName()));
        player.getInventory().setContents(inventoryContents.get(player.getName()));
        player.getInventory().setArmorContents(armourContents.get(player.getName()));
        player.setLevel(xplevel.get(player.getName()));
        player.setGameMode(gamemode.get(player.getName()));

        xplevel.remove(player.getName());
        locations.remove(player.getName());
        armourContents.remove(player.getName());
        inventoryContents.remove(player.getName());
        gamemode.remove(player.getName());

    }

}

