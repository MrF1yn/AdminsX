package xyz.vectlabs.adminsx.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.StaffVault;
import xyz.vectlabs.adminsx.commands.handler.AdminsXCommand;
import xyz.vectlabs.adminsx.commands.handler.SubCommand;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VaultCommand implements SubCommand {
    private List<String> vaultNames;
    public VaultCommand(){
        vaultNames = new ArrayList<>();
        ResultSet vaults = AdminsX.plugin.getDb().getVaultNames();
        try {
            if (vaults != null) {
                do {
                    vaultNames.add(vaults.getString("NAME"));
                } while (vaults.next());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSubCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if(args.length<1){
            //TODO: add invalid msg
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, ()->{

            StaffVault vault = StaffVault.getStaffVaultOrCreate(args[0]);
            Bukkit.getScheduler().runTask(AdminsX.plugin, ()->{
                vault.open(p);
            });

        });
        return true;
    }

    @Override
    public List<String> suggestTabCompletes(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return AdminsXCommand.sortedResults(args[0], new ArrayList<>(vaultNames));
        }
        return null;
    }

    @Override
    public String getName() {
        return "vault";
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public String getPermission() {
        return "adminsx.command.vault";
    }



}
