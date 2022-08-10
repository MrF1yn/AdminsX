package xyz.vectlabs.adminsx.databases;


import com.google.gson.Gson;
import xyz.vectlabs.adminsx.AdminsX;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLite implements IDatabase {
    Gson gson = new Gson();
    private String url;

    private Connection connection;

    @Override
    public String name() {
        return "SQLite";
    }

    @Override
    public boolean connect() {
        File folder = new File(AdminsX.plugin.getDataFolder() + "/Cache");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                AdminsX.plugin.getLogger().severe("Could not create /Cache folder!");
            }
        }
        File dataFolder = new File(folder.getPath() + "/cache.db");
        if (!dataFolder.exists()) {
            try {
                if (!dataFolder.createNewFile()) {
                    AdminsX.plugin.getLogger().severe("Could not create /Cache/cache.db file!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        this.url = "jdbc:sqlite:" + dataFolder;
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof ClassNotFoundException) {
                AdminsX.plugin.getLogger().severe("Could Not Found SQLite Driver on your system!");
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void init() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS player_info (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "UUID VARCHAR(200), NAME VARCHAR(200), STATUS BOOLEAN, INVENTORY BLOB);";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

