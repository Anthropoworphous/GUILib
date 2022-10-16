package com.github.anthropoworphous.guilib.window.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventoryBuilder {
    public InventoryBuilder(InventoryType invType) {
        this.invType = invType;
        if (Arrays.stream(SUPPORTED_INVENTORY_TYPES).noneMatch(t -> t == invType)) {
            throw new IllegalArgumentException("Unsupported inventory type");
        }
    }
    public InventoryBuilder(int size) {
        if (size % 9 != 0) {
            if (size <= 6) {
                this.size = size * 9;
            } else {
                throw new IllegalArgumentException("Size is not multiple of 9");
            }
        } else {
            this.size = size;
        }
    }

    public static final InventoryType[] SUPPORTED_INVENTORY_TYPES = {
            InventoryType.CHEST,
            InventoryType.PLAYER,
            InventoryType.ENDER_CHEST,
            InventoryType.SHULKER_BOX,
            InventoryType.BARREL,
            InventoryType.HOPPER,
            InventoryType.DISPENSER,
            InventoryType.WORKBENCH,
            InventoryType.DROPPER,
            InventoryType.CRAFTING
    };

    private InventoryType invType = InventoryType.CHEST;
    private int size = 27;

    public Inventory getInventory(Component component) {
        if (invType == InventoryType.CHEST) {
            if (component == null) {
                return Bukkit.getServer().createInventory(null, size);
            } else {
                return Bukkit.getServer().createInventory(null, size, component);
            }
        } else {
            if (component == null) {
                return Bukkit.getServer().createInventory(null, invType);
            } else {
                return Bukkit.getServer().createInventory(null, invType, component);
            }
        }
    }
    public int getWidth() {
        return switch (invType) {
            case CHEST, PLAYER, ENDER_CHEST, SHULKER_BOX, BARREL -> 9;
            case HOPPER -> 5;
            case DISPENSER, WORKBENCH, DROPPER -> 3;
            case CRAFTING -> 2;
            default -> throw new IllegalStateException("Unsupported inventory type");
        };
    }
    public int getHeight() {
        return switch (invType) {
            case CHEST -> size / 9;
            case PLAYER, ENDER_CHEST, SHULKER_BOX, BARREL, DISPENSER, WORKBENCH, DROPPER -> 3;
            case CRAFTING -> 2;
            case HOPPER -> 1;
            default -> throw new IllegalStateException("Unsupported inventory type");
        };
    }
}
