package xyz.vectlabs.adminsx;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import xyz.vectlabs.adminsx.inventoryhandling.RawInv;

import java.io.IOException;
import java.util.HashMap;

public class StaffVault {

    public static HashMap<String, StaffVault>LOADED_VAULTS = new HashMap<>();

    private String name;
    private Inventory inventory;

    public StaffVault(String name, ItemStack[] itemStacks){
        this.name = name;
        this.inventory = Bukkit.createInventory(new StaffVault.StaffVaultHolder(this), 54, name);
        if(itemStacks!=null&&itemStacks.length<=54){
            inventory.setContents(itemStacks);
        }
        LOADED_VAULTS.put(name, this);
    }

    public void open(Player player){
        player.openInventory(inventory);
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    public String getName() {
        return name;
    }

    public void save(){
        for(HumanEntity ent : inventory.getViewers()){
            ent.closeInventory();
        }
        RawInv inv = new RawInv(InventoryType.CHEST);
        inv.setInvItems(inventory.getContents());
        try {
            if (AdminsX.plugin.getDb().isVaultExists(name)) {
                AdminsX.plugin.getDb().updateVault(name, inv.serialize());
                return;
            }
            AdminsX.plugin.getDb().createVault(name, inv.serialize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StaffVault getStaffVaultOrCreate(String name){
        if(LOADED_VAULTS.containsKey(name)){
            return LOADED_VAULTS.get(name);
        }
        if(AdminsX.plugin.getDb().isVaultExists(name)){
            return AdminsX.plugin.getDb().getVault(name);
        }
        StaffVault vault = new StaffVault(name, new ItemStack[]{});
        RawInv inv = new RawInv(InventoryType.CHEST);
        inv.setInvItems(vault.getInventory().getContents());
        try {
            AdminsX.plugin.getDb().createVault(name, inv.serialize());
        }catch (Exception e){
            e.printStackTrace();
        }
        return vault;
    }

    public static void deleteVault(String name) {
        if (LOADED_VAULTS.containsKey(name)) {
            for(HumanEntity ent : LOADED_VAULTS.get(name).getInventory().getViewers()){
                ent.closeInventory();
            }
            LOADED_VAULTS.remove(name);
        }
        if (AdminsX.plugin.getDb().isVaultExists(name)) {
            AdminsX.plugin.getDb().deleteVault(name);
        }
    }

    public static class StaffVaultHolder implements InventoryHolder{
        StaffVault vault;
        public StaffVaultHolder(StaffVault vault){
            this.vault = vault;
        }

        public String getName(){
            return vault.getName();
        }

        @Override
        public Inventory getInventory() {
            return vault.getInventory();
        }

        public StaffVault getVault() {
            return vault;
        }
    }
}
