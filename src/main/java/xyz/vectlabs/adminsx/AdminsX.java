package xyz.vectlabs.adminsx;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.vectlabs.adminsx.configs.Config;

import java.io.File;

public final class AdminsX extends JavaPlugin {
    public static AdminsX plugin;
    private Config config;
    private InventoryManager invManager;
    @Override
    public void onEnable() {
        plugin = this;
        config = new Config(new File("plugins/lang.yml"));
        config.init();
        invManager = new InventoryManager();
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
}