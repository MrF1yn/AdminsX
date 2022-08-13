package xyz.vectlabs.adminsx.configs;



import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import xyz.vectlabs.adminsx.AdminsX;

import java.io.File;

public class Config {


    private YamlFile languageConfig;
    private YamlFile mainConfig;

    public Config(){
    }

    public void init(){
        AdminsX.plugin.saveResource("config.yml",false);
        mainConfig = new YamlFile("plugins/AdminsX/config.yml");
        languageConfig = new YamlFile("plugins/AdminsX/lang.yml");
        try {
            mainConfig.load();
            if (!languageConfig.exists()) {
                languageConfig.createNewFile(true);
                System.out.println("New file has been created: " + languageConfig.getFilePath() + "\n");
                languageConfig.load();

                for(ConfigPath c : ConfigPath.values()){
                    languageConfig.addDefault(c.toString(), c.getValue());
                }

                languageConfig.save();

            } else {
                System.out.println(languageConfig.getFilePath() + " already exists, loading configurations...\n");
            }
            languageConfig.load(); // Loads the entire file
            // If your file has comments inside you have to load it with yamlFile.loadWithComments()
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public YamlFile getLanguageConfig() {
        return languageConfig;
    }

    public YamlFile getMainConfig() {
        return this.mainConfig;
    }

}
