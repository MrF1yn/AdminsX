package xyz.vectlabs.adminsx.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.commands.handler.SubCommand;

import java.util.List;

public class StaffCommand implements SubCommand {

    public StaffCommand(){

    }

    @Override
    public boolean onSubCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        boolean status = true;//TODO: FETCH FROM DB
        String playerGroup = null;
        for(String s : AdminsX.plugin.getConfigs().getMainConfig().getConfigurationSection("staff_command."+(status?"on":"off")).getKeys(false)){
            if(p.hasPermission("adminsx.group."+s)){
                playerGroup = s;
                break;
            }
        }
        if(playerGroup==null){
            //TODO: NO GROUPS FOUND
            return true;
        }
        if (status)
            AdminsX.plugin.getInvManager().saveInventory(p);
        else
            AdminsX.plugin.getInvManager().restoreInventory(p);
        for(String command : AdminsX.plugin.getConfigs().getMainConfig().getStringList("staff_command."+(status?"on":"off")+"."+playerGroup)){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        return true;
    }

    @Override
    public List<String> suggestTabCompletes(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public String getName() {
        return "staff";
    }

    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public String getPermission() {
        return "adminsx.command.staff";
    }
}
