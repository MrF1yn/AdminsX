package xyz.vectlabs.adminsx.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.StaffVault;
import xyz.vectlabs.adminsx.commands.handler.AdminsXCommand;
import xyz.vectlabs.adminsx.commands.handler.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class DelVaultCommand implements SubCommand {
    public DelVaultCommand(){

    }

    @Override
    public boolean onSubCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if(args.length<1){
            //TODO: add invalid msg
            p.sendMessage("Mention a name.");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, ()->{

            StaffVault.deleteVault(args[0]);
            Bukkit.getScheduler().runTask(AdminsX.plugin, () -> {
                p.sendMessage("Successfully deleted vault.");
            });

        });
        return true;
    }

    @Override
    public List<String> suggestTabCompletes(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return AdminsXCommand.sortedResults(args[0], new ArrayList<>(VaultCommand.vaultNames));
        }
        return null;
    }

    @Override
    public String getName() {
        return "delVault";
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public String getPermission() {
        return "adminsx.command.delVault";
    }



}
