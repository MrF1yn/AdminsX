package xyz.vectlabs.adminsx;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.vectlabs.adminsx.commands.DelVaultCommand;
import xyz.vectlabs.adminsx.commands.ReloadCommand;
import xyz.vectlabs.adminsx.commands.StaffCommand;
import xyz.vectlabs.adminsx.commands.VaultCommand;
import xyz.vectlabs.adminsx.commands.handler.AdminsXCommand;
import xyz.vectlabs.adminsx.configs.Config;
import xyz.vectlabs.adminsx.databases.IDatabase;
import xyz.vectlabs.adminsx.databases.SQLite;
import xyz.vectlabs.adminsx.inventoryhandling.InventoryManager;

import java.io.File;

public final class AdminsX extends JavaPlugin {
    public static AdminsX plugin;
    private Config config;
    private InventoryManager invManager;
    private IDatabase db;
    @Override
    public void onEnable() {
        plugin = this;
        config = new Config();
        config.init();
        invManager = new InventoryManager();
        db = new SQLite();
        db.connect();
        db.init();
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        AdminsXCommand command = new AdminsXCommand(
                new StaffCommand(),
                new VaultCommand(),
                new DelVaultCommand(),
                new ReloadCommand()
        );
        getCommand("ax").setExecutor(command);
        getCommand("ax").setTabCompleter(command);
//        System.out.println("CONFIG");
//        for (String s : config.getMainConfig().getConfigurationSection("staff-command.true").getKeys(false)) {
//            System.out.println(s);
//        }
    }

    @Override
    public void onDisable() {

    }

    public Config getConfigs() {
        return config;
    }

    public InventoryManager getInvManager() {
        return invManager;
    }

    public IDatabase getDb() {
        return db;
    }
}
