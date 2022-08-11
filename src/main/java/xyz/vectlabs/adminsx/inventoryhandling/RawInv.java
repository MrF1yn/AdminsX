package xyz.vectlabs.adminsx.inventoryhandling;


import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class RawInv implements Serializable {
    private InventoryType invType;
    private ItemStack[] invItems;
    private ItemStack[] armorContents;
    private int xpLevel;

    public RawInv(InventoryType invType){
        this.invType = invType;
    }

    public void setInvItems(ItemStack[] items){
        invItems = items;
    }

    public boolean setArmorContents(ItemStack[] items){
        if (invType!=InventoryType.PLAYER)return false;
        armorContents = items;
        return true;
    }

    public boolean setXpLevel(int level) {
        if (invType != InventoryType.PLAYER) return false;
        xpLevel = level;
        return true;
    }

    public ItemStack[] getInvItems() {
        return invItems;
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public InventoryType getInvType() {
        return invType;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BukkitObjectOutputStream bout = new BukkitObjectOutputStream(out);
        bout.writeObject(this);
        bout.close();
        return out.toByteArray();
    }

    public static RawInv deserialize(byte[] arr) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(arr);
        BukkitObjectInputStream bin = new BukkitObjectInputStream(in);
        return (RawInv) bin.readObject();
    }


}
