package xyz.vectlabs.adminsx;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HotBarHandler {

    public HashMap<String, List<String>> itemCommands;
    public HashMap<String, HotBarHandler.HotBar> grpToHotBar;


    public HotBarHandler(){
        grpToHotBar = new HashMap<>();
        itemCommands = new HashMap<>();
        for (String playerGroup: AdminsX.plugin.getConfigs().getMainConfig().getConfigurationSection("staff-hotbar").getKeys(false)) {
            HotBarHandler.HotBar hotBar = new HotBarHandler.HotBar();
            for (String slot: AdminsX.plugin.getConfigs().getMainConfig().getConfigurationSection("staff-hotbar."+ playerGroup).getKeys(false)) {
                int hSlot = Integer.parseInt(slot);
                String name = Utils.color(AdminsX.plugin.getConfigs().getMainConfig().getString("staff-hotbar." + playerGroup + "." + hSlot + ".name"));
                Material material = Material.valueOf(AdminsX.plugin.getConfigs().getMainConfig().getString("staff-hotbar." + playerGroup + "." + hSlot + ".material"));
                List<String> lore = AdminsX.plugin.getConfigs().getMainConfig().getStringList("staff-hotbar." + playerGroup + "." + hSlot + ".lore").
                        stream().map(Utils::color).collect(Collectors.toList());
                List<String> commands = AdminsX.plugin.getConfigs().getMainConfig().getStringList("staff-hotbar." + playerGroup + "." + hSlot + ".commands");
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                meta.setLore(lore);
                item.setItemMeta(meta);
                NBTItem nbtItem = new NBTItem(item);
                nbtItem.setString("adminsx", playerGroup+":"+hSlot);
                item = nbtItem.getItem();
                itemCommands.put(playerGroup+":"+hSlot, commands);
                hotBar.put(hSlot, item);
            }
            grpToHotBar.put(playerGroup, hotBar);
        }
    }

    public void giveHotBar(Player p, String playerGroup){
        HotBarHandler.HotBar hotBar = grpToHotBar.get(playerGroup);
        System.out.println("Give hotbar called.");
        if (hotBar==null)return;
        System.out.println("Give hotbar called 1");
        for(int i : hotBar.items.keySet()){
            System.out.println(i);
            p.getInventory().setItem(i, hotBar.items.get(i));
        }
        p.updateInventory();
    }

    public void onClick(PlayerInteractEvent e){
        if(e.getItem()==null)return;
        NBTItem item = new NBTItem(e.getItem());
        if(!item.hasKey("adminsx"))return;
        e.setCancelled(true);
        String s = item.getString("adminsx");
        for(String cmd : itemCommands.get(s)){
            Utils.parseAndExecCommand(e.getPlayer(), cmd);
        }
    }

    public void onClick(InventoryClickEvent e) {
        if (e.getClickedInventory()==null)return;
        if(e.getCurrentItem()==null)return;
        if(e.getCurrentItem().getType()==Material.AIR)return;
        NBTItem item = new NBTItem(e.getCurrentItem());
        if(!item.hasKey("adminsx"))return;
        e.setCancelled(true);
        String s = item.getString("adminsx");
        for(String cmd : itemCommands.get(s)){
            Utils.parseAndExecCommand((Player) e.getWhoClicked(), cmd);
        }
    }

    public static class HotBar{

        public HashMap<Integer, ItemStack> items;

        public HotBar() {
            this.items = new HashMap<>();
        }

        public void put(int slot, ItemStack item){
            this.items.put(slot, item);
        }
    }


}
