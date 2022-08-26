package xyz.vectlabs.adminsx;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        if(!(e.getInventory().getHolder() instanceof StaffVault.StaffVaultHolder))
            return;
        StaffVault.StaffVaultHolder vaultHolder = (StaffVault.StaffVaultHolder) e.getInventory().getHolder();
        if(e.getInventory().getViewers().size()>1)return;
        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, ()->{
            vaultHolder.getVault().save();
        });
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        AdminsX.plugin.getHotBarHandler().onClick(e);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        AdminsX.plugin.getHotBarHandler().onClick(e);
    }


}
