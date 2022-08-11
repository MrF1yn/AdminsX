package xyz.vectlabs.adminsx.databases;


import xyz.vectlabs.adminsx.StaffVault;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public interface IDatabase {

    String name();

    boolean connect();

    void init();

    public boolean isVaultExists(String name);
    public void createVault(String name, byte[] rawInv);
    public void updateVault(String name, byte[] rawInv);
    public StaffVault getVault(String name);
    public boolean isPlayerExists(UUID uuid);
    public void createPlayer(UUID uuid, String name, boolean status, byte[] rawInv);
    public void updatePlayer(UUID uuid, boolean status);
    public void updatePlayer(UUID uuid, byte[] rawInv);
    public void updatePlayer(UUID uuid, byte[] rawInv, boolean status);
    public ResultSet getPlayerInfo(UUID uuid);

}
