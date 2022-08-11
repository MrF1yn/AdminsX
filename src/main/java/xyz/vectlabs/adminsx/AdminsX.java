package xyz.vectlabs.adminsx;

import org.bukkit.plugin.java.JavaPlugin;
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
        config = new Config(new File("plugins/lang.yml"));
        config.init();
        invManager = new InventoryManager();
        db = new SQLite();
        db.connect();
        db.init();
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
