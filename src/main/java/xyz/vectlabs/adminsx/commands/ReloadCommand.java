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

public class ReloadCommand implements SubCommand {
    public ReloadCommand(){

    }

    @Override
    public boolean onSubCommand(CommandSender sender, Command cmd, String label, String[] args) {

        AdminsX.plugin.reloadConfig();
        sender.sendMessage("AdminsX reloaded successfully.");
        return true;
    }

    @Override
    public List<String> suggestTabCompletes(CommandSender sender, Command cmd, String label, String[] args) {
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
        return "adminsx.command.reload";
    }



}
