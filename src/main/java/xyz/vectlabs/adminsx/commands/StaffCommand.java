package xyz.vectlabs.adminsx.commands;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.Utils;
import xyz.vectlabs.adminsx.commands.handler.SubCommand;
import xyz.vectlabs.adminsx.databases.PlayerInfo;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

public class StaffCommand implements SubCommand {

    public StaffCommand(){

    }

    @Override
    public boolean onSubCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        Bukkit.getScheduler().runTaskAsynchronously(AdminsX.plugin, () -> {
            try {
                boolean status;
                PlayerInfo playerInfo = AdminsX.plugin.getDb().getPlayerInfo(p.getUniqueId());
                if (playerInfo == null) {
                    status = false;
                } else {
                    status = playerInfo.status;
                }
                Bukkit.getScheduler().runTask(AdminsX.plugin, () -> {
                    String playerGroup = null;
                    for (String s : AdminsX.plugin.getConfigs().getMainConfig().getConfigurationSection("staff-command").getKeys(false)) {
                       System.out.println(s);
                    }
                    for (String command : AdminsX.plugin.getConfigs().getMainConfig().getStringList("staff-command.on.admin")) {
                        System.out.println(command);
                    }
                    for (String s : AdminsX.plugin.getConfigs().getMainConfig().getConfigurationSection("staff-command." + (!status ? "true" : "false")).getKeys(false)) {
                        if (p.hasPermission("adminsx.group." + s)) {
                            playerGroup = s;
                            break;
                        }
                    }
                    if (playerGroup == null) {
                        //TODO: NO GROUPS FOUND
                         p.sendMessage("No groups were found.");
                        return;
                    }
                    if (!status) {
                        AdminsX.plugin.getInvManager().saveInventory(p);
                        p.sendMessage("Staff mode turned on.");
                    }
                    else {
                        AdminsX.plugin.getInvManager().restoreInventory(p);
                        p.sendMessage("Staff mode turned off.");
                    }
                    for (String command : AdminsX.plugin.getConfigs().getMainConfig().getStringList("staff-command." + (!status ? "true" : "false") + "." + playerGroup)) {
                        Utils.parseAndExecCommand(p, command);
                    }
                    AdminsX.plugin.getHotBarHandler().giveHotBar(p, playerGroup);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        });
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
