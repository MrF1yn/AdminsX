package xyz.vectlabs.adminsx.databases;

public class PlayerInfo {
    public String uuid;
    public String name;
    public boolean status;
    public byte[] inventory;

    public PlayerInfo(String uuid, String name, boolean status, byte[] inventory) {
        this.uuid = uuid;
        this.name = name;
        this.status = status;
        this.inventory = inventory;
    }
}
