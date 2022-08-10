package xyz.vectlabs.adminsx.configs;


import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.vectlabs.adminsx.AdminsX;

import java.io.File;

public class Config {


    private File languageFile;
    private FileConfiguration languageConfig;

    public Config(File languageFile){
        this.languageFile = languageFile;
    }

    public void init(){
        AdminsX.plugin.saveResource("config.yml",false);
        try {
            if (!languageFile.exists()) {
                languageFile.createNewFile();
                System.out.println("New file has been created: " + languageFile.getPath() + "\n");
                languageConfig =YamlConfiguration.loadConfiguration(languageFile);

                for(ConfigPath c : ConfigPath.values()){
                    languageConfig.addDefault(c.toString(), c.getValue());
                }

                languageConfig.save(languageFile);

            } else {
                System.out.println(languageFile.getPath() + " already exists, loading configurations...\n");
            }
            YamlConfiguration.loadConfiguration(languageFile); // Loads the entire file
            // If your file has comments inside you have to load it with yamlFile.loadWithComments()
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getLanguageConfig() {
        return languageConfig;
    }

    public FileConfiguration getMainConfig() {
        return AdminsX.plugin.getConfig();
    }

}
