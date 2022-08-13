package xyz.vectlabs.adminsx.databases;


import com.google.gson.Gson;
import org.bukkit.inventory.ItemStack;
import xyz.vectlabs.adminsx.AdminsX;
import xyz.vectlabs.adminsx.StaffVault;
import xyz.vectlabs.adminsx.inventoryhandling.RawInv;

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
            this.connection = DriverManager.getConnection(url);
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
            sql = "CREATE TABLE IF NOT EXISTS staff_vaults (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME VARCHAR(200), INVENTORY BLOB);";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isPlayerExists(UUID uuid) {
        String sql = "SELECT * FROM player_info WHERE UUID=?;";
        try  {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean isVaultExists(String name) {
        String sql = "SELECT * FROM staff_vaults WHERE NAME=?;";
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void createVault(String name, byte[] rawInv) {
        String sql = "INSERT INTO staff_vaults (NAME, INVENTORY) VALUES (?, ?);";
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setBytes(2, rawInv);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateVault(String name, byte[] rawInv) {
        String sql = "UPDATE staff_vaults SET INVENTORY=? WHERE NAME=?;";
        try  {
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setBytes(1, rawInv);
                statement.setString(2, name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void deleteVault(String name) {
    String sql = "DELETE FROM staff_vaults WHERE NAME=?;";
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public StaffVault getVault(String name) {
        String sql = "SELECT * FROM staff_vaults WHERE NAME=?;";
        try  {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    RawInv inv = RawInv.deserialize(result.getBytes("INVENTORY"));
                    return new StaffVault(name, inv.getInvItems());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<String> getVaultNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT NAME FROM staff_vaults;";
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet result = statement.executeQuery();
                while (result.next()){
                    names.add(result.getString("NAME"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    @Override
    public void createPlayer(UUID uuid, String name, boolean status, byte[] rawInv) {
        String sql = "INSERT INTO player_info (UUID, NAME, STATUS, INVENTORY) VALUES (?, ?, ?, ?);";
        try  {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, name);
                statement.setBoolean(3, status);
                statement.setBytes(4, rawInv);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updatePlayer(UUID uuid, boolean status) {
        String sql = "UPDATE player_info SET STATUS=? WHERE UUID=?;";
        try  {
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setBoolean(1, status);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updatePlayer(UUID uuid, byte[] rawInv) {
        String sql = "UPDATE player_info SET INVENTORY=? WHERE UUID=?;";
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setBytes(1, rawInv);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updatePlayer(UUID uuid, byte[] rawInv, boolean status) {
        String sql = "UPDATE player_info SET INVENTORY=?,STATUS=? WHERE UUID=?;";
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setBytes(1, rawInv);
                statement.setBoolean(2, status);
                statement.setString(3, uuid.toString());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public PlayerInfo getPlayerInfo(UUID uuid) {
        String sql = "SELECT * FROM player_info WHERE UUID=?;";
        try  {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid.toString());
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    return new PlayerInfo(uuid.toString(), result.getString("NAME"), result.getBoolean("STATUS"),
                            result.getBytes("INVENTORY"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

